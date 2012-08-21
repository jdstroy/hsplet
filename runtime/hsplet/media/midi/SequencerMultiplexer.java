/*
 * Copyright 2012 John Stroy
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hsplet.media.midi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequencer;

/**
 *
 * @author jdstroy
 */
public class SequencerMultiplexer {

    private class Monitor implements Runnable {

        private MusicClip myClip;
        private CountDownLatch gate = new CountDownLatch(1);
        
        public Monitor(MusicClip myClip) {
            this.myClip = myClip;
        }
        
        public CountDownLatch getCountDownLatch() {
            return gate;
        }

        @Override
        public void run() {
            
            boolean isMyClip = currentClip == myClip;
            while (target.isRunning() && isMyClip) {
                isMyClip = currentClip == myClip;
                try {
                    Thread.currentThread().sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SequencerMultiplexer.class.getName()).log(Level.WARNING, "Interrupted thread while sleeping.", ex);
                    if (monitor.isShutdown()) {
                        return;
                    }
                }
            }
            gate.countDown();
            myClip.stop();
        }
    }
    private ExecutorService monitor = Executors.newSingleThreadExecutor();
    private Sequencer target;
    private MusicClip currentClip;
    private List<MusicEventListener> eventListeners = new ArrayList<MusicEventListener>();

    public List<MusicEventListener> getEventListeners() {
        return eventListeners;
    }

    public void setEventListeners(List<MusicEventListener> eventListeners) {
        this.eventListeners = eventListeners;
    }

    public Sequencer getTarget() {
        return target;
    }

    public void stop() {
        _stop(currentClip, null);
    }

    @Override
    protected void finalize() throws Throwable {
        dispose();
        super.finalize();
    }

    private void dispose() {
        synchronized (this) {
            if (target.isOpen()) {
                target.stop();
                target.close();
            }
            monitor.shutdown();
        }
    }

    /**
     * Gets the current position of the sequencer in milliseconds.
     *
     * @return Millisecond position of the current clip if less than or equal to
     * Integer.MAX_VALUE, otherwise Integer.MAX_VALUE
     */
    public int getPosition() {
        long value = target.getMicrosecondPosition() / 1000;
        return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) (value);
    }

    private void _stop(MusicClip source, MusicClip destination) {

        synchronized (this) {

            try {
                // Notify all of our listeners
                for (MusicEventListener listener : eventListeners) {
                    listener.beforeMusicStop(source, destination, this);
                }
            } finally {
                // Update position before stopping.
                source.getPosition();
                target.stop();
                source.setPlaying(false);
            }

            // Notify all of our listeners
            for (MusicEventListener listener : eventListeners) {
                listener.onMusicStop(source, destination, this);
            }

            this.currentClip = null;
        }
    }

    public CountDownLatch play(MusicClip clip) throws IOException, InvalidMidiDataException {

        synchronized (this) {

            MusicClip oldClip = currentClip;

            _stop(oldClip, clip);

            for (MusicEventListener listener : eventListeners) {
                listener.beforeMusicPlay(oldClip, clip, this);
            }

            try {
                target.stop();
                setCurrentClip(clip);
                target.setSequence(clip.getInputStream());
                target.setMicrosecondPosition(0);
                    clip.setPlaying(true);
                    /*
                     * The Monitor is submitted and run here, but it
                     * automatically terminates when the clip has stopped
                     * playing, or when a new clip is playing.
                     */
                    Monitor m = new Monitor(clip);
                    monitor.submit(m);
                    return m.getCountDownLatch();

            } catch (IOException ex) {
                _stop(clip, null);
                throw ex;
            } finally {

                for (MusicEventListener listener : eventListeners) {
                    listener.onMusicPlay(oldClip, clip, this);
                }
            }
        }
    }

    public IMusicClip getCurrentClip() {
        return currentClip;
    }

    private void setCurrentClip(MusicClip currentClip) {
        this.currentClip = currentClip;
    }

    public MusicClip newClip(InputStream sequence) {
        return new MusicClip(this, sequence);
    }
}

enum SequencerPlaybackMode {

    PlaybackOnceAsynchronous(0),
    PlaybackRepeatAsynchronous(1),
    PlaybackOnceSynchronous(2);
    private int value;

    SequencerPlaybackMode(int value) {
        this.value = value;
    }
;

}

class MusicClip implements IMusicClip {

    private boolean playing;
    final private SequencerMultiplexer mux;
    private InputStream sequence;
    private int currentPosition;

    @Override
    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public MusicClip(SequencerMultiplexer sm, InputStream sequence) {
        this.mux = sm;
    }

    @Override
    public void stop() {
        synchronized (mux) {
            if (mux.getCurrentClip() == this) {
                mux.stop();
            }
            playing = false;
        }
    }

    public InputStream getInputStream() {
        return sequence;
    }

    @Override
    public CountDownLatch start() throws IOException, InvalidMidiDataException {
        synchronized (mux) {
            mux.play(this);
            CountDownLatch c = mux.play(this);
            playing = true;
            return c;
        }
    }

    public void start(SequencerPlaybackMode mode) throws IOException, InvalidMidiDataException {
        switch (mode) {
            case PlaybackOnceAsynchronous:
                start();
            case PlaybackOnceSynchronous:
                start();


            case PlaybackRepeatAsynchronous:
                mux.getEventListeners().add(new LoopMusicEventListener());
                start();
        }


    }

    public void setPosition(int seconds) {
        currentPosition = seconds;
        synchronized (mux) {
            if (mux.getCurrentClip() == this) {
                mux.getTarget().setMicrosecondPosition(seconds * 1000L);
            }
        }
    }

    @Override
    public int getPosition() {
        synchronized (mux) {
            if (mux.getCurrentClip() == this) {
                currentPosition = mux.getPosition();
            }
        }
        return currentPosition;
    }
}
