/*
 * $Id: StringArray.java,v 1.4.2.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.variable;

import hsplet.util.Conversion;

/**
 * 文字列の配列を表すクラス。
 * 
 * 
 * @author Yuki
 * @version $Revision: 1.4.2.1 $, $Date: 2006/08/02 12:13:06 $
 */
public final class StringArray extends Array {

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = 5025854224973749708L;

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: StringArray.java,v 1.4.2.1 2006/08/02 12:13:06 Yuki Exp $";

	/** オブジェクトの値。 */
	private ByteString[] values;

	/**
	 * 長さ 64、要素数 1 の配列を構築する。
	 */
	public StringArray() {

		this(64, 1, 0, 0, 0);
	}

	/**
	 * 初期長さと要素数を指定してオブジェクトを構築する。
	 * <p>
	 * オリジナル HSP と違い、要素数に 0 を指定することは出来ない。 つまり、配列は必ず四次元あることになる。 そのほうが要素数などの計算が簡単。
	 * </p>
	 * 
	 * @param length 初期状態での文字列バッファサイズ。
	 * @param l0 一次元目の要素数。
	 * @param l1 二次元目の要素数。
	 * @param l2 三次元目の要素数。
	 * @param l3 四次元目の要素数。
	 */
	public StringArray(final int length, final int l0, final int l1,
			final int l2, final int l3) {

		super(l0, l1, l2, l3);

		int size=l0;
		if(l1>0) {
			size *= l1;
			if(l2>0) {
				size*=l2;
				if(l3>0) {
					size*=l3;
				}
			}
		}

		values = new ByteString[size];
		for (int i = 0; i < values.length; ++i) {
			values[i] = new ByteString(new byte[length], 0, 0, false);
		}
	}

	//@Override
	public int getType() {

		return Type.STRING;
	}

	//@Override
	public String toStringRaw(final int index) {

		return values[index].toString();
	}

	//@Override
	public ByteString toByteStringRaw(final int index) {

		return values[index];
	}

	//@Override
	public int toIntRaw(final int index) {

		return Conversion.strtoi(values[index].toString());
	}

	//@Override
	public double toDoubleRaw(final int index) {

		return Conversion.strtod(values[index].toString());
	}

	//@Override
	public Operand dupRaw(int index) {
		return Scalar.fromValue(values[index]);
	}

	private static final ByteString oneString=new ByteString("1");
	//@Override
	public void incRaw(final int index) {

		values[index].append(oneString);
	}

	//@Override
	public void decRaw(final int index) {

		throw unsupportedOperator("-=");
	}

	public void assignRaw(final int index, final int newValue){
		values[index].assign(new ByteString(Integer.toString(newValue)));
	}
	public void assignRaw(final int index, final double newValue){
		values[index].assign(new ByteString(Double.toString(newValue)));
	}
	public void assignRaw(final int index, final String newValue){
		values[index].assign(new ByteString(newValue));
	}

	//@Override
	public void assignRaw(final int index, final Operand rhs, final int rhi) {

		values[index].assign(rhs.toByteStringRaw(rhi));
	}

	//@Override
	public void assignAddRaw(final int index, final Operand rhs, final int rhi) {

		values[index].append(rhs.toByteStringRaw(rhi));
	}

	//@Override
	public void assignSubRaw(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("-=");
	}

	//@Override
	public void assignMulRaw(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("*=");
	}

	//@Override
	public void assignDivRaw(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("/=");
	}

	//@Override
	public void assignModRaw(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("%=");
	}

	//@Override
	public void assignAndRaw(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("&=");
	}

	//@Override
	public void assignOrRaw(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("|=");
	}

	//@Override
	public void assignXorRaw(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("^=");
	}

	//@Override
	public void assignSrRaw(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator(">>=");
	}

	//@Override
	public void assignSlRaw(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("<<=");
	}

	//@Override
	public Operand addRaw(final int index, final Operand rhs, final int rhi) {

		return new StringScalar(ByteString.concat(values[index], rhs
				.toByteStringRaw(rhi)));
	}

	//@Override
	public Operand eqRaw(final int index, final Operand rhs, final int rhi) {

		return new IntScalar(values[index].equals(rhs.toByteStringRaw(rhi)) ? 1: 0);
	}

	//@Override
	public Operand neRaw(final int index, final Operand rhs, final int rhi) {

		return new IntScalar(values[index].compareTo(rhs.toByteStringRaw(rhi)));
	}

	//@Override
	public Operand gtRaw(final int index, final Operand rhs, final int rhi) {

		return new IntScalar(
				(values[index].compareTo(rhs.toByteStringRaw(rhi)) > 0) ? 1 : 0);
	}

	//@Override
	public Operand ltRaw(final int index, final Operand rhs, final int rhi) {

		return new IntScalar(
				(values[index].compareTo(rhs.toByteStringRaw(rhi)) < 0) ? 1 : 0);
	}

	//@Override
	public Operand geRaw(final int index, final Operand rhs, final int rhi) {

		return new IntScalar(
				(values[index].compareTo(rhs.toByteStringRaw(rhi)) >= 0) ? 1 : 0);
	}

	//@Override
	public Operand leRaw(final int index, final Operand rhs, final int rhi) {

		return new IntScalar(
				(values[index].compareTo(rhs.toByteStringRaw(rhi)) <= 0) ? 1 : 0);
	}

	//@Override
	public Operand subRaw(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("-");
	}

	//@Override
	public Operand mulRaw(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("*");
	}

	//@Override
	public Operand divRaw(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("/");
	}

	//@Override
	public Operand modRaw(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("%");
	}

	//@Override
	public Operand andRaw(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("&");
	}

	//@Override
	public Operand orRaw(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("|");
	}

	//@Override
	public Operand xorRaw(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("^");
	}

	//@Override
	public Operand slRaw(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("<<");
	}

	//@Override
	public Operand srRaw(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator(">>");
	}

	//@Override
	public byte peek(int index, int offset) {

		return values[index].get(offset);
	}

	//@Override
	public void poke(int index, int offset, byte value) {

		values[index].set(offset, value);
	}

    @Override
    public void assignNeRaw(int index, Operand rhs, int rhi) {
        throw unsupportedOperator("= !=");
    }

    @Override
    public void assignGtRaw(int index, Operand rhs, int rhi) {
        throw unsupportedOperator("= >");
    }

    @Override
    public void assignLtRaw(int index, Operand rhs, int rhi) {
        throw unsupportedOperator("= <");
    }

    @Override
    public void assignGtEqRaw(int index, Operand rhs, int rhi) {
        throw unsupportedOperator("= >=");
    }

    @Override
    public void assignLtEqRaw(int index, Operand rhs, int rhi) {
        throw unsupportedOperator("= <=");
    }

	//@Override
	public void expandToIndexes() {

		int size=l0;
		if(l1>0) {
			size *= l1;
			if(l2>0) {
				size*=l2;
				if(l3>0) {
					size*=l3;
				}
			}
		}

		final ByteString[] newValues = new ByteString[size];

		System.arraycopy(values, 0, newValues, 0, values.length);
		for (int i = values.length; i < newValues.length; ++i) {
			newValues[i] = new ByteString(new byte[256], 0, 0, false);
		}

		values = newValues;
	}
}
