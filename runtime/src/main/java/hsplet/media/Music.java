/*
 * $Id: Music.java,v 1.8 2006/02/05 10:08:14 Yuki Exp $
 */
package hsplet.media;

import hsplet.Context;
import hsplet.media.midi.MusicClip;
import hsplet.media.midi.SequencerMultiplexer;
import hsplet.media.midi.SequencerPlaybackMode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

/**
 * MIDI ミュージック。
 *
 * @author Yuki
 * @version $Revision: 1.8 $, $Date: 2006/02/05 10:08:14 $
 */
public class Music implements HSPMedia {

    /**
     * このクラスを含むソースファイルのバージョン文字列。
     */
    private static final String fileVersionID = "$Id: Music.java,v 1.8 2006/02/05 10:08:14 Yuki Exp $";
    /**
     * 直列化復元時に、データの互換性を確認するためのバージョン番号。
     */
    private static final long serialVersionUID = 9143714080541551764L;
    private SequencerMultiplexer multiplexer;
    private final MusicClip clip;

    /**
     * メディアを読み込みオブジェクトを構築する。
     *
     * @param context 実行しているコンテキスト。
     * @param fileName 読み込むメディア。
     * @param mode 再生モード。
     * @throws Exception 読み込めなかったとき。
     */
    public Music(final Context context, final String fileName, final int mode, SequencerMultiplexer multiplexer) throws Exception {
        this.multiplexer = multiplexer;
        this.context = context;
        this.sequencer = MidiSystem.getSequencer();
        this.mode = mode;

        final InputStream in = context.getBufferedResource(fileName);
        if (in == null) {
            throw new FileNotFoundException(fileName + " not found");
        } else {
            in.close();
        }

        
        clip = multiplexer.newClip(new Callable<InputStream>() {
            @Override
            public InputStream call() {
                return context.getBufferedResource(fileName);
            }
            
        });
    }
    private Context context;
    final private Sequencer sequencer;
    private final int mode;

    public boolean isPlaying() {
        return clip.isPlaying();
    }
    private static final SequencerPlaybackMode[] modes = new SequencerPlaybackMode[]{
        SequencerPlaybackMode.PlaybackOnceAsynchronous,
        SequencerPlaybackMode.PlaybackRepeatAsynchronous,
        SequencerPlaybackMode.PlaybackOnceSynchronous,};

    public void play() {

        if (mode >= modes.length) {
            throw new IllegalArgumentException("Invalid mode specified: " + mode);
        }
        try {
            clip.start(modes[mode]);
        } catch (IOException ex) {
            Logger.getLogger(Music.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidMidiDataException ex) {
            Logger.getLogger(Music.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MidiUnavailableException ex) {
            Logger.getLogger(Music.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stop() {
        clip.stop();
    }

    public void setPosition(int value) {
        clip.setPosition(value);
    }

    public int getPosition() {
        return clip.getPosition();
    }

    @Override
    public void dispose() {
        clip.stop();
    }
}
