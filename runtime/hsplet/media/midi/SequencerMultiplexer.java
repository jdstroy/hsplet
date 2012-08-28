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
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

/**
 *
 * @author jdstroy
 */
public class SequencerMultiplexer {

    public SequencerMultiplexer(Sequencer target) {
        this.target = target;
    }

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

            do {
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
                if (myClip.isLoop() && !target.isRunning()) {
                    try {
                        SequencerMultiplexer.this.playConditional(myClip);
                    } catch (IOException ex) {
                        Logger.getLogger(SequencerMultiplexer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } while (myClip.isLoop() && isMyClip);

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
                if (source != null) {
                    source.getPosition();
                }
                if (target.isOpen() && target.isRunning()) {
                    target.stop();
                }
                if (source != null) {
                    source.setPlaying(false);
                }
            }

            // Notify all of our listeners
            for (MusicEventListener listener : eventListeners) {
                listener.onMusicStop(source, destination, this);
            }

            this.currentClip = null;
        }
    }

    void playConditional(MusicClip clip) throws IOException {
        synchronized (this) {
            if (currentClip == clip) {
                try {
                    target.setSequence(clip.getInputStream());
                } catch (Exception ex) {
                    throw new IOException("Couldn't get MIDI input stream.", ex);
                }
                target.setMicrosecondPosition(0);
                target.start();
                clip.setPlaying(true);
            }
        }
    }

    CountDownLatch play(MusicClip clip) throws InvalidMidiDataException, MidiUnavailableException, IOException {

        synchronized (this) {

            MusicClip oldClip = currentClip;

            _stop(oldClip, clip);

            for (MusicEventListener listener : eventListeners) {
                listener.beforeMusicPlay(oldClip, clip, this);
            }

            try {
                if (!target.isOpen()) {
                    target.open();
                }
                if (target.isRunning()) {
                    target.stop();
                }
                setCurrentClip(clip);
                try {
                    target.setSequence(clip.getInputStream());
                } catch (Exception ex) {
                    throw new IOException("Couldn't get MIDI input stream.", ex);
                }
                target.setMicrosecondPosition(0);
                target.start();
                clip.setPlaying(true);
                /*
                 * The Monitor is submitted and run here, but it automatically
                 * terminates when the clip has stopped playing, or when a new
                 * clip is playing.
                 */
                Monitor m = new Monitor(clip);
                monitor.submit(m);
                return m.getCountDownLatch();

            } catch (Exception ex) {
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

    public MusicClip newClip(Callable<InputStream> sequence) {
        return new MusicClip(this, sequence);
    }
}
