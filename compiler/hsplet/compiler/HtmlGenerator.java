/*
 * $Id: HtmlGenerator.java,v 1.4 2006/01/16 19:34:22 Yuki Exp $
 */
package hsplet.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * HSPLet �ŖړI�̃R�[�h�����s���邽�߂� HTML �𐶐�����N���X�B
 * <p>
 * �e���v���[�g��ǂݍ���ŁA�K�v�ȉӏ������������ďo�͂���B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.4 $, $Date: 2006/01/16 19:34:22 $
 */
public final class HtmlGenerator implements Serializable {

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = -5216057552518726784L;

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: HtmlGenerator.java,v 1.4 2006/01/16 19:34:22 Yuki Exp $";

	/** �e���v���[�g�t�@�C�����B */
	private static final String TEMPLATE_NAME = "template.html";

	/** �N���X����u��������^�O�B */
	private static final String START_CLASS_NAME_TAG_REGEX = "\\{START_CLASS_NAME\\}";

	/** �^�C�g����u��������^�O�B */
	private static final String TITLE_TAG_REGEX = "\\{TITLE\\}";

	/** mp3�p���C�u������u��������^�O�B */
	private static final String LIBS_TAG_REGEX = "\\{LIBS\\}";

	/** ������u��������^�O�B */
	private static final String WIDTH_TAG_REGEX = "\\{WIDTH\\}";

	/** ������u��������^�O�B */
	private static final String HEIGHT_TAG_REGEX = "\\{HEIGHT\\}";

	/** ���s����N���X���B */
	private final String className;

	/** �y�[�W�^�C�g�� */
	private final String title;

	/** �g�����C�u�����B */
	private final Collection libs;

	/** �����B */
	private final int width;

	/** �����B */
	private final int height;

	/** �e���v���[�g�t�@�C�� */
	private final File template;

	/**
	 * ���s����N���X�����w�肵�ăI�u�W�F�N�g���\�z����B
	 * 
	 * @param className ���s����N���X���B
	 * @param title �y�[�W�^�C�g���B
	 * @param libs �g�����C�u�����̃��X�g�B
	 * @param h ����
	 * @param w ����
	 * @param template �e���v���[�g�t�@�C���B
	 */
	public HtmlGenerator(final String className, final String title,
			final Collection libs, final int w, int h, final File template) {

		this.className = className;
		this.title = title;
		this.libs = libs;
		this.width = w;
		this.height = h;
		this.template = template;
	}

	/**
	 * HTML �𐶐�����B
	 * 
	 * @param out �o�͐�B
	 * @throws IOException ���o�̓G���[�����������Ƃ��B
	 */
	public void generate(final OutputStream out) throws IOException {

		final PrintWriter w = new PrintWriter(out, true);

		final InputStream in = template == null ? getClass()
				.getResourceAsStream(TEMPLATE_NAME) : new FileInputStream(
				template);
		try {

			final BufferedReader r = new BufferedReader(new InputStreamReader(
					in));

			for (;;) {

				final String line = r.readLine();
				if (line == null) {
					break;
				}

				final StringBuffer extLibNames = new StringBuffer();
				for (final Iterator i = libs.iterator(); i.hasNext();) {
					extLibNames.append(i.next() + ",");
				}

				w.println(line
						.replaceAll(START_CLASS_NAME_TAG_REGEX, className)
						.replaceAll(LIBS_TAG_REGEX, extLibNames.toString())
						.replaceAll(WIDTH_TAG_REGEX, "" + width).replaceAll(
								HEIGHT_TAG_REGEX, "" + height).replaceAll(
								TITLE_TAG_REGEX, title));
			}
		} finally {
			in.close();
		}

	}

}
