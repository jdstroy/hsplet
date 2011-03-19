/*
 * $Id: JumpStatement.java,v 1.2 2006/01/13 20:32:09 Yuki Exp $
 */
package hsplet.function;

import java.io.Serializable;

/**
 * ジャンプステートメントをあらわす引数値。
 * <p>
 * button など、goto or gosub を追加で受け取るコマンドの引数に使用する値。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/01/13 20:32:09 $
 */
public final class JumpStatement implements Serializable {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: JumpStatement.java,v 1.2 2006/01/13 20:32:09 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = -4548306193459980074L;

	private int code;

	/**
	 * オブジェクトを構築する。
	 * @param code ジャンプの種類を表す定数。
	 */
	private JumpStatement(final int code) {
		this.code = code;
	}

	/** goto が指定された。 */
	public static final JumpStatement Goto = new JumpStatement(0);

	/** gosub が指定された。 */
	public static final JumpStatement Gosub = new JumpStatement(1);

	private Object readResolve() {
        return code==0 ? Goto : Gosub;
    }

}
