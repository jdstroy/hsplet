package hsplet.variable;

/**
 * 変数の値を管理するオブジェクト。
 * <p>
 * 変数の値が変更され、最新の情報が必要になったときに呼び出される。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/02/11 17:19:35 $
 */
public interface VolatileValueUpdater {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	static final String fileVersionID = "$Id: VolatileValueUpdater.java,v 1.2 2006/02/11 17:19:35 Yuki Exp $";

	/**
	 * 引数に渡された変数の値を最新にする。
	 * 
	 * @param value 更新する変数。
	 */
	public void update(final Operand value);
}
