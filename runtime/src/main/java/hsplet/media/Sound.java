/*
 * $Id: Sound.java,v 1.10 2006/05/09 11:57:31 Yuki Exp $
 */
package hsplet.media;

import hsplet.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;

/**
 * AIFF/AU/WAVE サウンド。
 * 
 * @author Yuki
 * @version $Revision: 1.10 $, $Date: 2006/05/09 11:57:31 $
 */
public class Sound implements HSPMedia {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: Sound.java,v 1.10 2006/05/09 11:57:31 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = 4437980027030449232L;

	/**
	 * メディアを読み込みオブジェクトを構築する。
	 * @param context 実行しているコンテキスト。
	 * @param fileName 読み込むメディア。
	 * @param mode 再生モード。
	 * @throws Exception 読み込めなかったとき。
	 */
	public Sound(final Context context, final String fileName, final int mode) throws Exception {

		this.context = context;
		this.mode = mode;

		final InputStream in = context.getBufferedResource(fileName);
		if (in == null) {
			throw new FileNotFoundException(fileName + " not found");
		}
		
		try {

			AudioInputStream ais = AudioSystem.getAudioInputStream(in);

			final AudioFormat f = ais.getFormat();
			
			if ( f.getEncoding().equals( AudioFormat.Encoding.ULAW ) ) {
				AudioFormat l  = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, f.getSampleRate(), f.getSampleSizeInBits(), f.getChannels(),
						f.getFrameSize(), f.getFrameRate(), false );
				
				ais = new AudioInputStream( new InputStream(){
					public int read() throws IOException {
						return (u2l[in.read() & 0xFF]>>8)+128;
					}
					public int read(byte[] b) throws IOException {
						final int length = in.read(b);
						for( int i = 0; i<length; ++i ) {
							
							b[i] = (byte)( (u2l[b[i] & 0xFF]>>8)+128 );
						}
						return length;
					}
					public int read(byte[] b, int off, int len) throws IOException {
						int length = in.read(b,off,len);
						for( int i = off; i<off+length; ++i ) {
							
							b[i] = (byte)( (u2l[b[i] & 0xFF]>>8)+128 );
						}
						return length;
					}
				}, l, ais.getFrameLength() );
			}

			this.clip = (Clip) AudioSystem.getLine( new Line.Info(Clip.class));
			
			clip.open(ais);
			
		} catch (Exception e) {
			
			if (in != null) {
				in.close();
			}
			
			throw e;
		}

	}
	
	private static short[] u2l = {
	    -32124, -31100, -30076, -29052, -28028, -27004, -25980, -24956,
	    -23932, -22908, -21884, -20860, -19836, -18812, -17788, -16764,
	    -15996, -15484, -14972, -14460, -13948, -13436, -12924, -12412,
	    -11900, -11388, -10876, -10364, -9852, -9340, -8828, -8316,
	    -7932, -7676, -7420, -7164, -6908, -6652, -6396, -6140,
	    -5884, -5628, -5372, -5116, -4860, -4604, -4348, -4092,
	    -3900, -3772, -3644, -3516, -3388, -3260, -3132, -3004,
	    -2876, -2748, -2620, -2492, -2364, -2236, -2108, -1980,
	    -1884, -1820, -1756, -1692, -1628, -1564, -1500, -1436,
	    -1372, -1308, -1244, -1180, -1116, -1052, -988, -924,
	    -876, -844, -812, -780, -748, -716, -684, -652,
	    -620, -588, -556, -524, -492, -460, -428, -396,
	    -372, -356, -340, -324, -308, -292, -276, -260,
	    -244, -228, -212, -196, -180, -164, -148, -132,
	    -120, -112, -104, -96, -88, -80, -72, -64,
	    -56, -48, -40, -32, -24, -16, -8, 0,
	    32124, 31100, 30076, 29052, 28028, 27004, 25980, 24956,
	    23932, 22908, 21884, 20860, 19836, 18812, 17788, 16764,
	    15996, 15484, 14972, 14460, 13948, 13436, 12924, 12412,
	    11900, 11388, 10876, 10364, 9852, 9340, 8828, 8316,
	    7932, 7676, 7420, 7164, 6908, 6652, 6396, 6140,
	    5884, 5628, 5372, 5116, 4860, 4604, 4348, 4092,
	    3900, 3772, 3644, 3516, 3388, 3260, 3132, 3004,
	    2876, 2748, 2620, 2492, 2364, 2236, 2108, 1980,
	    1884, 1820, 1756, 1692, 1628, 1564, 1500, 1436,
	    1372, 1308, 1244, 1180, 1116, 1052, 988, 924,
	    876, 844, 812, 780, 748, 716, 684, 652,
	    620, 588, 556, 524, 492, 460, 428, 396,
	    372, 356, 340, 324, 308, 292, 276, 260,
	    244, 228, 212, 196, 180, 164, 148, 132,
	    120, 112, 104, 96, 88, 80, 72, 64,
	    56, 48, 40, 32, 24, 16, 8, 0
	};

	private Context context;

	//@Override
	protected void finalize() throws Throwable {

		dispose();

		super.finalize();
	}

	public void dispose() {

		if (clip.isOpen()) {
			clip.stop();
			clip.close();
		}

	}

	private final javax.sound.sampled.Clip clip;

	private final int mode;

	public void play() {

		if (!clip.isOpen()) {
			return;
		}

		switch (mode) {
		case 0:
			clip.start();
			return;
		case 1:
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			return;
		case 2:
			clip.start();
			try {
				while (isPlaying()) {
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}

	}

	public boolean isPlaying() {
		return clip.isRunning();
	}

	public void stop() {

		if (clip.isOpen()) {
			clip.stop();
		}
	}

	public void setPosition(int value) {

		clip.setMicrosecondPosition(value * 1000L);
	}

	public int getPosition() {

		return (int) (clip.getMicrosecondPosition() / 1000);
	}
}
