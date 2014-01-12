/*
 * $Id: Label.java,v 1.2 2006/01/13 20:32:12 Yuki Exp $
 */
package hsplet.variable;

/**
 * ラベルをあらわすオペランド。
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/01/13 20:32:12 $
 */
public class Label extends IntScalar {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: Label.java,v 1.2 2006/01/13 20:32:12 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = 6798195317537227773L;

	/**
	 * 値を指定してオブジェクトを構築する。
	 * 
	 * @param value 初期値。
	 */
	public Label(final int value) {

		super(value);
	}

	//@Override
	public int getType() {

		return Type.LABEL;
	}

}
