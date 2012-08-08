/*
 * $Id: Music.java,v 1.8 2006/02/05 10:08:14 Yuki Exp $
 */
package hsplet.media;

import hsplet.Context;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;

/**
 * MIDI ミュージック。
 * 
 * @author Yuki
 * @version $Revision: 1.8 $, $Date: 2006/02/05 10:08:14 $
 */
public class Music implements HSPMedia {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: Music.java,v 1.8 2006/02/05 10:08:14 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = 9143714080541551764L;

	/**
	 * メディアを読み込みオブジェクトを構築する。
	 * @param context 実行しているコンテキスト。
	 * @param fileName 読み込むメディア。
	 * @param mode 再生モード。
	 * @throws Exception 読み込めなかったとき。
	 */
	public Music(final Context context, final String fileName, final int mode) throws Exception {

		this.context = context;
		this.sequencer = MidiSystem.getSequencer();
		this.mode = mode;

		final InputStream in = context.getBufferedResource(fileName);
		if (in == null) {
			throw new FileNotFoundException(fileName + " not found");
		}
		try {

			sequencer.setSequence(in);

			sequencer.open();

		} catch (Exception e) {
			if (in != null) {
				in.close();
			}

			throw e;
		}
	}

	private Context context;

	//@Override
	protected void finalize() throws Throwable {

		dispose();

		super.finalize();
	}

	public void dispose() {
            synchronized(mutex) {
		if (sequencer.isOpen()) {
                    
			sequencer.stop();
			sequencer.close();
                }
            }
	}

	final private Sequencer sequencer;

	private final int mode;

	private boolean playing;

	public boolean isPlaying() {
		return playing;
	}

	public void play() {
            synchronized(mutex) {
		if (!sequencer.isOpen()) {
			return;
		}

		playing = true;

		switch (mode) {
		case 0:
                    
			sequencer.start();
                    
			return;
		case 1:
			new Thread() {

				public void run() {

					while (isPlaying()) {
                                            synchronized(mutex) {
						if (!sequencer.isOpen()) {
							break;
						}

						setPosition(0);
						sequencer.start();
                                            }
						try {
							while (sequencer.isRunning()) {
								Thread.sleep(100);
							}
						} catch (InterruptedException e) {
							Logger.getLogger(Music.class.getName()).log(Level.SEVERE, null, e);
						}

					}
				}

			}.start();
			return;
		case 2:
			sequencer.start();
			try {
				while (sequencer.isRunning()) {
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
				Logger.getLogger(Music.class.getName()).
                                        log(Level.SEVERE, null, e);
			}
			return;
		}
            }
	}

	public void stop() {

            synchronized(mutex) {
		if (sequencer.isOpen()) {
			sequencer.stop();
		}
		playing = false;
            }
	}

	public void setPosition(int value) {

            synchronized(mutex) {
		sequencer.setMicrosecondPosition(value * 1000L);
            }
	}

	public int getPosition() {

		return (int) (sequencer.getMicrosecondPosition() / 1000);
	}
        
        private final Object mutex = new Object();

}
