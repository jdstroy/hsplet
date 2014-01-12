/*
 * $Id: HSPScreen.java,v 1.4 2006/01/21 12:48:16 Yuki Exp $
 */
package hsplet.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.io.Serializable;

/**
 * HSPの画面をあらわすインターフェイス。
 * <p>
 * 共通の機能は ScreenInfo で実装しているので、このインターフェイスはそれほど多くの情報を提供しない。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.4 $, $Date: 2006/01/21 12:48:16 $
 */
public interface HSPScreen extends Serializable {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	static final String fileVersionID = "$Id: HSPScreen.java,v 1.4 2006/01/21 12:48:16 Yuki Exp $";

	/**
	 * コンポーネントとしてのインスタンスを取得する。
	 * @return コンポーネントとしてのこのインスタンス。
	 */
	public Component asComponent();

	/**
	 * 画面情報オブジェクトを取得する。
	 * @return  画面情報オブジェクト。
	 */
	public Bmscr getBmscr();

	/**
	 * 画面サイズを取得する。
	 * @return 画面サイズ。
	 */
	public Dimension getSize();

	/**
	 * タイトルを設定する。
	 * @param text 設定するテキスト。
	 */
	public void setTitle(final String text);
}
