/*
 * $Id: Scalar.java,v 1.3 2006/02/11 18:00:37 Yuki Exp $
 */
package hsplet.variable;

/**
 * 単一の要素だけを持つオペランドをあらわす基本クラス。
 * <p>
 * Array が複数の要素を持つのに対して、このクラスは一つだけ要素を持つ。
 * </p>
 * <p>
 * 定数や演算の結果、関数の戻り値など、はじめから要素が一つしかないとわかっているときは、配列よりこちらを使用したほうが高速に実行できる。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.3 $, $Date: 2006/02/11 18:00:37 $
 */
public abstract class Scalar extends Operand {
    
	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: Scalar.java,v 1.3 2006/02/11 18:00:37 Yuki Exp $";

	private static final Scalar[] intCache = new Scalar[256];

	static {

		for (int i = 0; i < intCache.length; ++i) {
			intCache[i] = new IntScalar(i - 128);
		}
	}

        public static final Scalar UNITY = Scalar.fromValue(1);
	/**
	 * 特定の値をあらわすオブジェクトを取得する。
	 * <p>
	 * このメソッドから返されたオブジェクトの値は変更してはならない。
	 * </p>
	 * 
	 * @param rawValue 元の値。
	 * @return 指定された値を表すスカラーオブジェクト。
	 */
	public static Scalar fromValue(final String rawValue) {

		return new StringScalar(rawValue);
	}

	/**
	 * 特定の値をあらわすオブジェクトを取得する。
	 * <p>
	 * このメソッドから返されたオブジェクトの値は変更してはならない。
	 * </p>
	 * 
	 * @param rawValue 元の値。
	 * @return 指定された値を表すスカラーオブジェクト。
	 */
	public static Scalar fromValue(final ByteString rawValue) {

		return new StringScalar(rawValue);
	}

	/**
	 * 特定の値をあらわすオブジェクトを取得する。
	 * <p>
	 * このメソッドから返されたオブジェクトの値は変更してはならない。
	 * </p>
	 * 
	 * @param rawValue 元の値。
	 * @return 指定された値を表すスカラーオブジェクト。
	 */
	public static Scalar fromValue(final double rawValue) {

		return new DoubleScalar(rawValue);
	}

	/**
	 * 特定の値をあらわすオブジェクトを取得する。
	 * <p>
	 * このメソッドから返されたオブジェクトの値は変更してはならない。
	 * </p>
	 * 
	 * @param rawValue 元の値。
	 * @return 指定された値を表すスカラーオブジェクト。
	 */
	public static Scalar fromValue(final int rawValue) {

		if (rawValue >= -128 && rawValue <= 127) {
			return intCache[rawValue + 128];
		} else {
			return new IntScalar(rawValue);
		}
	}

	/**
	 * 特定の値をあらわすオブジェクトを取得する。
	 * <p>
	 * このメソッドから返されたオブジェクトの値は変更してはならない。
	 * </p>
	 * 
	 * @param rawValue 元の値。
	 * @return 指定された値を表すスカラーオブジェクト。
	 */
	public static Scalar fromLabel(final int rawValue) {

		return new Label(rawValue);
	}

	//Everything here's impossible to call. Implementing anyways.
	public void checkIncrementSize(int size) {
	}
	public int checkSize0(int size) {
		error(7, "", "Array overflow");
		return size;
	}
	public int checkSize1(int size) {
		error(7, "", "Array overflow");
		return size;
	}
	public int checkSize2(int size) {
		error(7, "", "Array overflow");
		return size;
	}
	public int checkSize3(int size) {
		error(7, "", "Array overflow");
		return size;
	}
	public int checkResize0(int size) {
		error(7, "", "Array overflow");
		return size;
	}
	public int checkResize1(int size) {
		error(7, "", "Array overflow");
		return size;
	}
	public int checkResize2(int size) {
		error(7, "", "Array overflow");
		return size;
	}
	public int checkResize3(int size) {
		error(7, "", "Array overflow");
		return size;
	}
	public int getIndex(final int i0) {
		if(i0 != 0)
			error(7, "", "Array overflow");
		return 0;
	}
	public int getIndex(final int i0, final int i1) {
		if(i0 != 0 || i1 != 0)
			error(7, "", "Array overflow");
		return 0;
	}
	public int getIndex(final int i0, final int i1, final int i2) {
		if(i0 != 0 || i1 != 0 || i2 != 0)
			error(7, "", "Array overflow");
		return 0;
	}
	public int getIndex(final int i0, final int i1, final int i2, final int i3) {
		if(i0 != 0 || i1 != 0 || i2 != 0 || i3 != 0)
			error(7, "", "Array overflow");
		return 0;
	}
	public int getResizeIndex(final int i0) {
		if(i0 != 0)
			error(7, "", "Array overflow");
		return 0;
	}
	public int getResizeIndex(final int i0, final int i1) {
		if(i0 != 0 || i1 != 0)
			error(7, "", "Array overflow");
		return 0;
	}
	public int getResizeIndex(final int i0, final int i1, final int i2) {
		if(i0 != 0 || i1 != 0 || i2 != 0)
			error(7, "", "Array overflow");
		return 0;
	}
	public int getResizeIndex(final int i0, final int i1, final int i2, final int i3) {
		if(i0 != 0 || i1 != 0 || i2 != 0 || i3 != 0)
			error(7, "", "Array overflow");
		return 0;
	}

	//@Override
	public int l0() {
		return 1;
	}

	//@Override
	public int l1() {
		return 0;
	}

	//@Override
	public int l2() {
		return 0;
	}

	//@Override
	public int l3() {
		return 0;
	}
}