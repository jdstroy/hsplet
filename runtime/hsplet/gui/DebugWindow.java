package hsplet.gui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * �f�o�b�O�E�B���h�E�B
 * 
 * @author Yuki
 * @version $Revision: 1.4 $, $Date: 2006/03/26 14:35:36 $
 */
public class DebugWindow extends JFrame {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: DebugWindow.java,v 1.4 2006/03/26 14:35:36 Yuki Exp $";

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = -9025077661160161316L;

	private PrintStream oldOut;

	private PrintStream oldError;

	private JScrollPane scrollPane = null;

	JTextArea logText = null;

	/**
	 * This method initializes 
	 * 
	 */
	public DebugWindow() {
		super();
		initialize();

		oldOut = System.out;
		oldError = System.err;

		final ByteArrayOutputStream logBuffer = new ByteArrayOutputStream();

		try {
			System.setOut(new PrintStream(new OutputStream() {

				//@Override
				public void write(byte[] b) throws IOException {
					logBuffer.write(b);
					try {
						logText.setText(new String(logBuffer.toByteArray(),
								"MS932"));
					} catch (Exception e) {
					}
				}

				//@Override
				public void write(byte[] b, int off, int len) {
					logBuffer.write(b, off, len);

					try {
						logText.setText(new String(logBuffer.toByteArray(),
								"MS932"));
					} catch (Exception e) {
					}
				}

				//@Override
				public void write(int b) {
					logBuffer.write(b);

					try {
						logText.setText(new String(logBuffer.toByteArray(),
								"MS932"));
					} catch (Exception e) {
					}

				}
			}, true, "MS932"));
			System.setErr(System.out);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//setLocationByPlatform(true);

		setVisible(true);
	}

	//@Override
	public void dispose() {
		super.dispose();

		System.setOut(oldOut);
		System.setErr(oldError);
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setSize(new java.awt.Dimension(493, 210));
		this.setContentPane(getScrollPane());
		this.setTitle("Debug Window");
	}

	/**
	 * This method initializes scrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getLogText());
		}
		return scrollPane;
	}

	/**
	 * This method initializes logText	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	private JTextArea getLogText() {
		if (logText == null) {
			logText = new JTextArea();
			logText
					.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN,
							10));
		}
		return logText;
	}

} //  @jve:decl-index=0:visual-constraint="33,31"
