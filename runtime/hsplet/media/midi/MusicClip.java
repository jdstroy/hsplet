/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.media.midi;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

/**
 *
 * @author jdstroy
 */
public class MusicClip implements IMusicClip {

    private boolean playing;
    final private SequencerMultiplexer mux;
    private Callable<InputStream> sequence;
    private int currentPosition;

    @Override
    public boolean isPlaying() {
        return playing;
    }

    void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public MusicClip(SequencerMultiplexer sm, Callable<InputStream> sequence) {
        this.mux = sm;
        this.sequence = sequence;
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

    public InputStream getInputStream() throws Exception {
        return sequence.call();
    }

    CountDownLatch start() throws IOException, InvalidMidiDataException, MidiUnavailableException {
        synchronized (mux) {
            mux.play(this);
            CountDownLatch c = mux.play(this);
            playing = true;
            return c;
        }
    }

    public void start(SequencerPlaybackMode mode) throws IOException, InvalidMidiDataException, MidiUnavailableException {
        switch (mode) {
            case PlaybackOnceAsynchronous:
                loop = false;
                start();
                return;
            case PlaybackOnceSynchronous:
                loop = false;
                try {
                    start().await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MusicClip.class.getName()).log(Level.SEVERE, "Interrupted while waiting for music to end.", ex);
                }
                return;
            case PlaybackRepeatAsynchronous:
                loop = true;
                start();
        }
    }
    
    private boolean loop = false;

    public boolean isLoop() {
        return loop;
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
