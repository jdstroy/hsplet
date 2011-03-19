/*
 * $Id: EndException.java,v 1.1 2006/01/09 12:07:12 Yuki Exp $
 */
package hsplet.function;

/**
 * end 命令が実行されたときに投げられる例外。
 * 
 * @author Yuki
 * @version $Revision: 1.1 $, $Date: 2006/01/09 12:07:12 $
 */
public class EndException extends RuntimeException {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: EndException.java,v 1.1 2006/01/09 12:07:12 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = -413786554606335070L;

}
