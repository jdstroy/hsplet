/*
 * $Id: Operand.java,v 1.2.4.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.variable;
import hsplet.Context;
import hsplet.HSPError;

import java.io.Serializable;

/**
 * HSP のオペランドをあらわす基底クラス。
 * <p>
 * HSP で使用する変数・定数・演算結果はすべてこのクラスを継承する。
 * </p>
 * オペランドは主に
 * <ul>
 * <li>配列のインデックスからオフセットを取得するメソッド。</li>
 * <li>Java のプリミティブや文字列に変換するメソッド。</li>
 * <li>代入・変更系演算子メソッド。</li>
 * <li>二項演算子メソッド。</li>
 * <li>データのバイトへの直接アクセスメソッド。</li>
 * </ul>
 * から成る。
 * <p>
 * ほとんどのメソッドが abstract なので、継承したクラスは適切にこれを実装する。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.2.4.1 $, $Date: 2006/08/02 12:13:06 $
 */
public abstract class Operand implements Serializable {

	/** Hackish way to have all Operands be able to refer to context.
	 *  This is initialized in Applet.init(Class) */
	public static Context context;

        protected void error(int error, String command) {
            getContext().error(error, command);
        }

        protected void error(int error, String command, String message) {
            getContext().error(error, command, message);
        }
        
        protected Context getContext() {
            return context;
        }
        
        protected static final Scalar UNITY = Scalar.fromValue(1);

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: Operand.java,v 1.2.4.1 2006/08/02 12:13:06 Yuki Exp $";
	protected static int allErrorIndex=1;
	protected int errorIndex=0;
	public int errorIndex(){
		if(errorIndex==0) errorIndex=allErrorIndex++;
		return errorIndex;
	}

	/**
	 * データの型。
	 * 
	 * @author Yuki
	 */
	public static final class Type {

		/** データの型、未使用。 */
		public static final int UNKNOWN = 0;

		/** データの型、ラベルを表す定数。 */
		public static final int LABEL = 1;

		/** データの型、文字列を表す定数。 */
		public static final int STRING = 2;

		/** データの型、小数を表す定数。 */
		public static final int DOUBLE = 3;

		/** データの型、整数を表す定数。 */
		public static final int INTEGER = 4;

		/** データの型、用途は不明。 */
		public static final int MODULE = 5;
	}
	public static int[] typeSizes = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 8, 4, Integer.MAX_VALUE};

	/**
	 * データの型を取得する。
	 * 
	 * @return データの型。
	 */
	public abstract int getType();
	
	public Operand checkVar() {
		context.error(HSPError.VariableNameNotSpecified, "", "Variable required.");
		return this;
	}

	/**
	 * 配列の一次元目の要素数を取得する。
	 * 
	 * @return 配列の一次元目の要素数。
	 */
	public abstract int l0();
	public abstract int checkSize0(int size);
	public abstract int checkResize0(int size);
	public abstract void checkIncrementSize(int size);

	/**
	 * 配列の二次元目の要素数を取得する。
	 * 
	 * @return 配列の二次元目の要素数。
	 */
	public abstract int l1();
	public abstract int checkSize1(int size);
	public abstract int checkResize1(int size);

	/**
	 * 配列の三次元目の要素数を取得する。
	 * 
	 * @return 配列の三次元目の要素数。
	 */
	public abstract int l2();
	public abstract int checkSize2(int size);
	public abstract int checkResize2(int size);

	/**
	 * 配列の四次元目の要素数を取得する。
	 * 
	 * @return 配列の四次元目の要素数。
	 */
	public abstract int l3();
	public abstract int checkSize3(int size);
	public abstract int checkResize3(int size);

	public abstract int getIndex(final int i0);
	public abstract int getResizeIndex(final int i0);
	/**
	 * 二次元配列の、一次元に換算した要素のインデックスを取得する。
	 * <p>
	 * HSPLet 3.0 では高速化のために配列はすべて一次元で管理される。 二次元以上のインデックスは要素数を元に一次元に換算してから処理される。
	 * </p>
	 * 
	 * @param i0 一次元目のインデックス。
	 * @param i1 二次元目のインデックス。
	 * @return 一次元に換算した要素番号。
	 */
	public abstract int getIndex(final int i0, final int i1);
	public abstract int getResizeIndex(final int i0, final int i1);

	/**
	 * 三次元配列の、一次元に換算した要素のインデックスを取得する。
	 * <p>
	 * HSPLet 3.0 では高速化のために配列はすべて一次元で管理される。 二次元以上のインデックスは要素数を元に一次元に換算してから処理される。
	 * </p>
	 * 
	 * @param i0 一次元目のインデックス。
	 * @param i1 二次元目のインデックス。
	 * @param i2 三次元目のインデックス。
	 * @return 一次元に換算した要素番号。
	 */
	public abstract int getIndex(final int i0, final int i1, final int i2);
	public abstract int getResizeIndex(final int i0, final int i1, final int i2);

	/**
	 * 四次元配列の、一次元に換算した要素のインデックスを取得する。
	 * <p>
	 * HSPLet 3.0 では高速化のために配列はすべて一次元で管理される。 二次元以上のインデックスは要素数を元に一次元に換算してから処理される。
	 * </p>
	 * 
	 * @param i0 一次元目のインデックス。
	 * @param i1 二次元目のインデックス。
	 * @param i2 三次元目のインデックス。
	 * @param i3 四次元目のインデックス。
	 * @return 一次元に換算した要素番号。
	 */
	public abstract int getIndex(final int i0, final int i1, final int i2, final int i3);
	public abstract int getResizeIndex(final int i0, final int i1, final int i2, final int i3);

	/**
	 * このオブジェクトの文字列値を取得する。
	 * <p>
	 * このオブジェクトの型が文字列じゃないときは変換が行われる。
	 * </p>
	 * 
	 * @return オブジェクトの文字列値。
	 */
	//@Override
	public String toString() {
		return toStringRaw(0);
	}

	/**
	 * このオブジェクトの文字列値を取得する。
	 * <p>
	 * このオブジェクトの型が文字列じゃないときは変換が行われる。
	 * </p>
	 * 
	 * @param index 文字列値を取得する要素番号。
	 * @return オブジェクトの文字列値。
	 */
	public abstract String toStringRaw(final int index);
	public String toString(final int i0) {
		return toStringRaw(getIndex(i0));
	}
	public String toString(final int i0, final int i1) {
		return toStringRaw(getIndex(i0, i1));
	}
	public String toString(final int i0, final int i1, final int i2) {
		return toStringRaw(getIndex(i0, i1, i2));
	}
	public String toString(final int i0, final int i1, final int i2, final int i3) {
		return toStringRaw(getIndex(i0, i1, i2, i3));
	}

	/**
	 * このオブジェクトの文字列値を取得する。
	 * <p>
	 * このオブジェクトの型が文字列じゃないときは変換が行われる。
	 * </p>
	 * 
	 * @param index 文字列値を取得する要素番号。
	 * @return オブジェクトの文字列値。
	 */
	public abstract ByteString toByteStringRaw(final int index);
	public ByteString toByteString() {
		return toByteStringRaw(0);
	}
	public ByteString toByteString(final int i0) {
		return toByteStringRaw(getIndex(i0));
	}
	public ByteString toByteString(final int i0, final int i1) {
		return toByteStringRaw(getIndex(i0, i1));
	}
	public ByteString toByteString(final int i0, final int i1, final int i2) {
		return toByteStringRaw(getIndex(i0, i1, i2));
	}
	public ByteString toByteString(final int i0, final int i1, final int i2, final int i3) {
		return toByteStringRaw(getIndex(i0, i1, i2, i3));
	}

	/**
	 * このオブジェクトの整数値を取得する。
	 * <p>
	 * このオブジェクトの型が整数じゃないときは変換が行われる。
	 * </p>
	 * 
	 * @param index 整数値を取得する要素番号。
	 * @return オブジェクトの整数値。
	 */
	public abstract int toIntRaw(final int index);
	public int toInt() {
		return toIntRaw(0);
	}
	public int toInt(final int i0) {
		return toIntRaw(getIndex(i0));
	}
	public int toInt(final int i0, final int i1) {
		return toIntRaw(getIndex(i0, i1));
	}
	public int toInt(final int i0, final int i1, final int i2) {
		return toIntRaw(getIndex(i0, i1, i2));
	}
	public int toInt(final int i0, final int i1, final int i2, final int i3) {
		return toIntRaw(getIndex(i0, i1, i2, i3));
	}

	/**
	 * このオブジェクトの小数値を取得する。
	 * <p>
	 * このオブジェクトの型が小数じゃないときは変換が行われる。
	 * </p>
	 * 
	 * @param index 小数値を取得する要素番号。
	 * @return オブジェクトの小数値。
	 */
	public abstract double toDoubleRaw(final int index);
	public double toDouble() {
		return toDoubleRaw(0);
	}
	public double toDouble(final int i0) {
		return toDoubleRaw(getIndex(i0));
	}
	public double toDouble(final int i0, final int i1) {
		return toDoubleRaw(getIndex(i0, i1));
	}
	public double toDouble(final int i0, final int i1, final int i2) {
		return toDoubleRaw(getIndex(i0, i1, i2));
	}
	public double toDouble(final int i0, final int i1, final int i2, final int i3) {
		return toDoubleRaw(getIndex(i0, i1, i2, i3));
	}

	/**
	 * オブジェクトへの参照を取得する。
	 * <p>
	 * このメソッドから返されるオブジェクトはこのオブジェクトの指定された要素を参照している。 参照オブジェクトの値を変更したり、このオブジェクトの値を変更するとお互いに反映される。
	 * </p>
	 * <p>
	 * 引数の値によってはこのオブジェクト自身が返される可能性もある。
	 * </p>
	 * 
	 * @param index 要素番号。
	 * @return このオブジェクトへの参照。
	 */
	public Operand ref(final int index) {

		if (index == 0) {
			return this;
		} else {

			return new Reference(this, index);
		}
	}
	
	/**
	 * オブジェクトの複製を取得する。
	 * @param index 複製されるようそのインデックス。
	 * @return このオブジェクトの指定された要素を保持するオブジェクト。
	 */
	public abstract Operand dupRaw(final int index);
	public Operand dup() {
		return dupRaw(0);
	}
	public Operand dup(final int i0) {
		return dupRaw(getIndex(i0));
	}
	public Operand dup(final int i0, final int i1) {
		return dupRaw(getIndex(i0, i1));
	}
	public Operand dup(final int i0, final int i1, final int i2) {
		return dupRaw(getIndex(i0, i1, i2));
	}
	public Operand dup(final int i0, final int i1, final int i2, final int i3) {
		return dupRaw(getIndex(i0, i1, i2, i3));
	}

	/**
	 * オブジェクトの値をインクリメントする。
	 * 
	 * @param index インクリメントする要素番号。
	 */
	public abstract void incRaw(final int index);
	public void inc() {
		incRaw(0);
	}
	public void inc(final int i0) {
		incRaw(getIndex(i0));
	}
	public void inc(final int i0, final int i1) {
		incRaw(getIndex(i0, i1));
	}
	public void inc(final int i0, final int i1, final int i2) {
		incRaw(getIndex(i0, i1, i2));
	}
	public void inc(final int i0, final int i1, final int i2, final int i3) {
		incRaw(getIndex(i0, i1, i2, i3));
	}

	/**
	 * オブジェクトの値をデクリメントする。
	 * 
	 * @param index デクリメントする要素番号。
	 */
	public abstract void decRaw(final int index);
	public void dec() {
		decRaw(0);
	}
	public void dec(final int i0) {
		decRaw(getIndex(i0));
	}
	public void dec(final int i0, final int i1) {
		decRaw(getIndex(i0, i1));
	}
	public void dec(final int i0, final int i1, final int i2) {
		decRaw(getIndex(i0, i1, i2));
	}
	public void dec(final int i0, final int i1, final int i2, final int i3) {
		decRaw(getIndex(i0, i1, i2, i3));
	}

    /*
     * "assignNe", "assignGt", "assignLt", "assignGtEq", "assignLtEq",
     */
    /**
     * assignNe - assign Not Equal
     */
    public abstract void assignNeRaw(final int index, final Operand rhs, final int rhi);
    public void assignNeRaw(final int index, final Operand rhs) {
	    assignNeRaw(index, rhs, 0);
    }
	public void assignNe(final Operand rhs) {
		assignNeRaw(0, rhs, 0);
	}
	public void assignNe(final Operand rhs, final int rhi) {
		assignNeRaw(0, rhs, rhi);
	}
	public void assignNe(final int i0, final Operand rhs, final int rhi) {
		assignNeRaw(getIndex(i0), rhs, rhi);
	}
	public void assignNe(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignNeRaw(getIndex(i0, i1), rhs, rhi);
	}
	public void assignNe(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignNeRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignNe(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignNeRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}
    /**
     * assignNe - assign Greater Than
     */
    public abstract void assignGtRaw(final int index, final Operand rhs, final int rhi);
    public void assignGtRaw(final int index, final Operand rhs) {
	    assignGtRaw(index, rhs, 0);
    }
	public void assignGt(final Operand rhs) {
		assignGtRaw(0, rhs, 0);
	}
	public void assignGt(final Operand rhs, final int rhi) {
		assignGtRaw(0, rhs, rhi);
	}
	public void assignGt(final int i0, final Operand rhs, final int rhi) {
		assignGtRaw(getIndex(i0), rhs, rhi);
	}
	public void assignGt(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignGtRaw(getIndex(i0, i1), rhs, rhi);
	}
	public void assignGt(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignGtRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignGt(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignGtRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}
    /**
     * assignNe - assign Less Than
     */
    public abstract void assignLtRaw(final int index, final Operand rhs, final int rhi);
    public void assignLtRaw(final int index, final Operand rhs) {
	    assignLtRaw(index, rhs, 0);
    }
	public void assignLt(final Operand rhs) {
		assignLtRaw(0, rhs, 0);
	}
	public void assignLt(final Operand rhs, final int rhi) {
		assignLtRaw(0, rhs, rhi);
	}
	public void assignLt(final int i0, final Operand rhs, final int rhi) {
		assignLtRaw(getIndex(i0), rhs, rhi);
	}
	public void assignLt(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignLtRaw(getIndex(i0, i1), rhs, rhi);
	}
	public void assignLt(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignLtRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignLt(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignLtRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}
    /**
     * assignNe - assign Greater Than or Equal
     */
    public abstract void assignGtEqRaw(final int index, final Operand rhs, final int rhi);
    public void assignGtEqRaw(final int index, final Operand rhs) {
	    assignGtEqRaw(index, rhs, 0);
    }
	public void assignGtEq(final Operand rhs) {
		assignGtEqRaw(0, rhs, 0);
	}
	public void assignGtEq(final Operand rhs, final int rhi) {
		assignGtEqRaw(0, rhs, rhi);
	}
	public void assignGtEq(final int i0, final Operand rhs, final int rhi) {
		assignGtEqRaw(getIndex(i0), rhs, rhi);
	}
	public void assignGtEq(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignGtEqRaw(getIndex(i0, i1), rhs, rhi);
	}
	public void assignGtEq(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignGtEqRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignGtEq(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignGtEqRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}
    /**
     * assignNe - assign Less Than or Equal
     */
    public abstract void assignLtEqRaw(final int index, final Operand rhs, final int rhi);
    public void assignLtEqRaw(final int index, final Operand rhs) {
	    assignLtEqRaw(index, rhs, 0);
    }
	public void assignLtEq(final Operand rhs) {
		assignLtEqRaw(0, rhs, 0);
	}
	public void assignLtEq(final Operand rhs, final int rhi) {
		assignLtEqRaw(0, rhs, rhi);
	}
	public void assignLtEq(final int i0, final Operand rhs, final int rhi) {
		assignLtEqRaw(getIndex(i0), rhs, rhi);
	}
	public void assignLtEq(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignLtEqRaw(getIndex(i0, i1), rhs, rhi);
	}
	public void assignLtEq(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignLtEqRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignLtEq(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignLtEqRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}
	/**
	 * 代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assignRaw(final int index, final int newValue);
	public void assign(final int newValue) {
		assignRaw(0, newValue);
	}
	public void assign(final int i0, final int newValue) {
		assignRaw(getResizeIndex(i0), newValue);
	}
	public void assign(final int i0, final int i1, final int newValue) {
		assignRaw(getResizeIndex(i0, i1), newValue);
	}
	public void assign(final int i0, final int i1, final int i2, final int newValue) {
		assignRaw(getResizeIndex(i0, i1, i2), newValue);
	}
	public void assign(final int i0, final int i1, final int i2, final int i3, final int newValue) {
		assignRaw(getResizeIndex(i0, i1, i2, i3), newValue);
	}
	public abstract void assignRaw(final int index, final double newValue);
	public void assign(final double newValue) {
		assignRaw(0, newValue);
	}
	public void assign(final int i0, final double newValue) {
		assignRaw(getResizeIndex(i0), newValue);
	}
	public void assign(final int i0, final int i1, final double newValue) {
		assignRaw(getResizeIndex(i0, i1), newValue);
	}
	public void assign(final int i0, final int i1, final int i2, final double newValue) {
		assignRaw(getResizeIndex(i0, i1, i2), newValue);
	}
	public void assign(final int i0, final int i1, final int i2, final int i3, final double newValue) {
		assignRaw(getResizeIndex(i0, i1, i2, i3), newValue);
	}
	public abstract void assignRaw(final int index, final String newValue);
	public void assign(final String newValue) {
		assignRaw(0, newValue);
	}
	public void assign(final int i0, final String newValue) {
		assignRaw(getResizeIndex(i0), newValue);
	}
	public void assign(final int i0, final int i1, final String newValue) {
		assignRaw(getResizeIndex(i0, i1), newValue);
	}
	public void assign(final int i0, final int i1, final int i2, final String newValue) {
		assignRaw(getResizeIndex(i0, i1, i2), newValue);
	}
	public void assign(final int i0, final int i1, final int i2, final int i3, final String newValue) {
		assignRaw(getResizeIndex(i0, i1, i2, i3), newValue);
	}

	public abstract void assignRaw(final int index, final Operand rhs, final int rhi);
	public void assignRaw(final int index, final Operand rhs) {
		assignRaw(index, rhs, 0);
	}
	public void assign(final Operand rhs) {
		assignRaw(0, rhs, 0);
	}
	public void assign(final Operand rhs, final int rhi) {
		assignRaw(0, rhs, rhi);
	}
	public void assign(final int i0, final Operand rhs, final int rhi) {
		assignRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assign(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assign(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assign(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * 加算代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assignAddRaw(final int index, final Operand rhs, final int rhi);
    public void assignAddRaw(final int index, final Operand rhs) {
	    assignAddRaw(index, rhs, 0);
    }
	public void assignAdd(final Operand rhs) {
		assignAddRaw(0, rhs, 0);
	}
	public void assignAdd(final Operand rhs, final int rhi) {
		assignAddRaw(0, rhs, rhi);
	}
	public void assignAdd(final int i0, final Operand rhs, final int rhi) {
		assignAddRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assignAdd(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignAddRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assignAdd(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignAddRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignAdd(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignAddRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * 減算代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assignSubRaw(final int index, final Operand rhs, final int rhi);
    public void assignSubRaw(final int index, final Operand rhs) {
	    assignSubRaw(index, rhs, 0);
    }
	public void assignSub(final Operand rhs) {
		assignSubRaw(0, rhs, 0);
	}
	public void assignSub(final Operand rhs, final int rhi) {
		assignSubRaw(0, rhs, rhi);
	}
	public void assignSub(final int i0, final Operand rhs, final int rhi) {
		assignSubRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assignSub(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignSubRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assignSub(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignSubRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignSub(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignSubRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * 乗算代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assignMulRaw(final int index, final Operand rhs, final int rhi);
    public void assignMulRaw(final int index, final Operand rhs) {
	    assignMulRaw(index, rhs, 0);
    }
	public void assignMul(final Operand rhs) {
		assignMulRaw(0, rhs, 0);
	}
	public void assignMul(final Operand rhs, final int rhi) {
		assignMulRaw(0, rhs, rhi);
	}
	public void assignMul(final int i0, final Operand rhs, final int rhi) {
		assignMulRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assignMul(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignMulRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assignMul(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignMulRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignMul(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignMulRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * 除算代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assignDivRaw(final int index, final Operand rhs, final int rhi);
    public void assignDivRaw(final int index, final Operand rhs) {
	    assignDivRaw(index, rhs, 0);
    }
	public void assignDiv(final Operand rhs) {
		assignDivRaw(0, rhs, 0);
	}
	public void assignDiv(final Operand rhs, final int rhi) {
		assignDivRaw(0, rhs, rhi);
	}
	public void assignDiv(final int i0, final Operand rhs, final int rhi) {
		assignDivRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assignDiv(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignDivRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assignDiv(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignDivRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignDiv(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignDivRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * 剰余代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assignModRaw(final int index, final Operand rhs, final int rhi);
    public void assignModRaw(final int index, final Operand rhs) {
	    assignModRaw(index, rhs, 0);
    }
	public void assignMod(final Operand rhs) {
		assignModRaw(0, rhs, 0);
	}
	public void assignMod(final Operand rhs, final int rhi) {
		assignModRaw(0, rhs, rhi);
	}
	public void assignMod(final int i0, final Operand rhs, final int rhi) {
		assignModRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assignMod(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignModRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assignMod(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignModRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignMod(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignModRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * ビット論理積代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assignAndRaw(final int index, final Operand rhs, final int rhi);
    public void assignAndRaw(final int index, final Operand rhs) {
	    assignAndRaw(index, rhs, 0);
    }
	public void assignAnd(final Operand rhs) {
		assignAndRaw(0, rhs, 0);
	}
	public void assignAnd(final Operand rhs, final int rhi) {
		assignAndRaw(0, rhs, rhi);
	}
	public void assignAnd(final int i0, final Operand rhs, final int rhi) {
		assignAndRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assignAnd(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignAndRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assignAnd(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignAndRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignAnd(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignAndRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * ビット論理和代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assignOrRaw(final int index, final Operand rhs, final int rhi);
    public void assignOrRaw(final int index, final Operand rhs) {
	    assignOrRaw(index, rhs, 0);
    }
	public void assignOr(final Operand rhs) {
		assignOrRaw(0, rhs, 0);
	}
	public void assignOr(final Operand rhs, final int rhi) {
		assignOrRaw(0, rhs, rhi);
	}
	public void assignOr(final int i0, final Operand rhs, final int rhi) {
		assignOrRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assignOr(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignOrRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assignOr(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignOrRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignOr(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignOrRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * ビット排他論理和代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assignXorRaw(final int index, final Operand rhs, final int rhi);
    public void assignXorRaw(final int index, final Operand rhs) {
	    assignXorRaw(index, rhs, 0);
    }
	public void assignXor(final Operand rhs) {
		assignXorRaw(0, rhs, 0);
	}
	public void assignXor(final Operand rhs, final int rhi) {
		assignXorRaw(0, rhs, rhi);
	}
	public void assignXor(final int i0, final Operand rhs, final int rhi) {
		assignXorRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assignXor(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignXorRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assignXor(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignXorRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignXor(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignXorRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * 右ビットシフト代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assignSrRaw(final int index, final Operand rhs, final int rhi);
    public void assignSrRaw(final int index, final Operand rhs) {
	    assignSrRaw(index, rhs, 0);
    }
	public void assignSr(final Operand rhs) {
		assignSrRaw(0, rhs, 0);
	}
	public void assignSr(final Operand rhs, final int rhi) {
		assignSrRaw(0, rhs, rhi);
	}
	public void assignSr(final int i0, final Operand rhs, final int rhi) {
		assignSrRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assignSr(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignSrRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assignSr(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignSrRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignSr(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignSrRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * 左ビットシフト代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assignSlRaw(final int index, final Operand rhs, final int rhi);
    public void assignSlRaw(final int index, final Operand rhs) {
	    assignSlRaw(index, rhs, 0);
    }
	public void assignSl(final Operand rhs) {
		assignSlRaw(0, rhs, 0);
	}
	public void assignSl(final Operand rhs, final int rhi) {
		assignSlRaw(0, rhs, rhi);
	}
	public void assignSl(final int i0, final Operand rhs, final int rhi) {
		assignSlRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assignSl(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignSlRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assignSl(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignSlRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignSl(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignSlRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * 加算を行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand addRaw(final int index, final Operand rhs, final int rhi);
	public Operand add(final Operand rhs) {
		return addRaw(0, rhs, 0);
	}
	public Operand add(final Operand rhs, final int rhi) {
		return addRaw(0, rhs, rhi);
	}
	public Operand add(final int i0, final Operand rhs, final int rhi) {
		return addRaw(getIndex(i0), rhs, rhi);
	}
	public Operand add(final int i0, final int i1, final Operand rhs, final int rhi) {
		return addRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand add(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return addRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand add(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return addRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * 減算を行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand subRaw(final int index, final Operand rhs, final int rhi);
	public Operand sub(final Operand rhs) {
		return subRaw(0, rhs, 0);
	}
	public Operand sub(final Operand rhs, final int rhi) {
		return subRaw(0, rhs, rhi);
	}
	public Operand sub(final int i0, final Operand rhs, final int rhi) {
		return subRaw(getIndex(i0), rhs, rhi);
	}
	public Operand sub(final int i0, final int i1, final Operand rhs, final int rhi) {
		return subRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand sub(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return subRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand sub(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return subRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * 乗算を行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand mulRaw(final int index, final Operand rhs, final int rhi);
	public Operand mul(final Operand rhs) {
		return mulRaw(0, rhs, 0);
	}
	public Operand mul(final Operand rhs, final int rhi) {
		return mulRaw(0, rhs, rhi);
	}
	public Operand mul(final int i0, final Operand rhs, final int rhi) {
		return mulRaw(getIndex(i0), rhs, rhi);
	}
	public Operand mul(final int i0, final int i1, final Operand rhs, final int rhi) {
		return mulRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand mul(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return mulRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand mul(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return mulRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * 除算を行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand divRaw(final int index, final Operand rhs, final int rhi);
	public Operand div(final Operand rhs) {
		return divRaw(0, rhs, 0);
	}
	public Operand div(final Operand rhs, final int rhi) {
		return divRaw(0, rhs, rhi);
	}
	public Operand div(final int i0, final Operand rhs, final int rhi) {
		return divRaw(getIndex(i0), rhs, rhi);
	}
	public Operand div(final int i0, final int i1, final Operand rhs, final int rhi) {
		return divRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand div(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return divRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand div(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return divRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * 剰余を求める。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand modRaw(final int index, final Operand rhs, final int rhi);
	public Operand mod(final Operand rhs) {
		return modRaw(0, rhs, 0);
	}
	public Operand mod(final Operand rhs, final int rhi) {
		return modRaw(0, rhs, rhi);
	}
	public Operand mod(final int i0, final Operand rhs, final int rhi) {
		return modRaw(getIndex(i0), rhs, rhi);
	}
	public Operand mod(final int i0, final int i1, final Operand rhs, final int rhi) {
		return modRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand mod(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return modRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand mod(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return modRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * ビット論理積を求める。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand andRaw(final int index, final Operand rhs, final int rhi);
	public Operand and(final Operand rhs) {
		return andRaw(0, rhs, 0);
	}
	public Operand and(final Operand rhs, final int rhi) {
		return andRaw(0, rhs, rhi);
	}
	public Operand and(final int i0, final Operand rhs, final int rhi) {
		return andRaw(getIndex(i0), rhs, rhi);
	}
	public Operand and(final int i0, final int i1, final Operand rhs, final int rhi) {
		return andRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand and(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return andRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand and(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return andRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * ビット論理和を求める。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand orRaw(final int index, final Operand rhs, final int rhi);
	public Operand or(final Operand rhs) {
		return orRaw(0, rhs, 0);
	}
	public Operand or(final Operand rhs, final int rhi) {
		return orRaw(0, rhs, rhi);
	}
	public Operand or(final int i0, final Operand rhs, final int rhi) {
		return orRaw(getIndex(i0), rhs, rhi);
	}
	public Operand or(final int i0, final int i1, final Operand rhs, final int rhi) {
		return orRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand or(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return orRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand or(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return orRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * ビット排他論理和を求める。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand xorRaw(final int index, final Operand rhs, final int rhi);
	public Operand xor(final Operand rhs) {
		return xorRaw(0, rhs, 0);
	}
	public Operand xor(final Operand rhs, final int rhi) {
		return xorRaw(0, rhs, rhi);
	}
	public Operand xor(final int i0, final Operand rhs, final int rhi) {
		return xorRaw(getIndex(i0), rhs, rhi);
	}
	public Operand xor(final int i0, final int i1, final Operand rhs, final int rhi) {
		return xorRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand xor(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return xorRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand xor(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return xorRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * 右ビットシフトを行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand slRaw(final int index, final Operand rhs, final int rhi);
	public Operand sl(final Operand rhs) {
		return slRaw(0, rhs, 0);
	}
	public Operand sl(final Operand rhs, final int rhi) {
		return slRaw(0, rhs, rhi);
	}
	public Operand sl(final int i0, final Operand rhs, final int rhi) {
		return slRaw(getIndex(i0), rhs, rhi);
	}
	public Operand sl(final int i0, final int i1, final Operand rhs, final int rhi) {
		return slRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand sl(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return slRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand sl(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return slRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * 左ビットシフトを行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand srRaw(final int index, final Operand rhs, final int rhi);
	public Operand sr(final Operand rhs) {
		return srRaw(0, rhs, 0);
	}
	public Operand sr(final Operand rhs, final int rhi) {
		return srRaw(0, rhs, rhi);
	}
	public Operand sr(final int i0, final Operand rhs, final int rhi) {
		return srRaw(getIndex(i0), rhs, rhi);
	}
	public Operand sr(final int i0, final int i1, final Operand rhs, final int rhi) {
		return srRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand sr(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return srRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand sr(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return srRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * ==比較を行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand eqRaw(final int index, final Operand rhs, final int rhi);
	public Operand eq(final Operand rhs) {
		return eqRaw(0, rhs, 0);
	}
	public Operand eq(final Operand rhs, final int rhi) {
		return eqRaw(0, rhs, rhi);
	}
	public Operand eq(final int i0, final Operand rhs, final int rhi) {
		return eqRaw(getIndex(i0), rhs, rhi);
	}
	public Operand eq(final int i0, final int i1, final Operand rhs, final int rhi) {
		return eqRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand eq(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return eqRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand eq(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return eqRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * !=比較を行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand neRaw(final int index, final Operand rhs, final int rhi);
	public Operand ne(final Operand rhs) {
		return neRaw(0, rhs, 0);
	}
	public Operand ne(final Operand rhs, final int rhi) {
		return neRaw(0, rhs, rhi);
	}
	public Operand ne(final int i0, final Operand rhs, final int rhi) {
		return neRaw(getIndex(i0), rhs, rhi);
	}
	public Operand ne(final int i0, final int i1, final Operand rhs, final int rhi) {
		return neRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand ne(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return neRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand ne(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return neRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * &gt;比較を行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand gtRaw(final int index, final Operand rhs, final int rhi);
	public Operand gt(final Operand rhs) {
		return gtRaw(0, rhs, 0);
	}
	public Operand gt(final Operand rhs, final int rhi) {
		return gtRaw(0, rhs, rhi);
	}
	public Operand gt(final int i0, final Operand rhs, final int rhi) {
		return gtRaw(getIndex(i0), rhs, rhi);
	}
	public Operand gt(final int i0, final int i1, final Operand rhs, final int rhi) {
		return gtRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand gt(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return gtRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand gt(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return gtRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * &lt;比較を行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand ltRaw(final int index, final Operand rhs, final int rhi);
	public Operand lt(final Operand rhs) {
		return ltRaw(0, rhs, 0);
	}
	public Operand lt(final Operand rhs, final int rhi) {
		return ltRaw(0, rhs, rhi);
	}
	public Operand lt(final int i0, final Operand rhs, final int rhi) {
		return ltRaw(getIndex(i0), rhs, rhi);
	}
	public Operand lt(final int i0, final int i1, final Operand rhs, final int rhi) {
		return ltRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand lt(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return ltRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand lt(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return ltRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * &gt;=比較を行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand geRaw(final int index, final Operand rhs, final int rhi);
	public Operand ge(final Operand rhs) {
		return geRaw(0, rhs, 0);
	}
	public Operand ge(final Operand rhs, final int rhi) {
		return geRaw(0, rhs, rhi);
	}
	public Operand ge(final int i0, final Operand rhs, final int rhi) {
		return geRaw(getIndex(i0), rhs, rhi);
	}
	public Operand ge(final int i0, final int i1, final Operand rhs, final int rhi) {
		return geRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand ge(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return geRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand ge(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return geRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * &lt;=比較を行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand leRaw(final int index, final Operand rhs, final int rhi);
	public Operand le(final Operand rhs) {
		return leRaw(0, rhs, 0);
	}
	public Operand le(final Operand rhs, final int rhi) {
		return leRaw(0, rhs, rhi);
	}
	public Operand le(final int i0, final Operand rhs, final int rhi) {
		return leRaw(getIndex(i0), rhs, rhi);
	}
	public Operand le(final int i0, final int i1, final Operand rhs, final int rhi) {
		return leRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand le(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return leRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand le(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return leRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * オブジェクトのバイトを取得する。
	 * 
	 * @param index 要素番号。
	 * @param offset オフセット。
	 * @return オブジェクトのバイト値。
	 */
	public abstract byte peek(int index, int offset);

	/**
	 * オブジェクトのバイトを設定する。
	 * 
	 * @param index 要素番号。
	 * @param offset オフセット。
	 * @param value 設定する値。
	 */
	public abstract void poke(int index, int offset, byte value);

	/*
	 * TODO: Type-clipping.
	 * New method needed for every assign-operand group (int index, int type, Operand O)
	 * Call something like below on the Operand? More complicated than that though, math largely depends on type.
	 */
	/*
	public Operand clipToType(int vartype) {
		int myType=getType();
		done:
		if(vartype!=myType)
		{
			int clipTo=typeSizes[vartype];
			int mySize=typeSizes[myType];
			if(mySize <= clipTo) break done;
			
			switch(myType)
			{
				//TODO
			}
		}
		return this;
		
	}
	*/

	/**
	 * サポートされない演算子が使用されたときに例外を発生させる。
	 * @param operator 演算子。
	 * @return UnsupportedOperationException 投げるべき例外。
	 */
	public UnsupportedOperationException unsupportedOperator(
			final String operator) {

		return new UnsupportedOperationException("演算子 " + operator + " は型 "
				+ getType() + " には使用できません。");
	}
}
