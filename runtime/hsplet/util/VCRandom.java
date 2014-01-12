/*
 * $Id: VCRandom.java,v 1.1 2006/01/21 12:48:19 Yuki Exp $
 */
package hsplet.util;

/**
 * VC++と同じ計算式の擬似乱数。
 * 
 * @author Yuki
 * @version $Revision: 1.1 $, $Date: 2006/01/21 12:48:19 $
 */
public class VCRandom {

	private static final int A = 214013;

	private static final int C = 2531011;

	private static final int F = 0;

	private static final int S = 1;

	private long x = S;

	/**
	 * 次の乱数値を得る。
	 * @return 次の乱数値。
	 */
	public int rand() {
		x = x * A + C;
		return (int) (x >> 16) & 32767;
	}

	/**
	 * 乱数を初期化する。
	 * @param s シード。
	 */
	public void srand(int s) {
		x = s;
		if (F != 0) {
			rand();
		}
	}

}
