/*
 * $Id: Conversion.java,v 1.3 2006/05/20 06:12:07 Yuki Exp $
 */
package hsplet.util;

/**
 * 型変換を提供するユーティリティクラス。
 * 
 * @author Yuki
 * @version $Revision: 1.3 $, $Date: 2006/05/20 06:12:07 $
 */
public class Conversion {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: Conversion.java,v 1.3 2006/05/20 06:12:07 Yuki Exp $";

	/**
	 * 文字列を可能な限り数字に変換する。
	 * <p>
	 * {@link java.lang.Integer#parseInt(java.lang.String)} と違い、このメソッドは例外を投げない。
	 * </p>
	 * 
	 * @param text 変換元文字列。
	 * @return 変換後の数字。
	 */
	public static int strtoi(final String text) {

		if (!text.trim().equals(text)) {
			return strtoi(text.trim());
		}

		if (text.length() == 0) {
			return 0;
		}

		// 変換可能なもっとも長い長さで変換する

		int result = 0;

		for (int i = 0; i < text.length(); ++i) {
			if (i == 0 && (text.charAt(i) == '-' || text.charAt(i) == '+')) {
				continue;
			}
			try {
				result = Integer.parseInt(text.substring(0, i+1));
			} catch (NumberFormatException e) {
				break;
			}
		}

		return result;
	}

	/**
	 * 文字列を可能な限り数字に変換する。
	 * <p>
	 * {@link java.lang.Double#parseDouble(java.lang.String)} と違い、このメソッドは例外を投げない。
	 * </p>
	 * 
	 * @param text 変換元文字列。
	 * @return 変換後の数字。
	 */
	public static double strtod(final String text) {

		if (!text.trim().equals(text)) {
			return strtod(text.trim());
		}

		if (text.length() == 0) {
			return 0.0;
		}

		// 変換可能なもっとも長い長さで変換する

		double result = 0;

		for (int i = 0; i < text.length(); ++i) {
			if (i == 0 && (text.charAt(i) == '-' || text.charAt(i) == '+')) {
				continue;
			}
			try {
				result = Double.parseDouble(text.substring(0, i+1));
			} catch (NumberFormatException e) {
				break;
			}
		}

		return result;
	}

}
