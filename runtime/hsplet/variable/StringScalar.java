/*
 * $Id: StringScalar.java,v 1.4.2.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.variable;

import hsplet.util.Conversion;

/**
 * 文字列を表すクラス。
 * 
 * 
 * @author Yuki
 * @version $Revision: 1.4.2.1 $, $Date: 2006/08/02 12:13:06 $
 */
public final class StringScalar extends Scalar {

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = 4664482061973809570L;

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: StringScalar.java,v 1.4.2.1 2006/08/02 12:13:06 Yuki Exp $";

	/** オブジェクトの値。 */
	public ByteString value;

	/**
	 * 値を指定してオブジェクトを構築する。
	 * 
	 * @param value 初期値。
	 */
	public StringScalar(final String value) {

		this(new ByteString(value));
	}

	/**
	 * 値を指定してオブジェクトを構築する。
	 * 
	 * @param value 初期値。
	 */
	public StringScalar(final ByteString value) {

		this.value = value;
	}

	//@Override
	public int getType() {

		return Type.STRING;
	}

	//@Override
	public String toString(final int index) {

		return value.toString();
	}

	//@Override
	public ByteString toByteString(final int index) {

		return value;
	}

	//@Override
	public int toInt(final int index) {

		return Conversion.strtoi(value.toString());
	}

	//@Override
	public double toDouble(final int index) {

		return Conversion.strtod(value.toString());
	}
	

	//@Override
	public Operand dup(int index) {
		return Scalar.fromValue(value);
	}

	//@Override
	public void inc(final int index) {

	}

	//@Override
	public void dec(final int index) {

	}

	public void assign(final int index, final int newValue){
		value.assign(new ByteString(Integer.toString(newValue)));
	}
	public void assign(final int index, final double newValue){
		value.assign(new ByteString(Double.toString(newValue)));
	}
	public void assign(final int index, final String newValue){
		value.assign(new ByteString(newValue));
	}

	//@Override
	public void assign(final int index, final Operand rhs, final int rhi) {

		value.assign(rhs.toByteString(rhi));
	}

	//@Override
	public void assignAdd(final int index, final Operand rhs, final int rhi) {

		value.append(rhs.toByteString(rhi));
	}

	//@Override
	public void assignSub(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("-=");
	}

	//@Override
	public void assignMul(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("*=");
	}

	//@Override
	public void assignDiv(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("/=");
	}

	//@Override
	public void assignMod(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("%=");
	}

	//@Override
	public void assignAnd(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("&=");
	}

	//@Override
	public void assignOr(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("|=");
	}

	//@Override
	public void assignXor(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("^=");
	}

	//@Override
	public void assignSr(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator(">>=");
	}

	//@Override
	public void assignSl(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("<<=");
	}

	//@Override
	public Operand add(final int index, final Operand rhs, final int rhi) {

		return new StringScalar(ByteString.concat(value, rhs.toByteString(rhi)));
	}

	//@Override
	public Operand eq(final int index, final Operand rhs, final int rhi) {

		return new IntScalar(value.equals(rhs.toByteString(rhi)) ? 1: 0);
	}

	//@Override
	public Operand ne(final int index, final Operand rhs, final int rhi) {

		return new IntScalar(value.compareTo(rhs.toByteString(rhi)));
	}

	//@Override
	public Operand gt(final int index, final Operand rhs, final int rhi) {

		return new IntScalar((value.compareTo(rhs.toByteString(rhi)) > 0) ? 1
				: 0);
	}

	//@Override
	public Operand lt(final int index, final Operand rhs, final int rhi) {

		return new IntScalar((value.compareTo(rhs.toByteString(rhi)) < 0) ? 1
				: 0);
	}

	//@Override
	public Operand ge(final int index, final Operand rhs, final int rhi) {

		return new IntScalar((value.compareTo(rhs.toByteString(rhi)) >= 0) ? 1
				: 0);
	}

	//@Override
	public Operand le(final int index, final Operand rhs, final int rhi) {

		return new IntScalar((value.compareTo(rhs.toByteString(rhi)) <= 0) ? 1
				: 0);
	}

	//@Override
	public Operand sub(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("-");
	}

	//@Override
	public Operand mul(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("*");
	}

	//@Override
	public Operand div(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("/");
	}

	//@Override
	public Operand mod(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("%");
	}

	//@Override
	public Operand and(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("&");
	}

	//@Override
	public Operand or(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("|");
	}

	//@Override
	public Operand xor(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("^");
	}

	//@Override
	public Operand sl(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator("<<");
	}

	//@Override
	public Operand sr(final int index, final Operand rhs, final int rhi) {

		throw unsupportedOperator(">>");
	}

	//@Override
	public byte peek(int index, int offset) {

		return value.get(offset);
	}

	//@Override
	public void poke(int index, int offset, byte value) {

		this.value.set(offset, value);

	}

    @Override
    public void assignNe(int index, Operand rhs, int rhi) {
        throw unsupportedOperator("= !=");
    }

    @Override
    public void assignGt(int index, Operand rhs, int rhi) {
        throw unsupportedOperator("= >=");
    }

    @Override
    public void assignLt(int index, Operand rhs, int rhi) {
        throw unsupportedOperator("= <=");
    }

    @Override
    public void assignGtEq(int index, Operand rhs, int rhi) {
        throw unsupportedOperator("= >=");
    }

    @Override
    public void assignLtEq(int index, Operand rhs, int rhi) {
        throw unsupportedOperator("= <=");
    }
}
