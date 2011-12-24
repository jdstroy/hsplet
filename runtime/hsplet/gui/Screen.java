/*
 * $Id: Screen.java,v 1.5 2006/01/21 12:48:16 Yuki Exp $
 */
package hsplet.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

/**
 * screen �E�B���h�E�B
 * 
 * @author Yuki
 * @version $Revision: 1.5 $, $Date: 2006/01/21 12:48:16 $
 */
public class Screen extends JFrame implements HSPScreen {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: Screen.java,v 1.5 2006/01/21 12:48:16 Yuki Exp $";

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = 8282094332529368460L;

	/**
	 * �I�u�W�F�N�g���\�z����B
	 * @param bufferSize �o�b�t�@�T�C�Y�B
	 * @param mode ���[�h�B
	 * @param location ������W�B
	 * @param size �\���T�C�Y�B
	 */
	public Screen(final Dimension bufferSize, final int mode,
			final Point location, final Dimension size) {

		setContentPane(new ContentPane(this));

		getContentPane().setSize(size);
		((ContentPane)getContentPane()).setPreferredSize(size);

		pack();

		getContentPane().addComponentListener(new ComponentAdapter(){public void componentResized(ComponentEvent e) {
			pack();
		}});

		if (location.x == Integer.MIN_VALUE || location.y == Integer.MIN_VALUE) {
		//	setLocationByPlatform(true);
		} else {
			setLocation(location);
		}

		setResizable((mode & 4) == 0);

		info = new Bmscr(this, (ContentPane)getContentPane(), bufferSize);
	}

	private final Bmscr info;

	public Component asComponent() {

		return this;
	}

	public Bmscr getBmscr() {

		return info;
	}

	//@Override
	public Dimension getSize() {

		return getContentPane().getSize();
	}

}