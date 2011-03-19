/*
 * $Id: Buffer.java,v 1.4 2006/01/21 12:48:16 Yuki Exp $
 */
package hsplet.gui;

import java.awt.Component;
import java.awt.Dimension;

/**
 * buffer ��ʁB
 * 
 * @author Yuki
 * @version $Revision: 1.4 $, $Date: 2006/01/21 12:48:16 $
 */
public class Buffer implements HSPScreen {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: Buffer.java,v 1.4 2006/01/21 12:48:16 Yuki Exp $";

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = 6515895350317408339L;

	private final Bmscr info;

	private final Dimension size;

	/**
	 * �I�u�W�F�N�g���\�z����B
	 * @param size �o�b�t�@�T�C�Y�B
	 */
	public Buffer(final Dimension size) {

		this.size = size;
		this.info = new Bmscr(this, null, size);
	}

	public Component asComponent() {

		return null;
	}

	public Bmscr getBmscr() {

		return info;
	}

	public Dimension getSize() {

		return size;
	}

	public void setTitle(final String text) {

	}
}
