/*
 * $Id: HSPControl.java,v 1.2 2006/01/13 05:20:51 Yuki Exp $
 */
package hsplet.gui;

import hsplet.variable.Operand;

import java.awt.Component;
import java.io.Serializable;

/**
 * HSPのコントロールをあらわすインターフェイス。
 * 
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/01/13 05:20:51 $
 */
public interface HSPControl extends Serializable {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	static final String fileVersionID = "$Id: HSPControl.java,v 1.2 2006/01/13 05:20:51 Yuki Exp $";

	/**
	 * コンポーネントとしてのインスタンスを取得する。
	 * @return コンポーネントとしてのこのインスタンス。
	 */
	public Component asComponent();

	/**
	 * コントロールに値を設定する。
	 * <p>objprm で使用される。</p>
	 * @param v 設定する変数。
	 * @param vi 設定する変数のインデックス。
	 */
	public void setValue(final Operand v, final int vi);

}
