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
 * screen ウィンドウ。
 * 
 * @author Yuki
 * @version $Revision: 1.5 $, $Date: 2006/01/21 12:48:16 $
 */
public class Screen extends JFrame implements HSPScreen {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: Screen.java,v 1.5 2006/01/21 12:48:16 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = 8282094332529368460L;

	/**
	 * オブジェクトを構築する。
	 * @param bufferSize バッファサイズ。
	 * @param mode モード。
	 * @param location 左上座標。
	 * @param size 表示サイズ。
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
