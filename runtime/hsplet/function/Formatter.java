/*
 * $Id: Formatter.java,v 1.3 2006/01/21 12:48:20 Yuki Exp $
 */
package hsplet.function;

import com.braju.format.Format;

/**
 * strf を実装するクラス。
 * <p>実際には hb16 に処理を丸投げする。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.3 $, $Date: 2006/01/21 12:48:20 $
 */
public class Formatter {

	/**
	 * オブジェクトを文字列化する。
	 * @param format 書式。
	 * @param param オブジェクト。
	 * @return 書式化された文字列。
	 */
	public static String format(final String format, final Object param) {
		return Format.sprintf(format, new Object[] { param });
	}
}
