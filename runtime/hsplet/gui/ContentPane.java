/*
 * $Id: ContentPane.java,v 1.9 2006/02/27 15:52:05 Yuki Exp $
 */
package hsplet.gui;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * HSPLet �̊e��ʂ́u���e���v�������I�u�W�F�N�g�B
 * <p>
 * �R���g���[�������̒��ɔz�u����ق��A�o�b�N�o�b�t�@���g�p�����_�u���o�b�t�@�����O���T�|�[�g����B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.9 $, $Date: 2006/02/27 15:52:05 $
 */
public class ContentPane extends JPanel {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: ContentPane.java,v 1.9 2006/02/27 15:52:05 Yuki Exp $";

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = 8376446656431845833L;

	/**
	 * �I�u�W�F�N�g���쐬����B
	 * @param owner �I�[�i�[�ƂȂ��ʁB
	 */
	public ContentPane(final HSPScreen owner) {

		this.owner = owner;

		setLayout(null);
	}

	private final HSPScreen owner;

	//@Override
	protected void paintComponent(Graphics g) {

		final Bmscr bmscr = owner.getBmscr();

		if (bmscr != null) {
			synchronized (bmscr.repaintLock) {
				final int ox = bmscr.originx;
				final int oy = bmscr.originy;

				g.drawImage(bmscr.backImage, -ox, -oy, null);

				g.setColor(getBackground());
				g.fillRect(0, 0, getWidth(), -oy);
				g.fillRect(0, -oy, -ox, bmscr.backImage.getHeight());
				g.fillRect(bmscr.backImage.getWidth() - ox, -oy, ox, bmscr.backImage.getHeight());
				g.fillRect(0, bmscr.backImage.getHeight() - oy, getWidth(), oy);
				bmscr.repaintLock.notifyAll();
			}
		}
	}
}
