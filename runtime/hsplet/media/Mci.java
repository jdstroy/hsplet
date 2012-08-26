/*
 * $Id: Mci.java,v 1.6 2006/02/05 10:08:14 Yuki Exp $
 */
package hsplet.media;

import hsplet.Context;
import hsplet.media.midi.SequencerMultiplexer;
import hsplet.util.Conversion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

/**
 * MCI �R�}���h����������N���X�B
 * <p>
 * ���㋐��ɂȂ�����ʃ��C�u�����ɕ����邩���B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.6 $, $Date: 2006/02/05 10:08:14 $
 */
public class Mci implements Serializable {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: Mci.java,v 1.6 2006/02/05 10:08:14 Yuki Exp $";

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = -3044829753912949031L;

	/**
	 * �I�u�W�F�N�g���\�z����B
	 * @param context ���s���Ă���R���e�L�X�g�B
	 */
	public Mci(final Context context) {
            this.context = context;
            try {
                this.sequenceMultiplexer = new SequencerMultiplexer(MidiSystem.getSequencer());
            } catch (MidiUnavailableException ex) {
                Logger.getLogger(Mci.class.getName()).log(Level.SEVERE, "MIDI is unavailable!", ex);
            }
            
	}
        
        private SequencerMultiplexer sequenceMultiplexer;

	private final Context context;

	private Map<String, HSPMedia> medias = new HashMap<String, HSPMedia>();

	/**
	 * mci �R�}���h�����s����B
	 * @param command ���s����R�}���h������B
	 */
	public void exec(final String command) {

		final String[] commands = split(command);
                final String firstWord = commands[0];

		if (commands.length < 1) {
			return;
		}

		if (firstWord.equalsIgnoreCase("open")) {
			open(commands);
		} else if (firstWord.equalsIgnoreCase("set")) {
			set(commands);
		} else if (firstWord.equalsIgnoreCase("seek")) {
			seek(commands);
		} else if (firstWord.equalsIgnoreCase("play")) {
			play(commands);
		} else if (firstWord.equalsIgnoreCase("status")) {
			status(commands);
		} else if (firstWord.equalsIgnoreCase("stop")) {
			stop(commands);
		} else if (firstWord.equalsIgnoreCase("close")) {
			close(commands);
		} else {
			throw new UnsupportedOperationException("mci ���� " + command + " �̓T�|�[�g����Ă��܂���B");
		}

	}

	private void open(final String[] commands) {

		try {
			final String fileName = commands[1];

			final String name;
			if (commands.length >= 4 && commands[2].equalsIgnoreCase("alias")) {
				name = commands[3];
			} else {
				name = fileName;
			}

			final HSPMedia media = open(fileName, 0);

			medias.put(name, media);

			context.stat.value = 0;

		} catch (Exception e) {
			e.printStackTrace();

			context.stat.value = -1;
		}
	}

	/**
	 * ���f�B�A���I�[�v������B
	 * @param fileName �t�@�C�����B
	 * @param mode �I�[�v�����[�h�B
	 * @return �J���ꂽ���f�B�A�B
	 * @throws Exception �J���Ȃ������Ƃ��B
	 */
	public HSPMedia open(final String fileName, final int mode) throws Exception {

		HSPMedia media = null;

		if (fileName.toLowerCase().endsWith(".mp3")) {

			try {
				media = new Mp3(context, fileName, mode);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} else if (fileName.toLowerCase().endsWith(".mid")) {

			try {
				media = new Music(context, fileName, mode, sequenceMultiplexer);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		if (media == null) {

			try {
				media = new Sound(context, fileName, mode);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		if (media == null) {
			try {
				media = new Music(context, fileName, mode, sequenceMultiplexer);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		if (media == null) {
			try {
				media = new Mp3(context, fileName, mode);
			} catch (Exception e) {
				throw e;
			} catch (Throwable e) {
				throw new Exception(e);
			}
		}

		return media;
	}

	private final void set(final String[] commands) {

		final HSPMedia media = (HSPMedia) medias.get(commands[1]);

		if (media == null) {
			context.stat.value = -1;
			return;
		}

		final String prop = commands[2];

		if (prop.equalsIgnoreCase("time")) {

			// TODO time �ݒ�
		}
	}

	private void play(final String[] commands) {

		final HSPMedia media = (HSPMedia) medias.get(commands[1]);

		if (media == null) {
			context.stat.value = -1;
			return;
		}

		media.play();
	}

	private void seek(final String[] commands) {

		final HSPMedia media = (HSPMedia) medias.get(commands[1]);

		if (media == null) {
			context.stat.value = -1;
			return;
		}

		if (!commands[2].equalsIgnoreCase("to")) {
			context.stat.value = -1;
			return;
		}

		final String position = commands[3];

		media.setPosition(Conversion.strtoi(position));

		context.stat.value = 0;
	}

	private void status(final String[] commands) {

		final HSPMedia media = (HSPMedia) medias.get(commands[1]);

		if (media == null) {
			context.stat.value = -1;
			return;
		}

		final String prop = commands[2];

		if (prop.equalsIgnoreCase("position")) {

			context.stat.value = media.getPosition();
		}

	}

	private void close(String[] commands) {

		final HSPMedia media = (HSPMedia) medias.get(commands[1]);

		if (media == null) {
			context.stat.value = -1;
			return;
		}

		media.dispose();
	}

	private void stop(String[] commands) {

		final HSPMedia media = (HSPMedia) medias.get(commands[1]);

		if (media == null) {
			context.stat.value = -1;
			return;
		}

		media.stop();
	}

	private static String[] split(final String command) {

		final List<String> result = new ArrayList<String>();

		int i = 0;
		while (i < command.length()) {

			final char ch = command.charAt(i);
			if (Character.isWhitespace(ch)) {
				++i;
			} else if (ch == '\"') {

				int end = command.indexOf('\"', i + 1);

				if (end < 0) {
					end = command.length();
				}

				result.add(command.substring(i + 1, end));

				i = end + 1;

			} else {

				final int start = i;

				while (i < command.length() && !Character.isWhitespace(command.charAt(i))) {
					++i;
				}

				result.add(command.substring(start, i));

			}

		}

		return (String[]) result.toArray(new String[0]);
	}

	/**
	 * �g�p���Ă��郊�\�[�X��j������B
	 */
	public void dispose() {

		for (final Iterator i = medias.values().iterator(); i.hasNext();) {
			final HSPMedia media = (HSPMedia) i.next();

			media.stop();
			media.dispose();
		}
		medias.clear();
	}


}
