/*
 * $Id: Mp3.java,v 1.7 2006/02/05 10:08:14 Yuki Exp $
 */
package hsplet.media;

import hsplet.Context;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.SampleBuffer;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;

/**
 * MP3 �T�E���h�B
 * <p>
 * JavaLayer ���C�u�������K�v�B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.7 $, $Date: 2006/02/05 10:08:14 $
 */
public class Mp3 implements HSPMedia {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: Mp3.java,v 1.7 2006/02/05 10:08:14 Yuki Exp $";

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = 4705909472249775632L;

	/**
	 * ���f�B�A��ǂݍ��݃I�u�W�F�N�g���\�z����B
	 * @param context ���s���Ă���R���e�L�X�g�B
	 * @param fileName �ǂݍ��ރ��f�B�A�B
	 * @param mode �Đ����[�h�B
	 * @throws Exception �ǂݍ��߂Ȃ������Ƃ��B
	 */
	public Mp3(final Context context, final String fileName, final int mode) throws Exception {

		this.context = context;
		this.dir = context.curdir;
		this.fileName = fileName;
		this.mode = mode;

		initialize();

	}

	//@Override
	protected void finalize() throws Throwable {

		dispose();

		super.finalize();
	}

	public void dispose() {

		if (bitstream != null) {
			try {
				bitstream.close();
			} catch (BitstreamException e) {
				e.printStackTrace();
			}
		}

		if (audio != null) {
			audio.close();
		}

	}

	private Context context;

	private URL dir;

	private String fileName;

	private Bitstream bitstream;

	private Decoder decoder;

	private AudioDevice audio;

	private int mode;

	protected boolean playing;

	public boolean isPlaying() {
		return playing;
	}

	public void play() {

		// �ŏ�����Đ�����

		playing = true;
		referenceTime = System.currentTimeMillis();
		referencePosition = audio.getPosition();

		switch (mode) {
		case 0: {
			final Thread thread = new Thread() {

				//@Override
				public void run() {

					try {

						while (isPlaying()) {
							if (!nextFrame(false)) {
								break;
							}
						}
						Mp3.this.stop();
					} catch (JavaLayerException e) {
						e.printStackTrace();
					}
				}
			};
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.start();
			return;
		}
		case 1: {
			final Thread thread = new Thread() {

				//@Override
				public void run() {

					try {
						while (isPlaying()) {
							while (isPlaying()) {
								if (!nextFrame(false)) {
									break;
								}
							}
							setPosition(0);
						}
						Mp3.this.stop();
					} catch (JavaLayerException e) {
						e.printStackTrace();
					}
				}
			};
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.start();
			return;
		}
		case 2:
			try {
				while (isPlaying()) {
					if (!nextFrame(false)) {
						break;
					}
				}
				stop();
			} catch (JavaLayerException e) {
				e.printStackTrace();
			}
			return;
		}

	}

	public void stop() {

		playing = false;
	}

	public void setPosition(int value) {

		if (value < getPosition()) {
			try {
				initialize();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}

		while (getPosition() < value) {
			try {
				if (!nextFrame(true)) {
					break;
				}
			} catch (JavaLayerException e) {
				e.printStackTrace();
				break;
			}
		}

	}

	private long skipLength;

	// �Ȃ��� JRE 1.5 �ł͍Đ����Ԃ��Ԃ��B
	// ���O�̎��Ԃ��L�����Ă����ĕ␳����B

	private long referencePosition;

	private long referenceTime;

	public int getPosition() {

		if (playing) {
			long playTime = System.currentTimeMillis() - referenceTime;
			long playPosition = audio.getPosition() - referencePosition;

			if (playTime < playPosition - 1000) {
				referenceTime -= (playPosition - playTime) / 10;
			} else if (playTime < playPosition - 10) {
				referenceTime -= 10;
			} else if (playTime > playPosition + 1000) {
				referenceTime += (playTime - playPosition) / 10;
			}
			if (playTime > playPosition + 10) {
				referenceTime += 10;
			}

			return (int) (playTime + referencePosition + skipLength / 1000);
		} else {
			return (int) (audio.getPosition() + skipLength / 1000);
		}
	}

	protected boolean nextFrame(final boolean skip) throws JavaLayerException {

		final Header h = bitstream.readFrame();

		if (h == null) {
			return false;
		}

		final SampleBuffer output = (SampleBuffer) decoder.decodeFrame(h, bitstream);

		if (output == null) {
			return false;
		}

		if (skip) {

			skipLength += h.ms_per_frame() * 1000;
			//skipLength += output.getBufferLength() * 1000000/ output.getChannelCount() / output.getSampleFrequency();
		} else {

			audio.write(output.getBuffer(), 0, output.getBufferLength());
		}

		bitstream.closeFrame();
		getPosition();

		return true;
	}

	protected void initialize() throws Exception {

		dispose();

		skipLength = 0;

		final InputStream in = context.getResource(dir, fileName);
		if (in == null) {
			throw new FileNotFoundException(fileName + " not found");
		}
		try {
			bitstream = new Bitstream(in);

			decoder = new Decoder();

			audio = FactoryRegistry.systemRegistry().createAudioDevice();

			audio.open(decoder);

		} catch (Exception e) {
			if (in != null) {
				in.close();
			}
			throw e;
		}
	}
}
