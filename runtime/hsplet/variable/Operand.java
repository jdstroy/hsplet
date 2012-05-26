/*
 * $Id: Operand.java,v 1.2.4.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.variable;

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

	/**
	 * データの型を取得する。
	 * 
	 * @return データの型。
	 */
	public abstract int getType();

	/**
	 * 配列の一次元目の要素数を取得する。
	 * 
	 * @return 配列の一次元目の要素数。
	 */
	public abstract int l0();

	/**
	 * 配列の二次元目の要素数を取得する。
	 * 
	 * @return 配列の二次元目の要素数。
	 */
	public abstract int l1();

	/**
	 * 配列の三次元目の要素数を取得する。
	 * 
	 * @return 配列の三次元目の要素数。
	 */
	public abstract int l2();

	/**
	 * 配列の四次元目の要素数を取得する。
	 * 
	 * @return 配列の四次元目の要素数。
	 */
	public abstract int l3();

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
	public abstract int getIndex(final int i0, final int i1, final int i2,
			final int i3);

	/**
	 * このオブジェクトの文字列値を取得する。
	 * <p>
	 * このオブジェクトの型が文字列じゃないときは変換が行われる。
	 * </p>
	 * 
	 * @return オブジェクトの文字列値。
	 */
	//@Override
	public final String toString() {

		return toString(0);
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
	public abstract String toString(final int index);

	/**
	 * このオブジェクトの文字列値を取得する。
	 * <p>
	 * このオブジェクトの型が文字列じゃないときは変換が行われる。
	 * </p>
	 * 
	 * @param index 文字列値を取得する要素番号。
	 * @return オブジェクトの文字列値。
	 */
	public abstract ByteString toByteString(final int index);

	/**
	 * このオブジェクトの整数値を取得する。
	 * <p>
	 * このオブジェクトの型が整数じゃないときは変換が行われる。
	 * </p>
	 * 
	 * @param index 整数値を取得する要素番号。
	 * @return オブジェクトの整数値。
	 */
	public abstract int toInt(final int index);

	/**
	 * このオブジェクトの小数値を取得する。
	 * <p>
	 * このオブジェクトの型が小数じゃないときは変換が行われる。
	 * </p>
	 * 
	 * @param index 小数値を取得する要素番号。
	 * @return オブジェクトの小数値。
	 */
	public abstract double toDouble(final int index);

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
	public abstract Operand dup(final int index);

	/**
	 * オブジェクトの値をインクリメントする。
	 * 
	 * @param index インクリメントする要素番号。
	 */
	public abstract void inc(final int index);

	/**
	 * オブジェクトの値をデクリメントする。
	 * 
	 * @param index デクリメントする要素番号。
	 */
	public abstract void dec(final int index);

        /*
         * "assignNe", "assignGt", "assignLt", "assignGtEq", "assignLtEq",
         */
        /**
         * assignNe - assign Not Equal
         */
        public abstract void assignNe(final int index, final Operand rhs, final int rhi);
        /**
         * assignNe - assign Greater Than
         */
        public abstract void assignGt(final int index, final Operand rhs, final int rhi);
        /**
         * assignNe - assign Less Than
         */
        public abstract void assignLt(final int index, final Operand rhs, final int rhi);
        /**
         * assignNe - assign Greater Than or Equal
         */
        public abstract void assignGtEq(final int index, final Operand rhs, final int rhi);
        /**
         * assignNe - assign Less Than or Equal
         */
        public abstract void assignLtEq(final int index, final Operand rhs, final int rhi);
	/**
	 * 代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assign(final int index, final Operand rhs,
			final int rhi);

	/**
	 * 加算代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assignAdd(final int index, final Operand rhs,
			final int rhi);

	/**
	 * 減算代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assignSub(final int index, final Operand rhs,
			final int rhi);

	/**
	 * 乗算代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assignMul(final int index, final Operand rhs,
			final int rhi);

	/**
	 * 除算代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assignDiv(final int index, final Operand rhs,
			final int rhi);

	/**
	 * 剰余代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assignMod(final int index, final Operand rhs,
			final int rhi);

	/**
	 * ビット論理積代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assignAnd(final int index, final Operand rhs,
			final int rhi);

	/**
	 * ビット論理和代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assignOr(final int index, final Operand rhs,
			final int rhi);

	/**
	 * ビット排他論理和代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assignXor(final int index, final Operand rhs,
			final int rhi);

	/**
	 * 右ビットシフト代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assignSr(final int index, final Operand rhs,
			final int rhi);

	/**
	 * 左ビットシフト代入を行う。
	 * 
	 * @param index 代入先要素番号。
	 * @param rhs 代入元オブジェクト。
	 * @param rhi 代入元要素番号。
	 */
	public abstract void assignSl(final int index, final Operand rhs,
			final int rhi);

	/**
	 * 加算を行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand add(final int index, final Operand rhs,
			final int rhi);

	/**
	 * 減算を行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand sub(final int index, final Operand rhs,
			final int rhi);

	/**
	 * 乗算を行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand mul(final int index, final Operand rhs,
			final int rhi);

	/**
	 * 除算を行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand div(final int index, final Operand rhs,
			final int rhi);

	/**
	 * 剰余を求める。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand mod(final int index, final Operand rhs,
			final int rhi);

	/**
	 * ビット論理積を求める。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand and(final int index, final Operand rhs,
			final int rhi);

	/**
	 * ビット論理和を求める。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand or(final int index, final Operand rhs, final int rhi);

	/**
	 * ビット排他論理和を求める。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand xor(final int index, final Operand rhs,
			final int rhi);

	/**
	 * 右ビットシフトを行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand sl(final int index, final Operand rhs, final int rhi);

	/**
	 * 左ビットシフトを行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand sr(final int index, final Operand rhs, final int rhi);

	/**
	 * ==比較を行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand eq(final int index, final Operand rhs, final int rhi);

	/**
	 * !=比較を行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand ne(final int index, final Operand rhs, final int rhi);

	/**
	 * &gt;比較を行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand gt(final int index, final Operand rhs, final int rhi);

	/**
	 * &lt;比較を行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand lt(final int index, final Operand rhs, final int rhi);

	/**
	 * &gt;=比較を行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand ge(final int index, final Operand rhs, final int rhi);

	/**
	 * &lt;=比較を行う。
	 * 
	 * @param index 要素番号。
	 * @param rhs 右オペランド。
	 * @param rhi 右オペランドの要素番号。
	 * @return 演算結果。
	 */
	public abstract Operand le(final int index, final Operand rhs, final int rhi);

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
