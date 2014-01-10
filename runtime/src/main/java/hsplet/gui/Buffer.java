/*
 * $Id: Buffer.java,v 1.4 2006/01/21 12:48:16 Yuki Exp $
 */
package hsplet.gui;

import java.awt.Component;
import java.awt.Dimension;

/**
 * buffer 画面。
 * 
 * @author Yuki
 * @version $Revision: 1.4 $, $Date: 2006/01/21 12:48:16 $
 */
public class Buffer implements HSPScreen {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: Buffer.java,v 1.4 2006/01/21 12:48:16 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = 6515895350317408339L;

	private final Bmscr info;

	private final Dimension size;

	/**
	 * オブジェクトを構築する。
	 * @param size バッファサイズ。
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
