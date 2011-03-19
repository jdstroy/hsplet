/*
 * $Id: ContentPane.java,v 1.9 2006/02/27 15:52:05 Yuki Exp $
 */
package hsplet.gui;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * HSPLet の各画面の「内容物」を現すオブジェクト。
 * <p>
 * コントロールをこの中に配置するほか、バックバッファを使用したダブルバッファリングもサポートする。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.9 $, $Date: 2006/02/27 15:52:05 $
 */
public class ContentPane extends JPanel {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: ContentPane.java,v 1.9 2006/02/27 15:52:05 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = 8376446656431845833L;

	/**
	 * オブジェクトを作成する。
	 * @param owner オーナーとなる画面。
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
