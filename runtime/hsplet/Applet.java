/*
 * $Id: Applet.java,v 1.9 2006/05/09 11:57:31 Yuki Exp $
 */
package hsplet;

import hsplet.gui.Bmscr;
import hsplet.gui.ContentPane;
import hsplet.gui.HSPScreen;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JApplet;
import javax.swing.UIManager;

/**
 * HSPLet �̃��C���A�v���b�g�N���X�B
 * 
 * @author Yuki
 * @version $Revision: 1.9 $, $Date: 2006/05/09 11:57:31 $
 */
public final class Applet extends JApplet implements HSPScreen {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: Applet.java,v 1.9 2006/05/09 11:57:31 Yuki Exp $";

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = -1458193602845132311L;

	/** �J�n�N���X����n���p�����[�^���B */
	public static final String START_CLASSNAME_PARAM = "startClass";

	/** �f�t�H���g�̊J�n�N���X���B*/
	public static final String DEFAULT_START_CLASSNAME = "start";

	private final Context context = new Context();

	/**
	 * �I�u�W�F�N�g���\�z����B
	 */
	public Applet() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setContentPane(new ContentPane(this));
		this.info = new Bmscr(this, (ContentPane) getContentPane());

		getContentPane().addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				if (getJMenuBar() != null) {

					setSize(getContentPane().getWidth(), getJMenuBar().getHeight() + getContentPane().getHeight());
				} else {
					setSize(getContentPane().getSize());
				}
			}
		});
	}

	//@Override
	public void init() {

		try {
			init(Class.forName(getParameter(START_CLASSNAME_PARAM) == null ? DEFAULT_START_CLASSNAME
					: getParameter(START_CLASSNAME_PARAM)));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * �N���X���w�肵�ăA�v���b�g���J�n����B
	 * @param codeClass ���s����R�[�h�̃N���X�B
	 */
	public void init(final Class codeClass) {

		hsplet.variable.Operand.context = context;

		context.init(this, codeClass);

		info.init(getBackground(), getSize());

	}

	//@SuppressWarnings("unchecked")
	//@Override
	public void start() {

		// ����N�����͂܂��t�H�[�J�X�𓾂�B
		new Timer().schedule(new TimerTask() {

			//@Override
			public void run() {
				requestFocus();

			}
		}, 100);

		context.start();
	}

	//@Override
	public void stop() {
		context.stop();

		System.gc();
	}

	//@Override
	public void destroy() {
		context.stop();

		System.gc();
	}

	private Bmscr info;

	public Bmscr getBmscr() {

		return info;
	}

	//@Override
	public void update(Graphics g) {

		paint(g);
	}

	public Component asComponent() {

		return this;
	}

	public void setTitle(final String text) {

		showStatus(text);
	}

	public Dimension getPreferredSize() {
		return info.contents.getPreferredSize();
	}

}
