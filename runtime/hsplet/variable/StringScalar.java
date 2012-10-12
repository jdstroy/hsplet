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
	public String toStringRaw(final int index) {

		return value.toString();
	}

	//@Override
	public ByteString toByteStringRaw(final int index) {

		return value;
	}

	//@Override
	public int toIntRaw(final int index) {

		return Conversion.strtoi(value.toString());
	}

	//@Override
	public double toDoubleRaw(final int index) {

		return Conversion.strtod(value.toString());
	}
	

	//@Override
	public Operand dupRaw(int index) {
		return Scalar.fromValue(value);
	}

	//@Override
	public void incRaw(final int index) {
		value.append(new ByteString("1"));
	}

	//@Override
	public void decRaw(final int index) {

		throw unsupportedOperator("-=");
	}

	public void assignRaw(final int index, final int newValue){
		value.assign(new ByteString(Integer.toString(newValue)));
	}
	public void assignRaw(final int index, final double newValue){
		value.assign(new ByteString(Double.toString(newValue)));
	}
	public void assignRaw(final int index, final String newValue){
		value.assign(new ByteString(newValue));
	}

	//@Override
	public void assignRaw(final int index, final Operand rhs, final int rhi) {

		value.assign(rhs.toByteStringRaw(rhi));
	}

	//@Override
	public void assignAddRaw(final int index, final Operand rhs, final int rhi) {

		value.append(rhs.toByteStringRaw(rhi));
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

		return new StringScalar(ByteString.concat(value, rhs.toByteStringRaw(rhi)));
	}

	//@Override
	public Operand eqRaw(final int index, final Operand rhs, final int rhi) {

		return new IntScalar(value.equals(rhs.toByteStringRaw(rhi)) ? 1: 0);
	}

	//@Override
	public Operand neRaw(final int index, final Operand rhs, final int rhi) {

		return new IntScalar(value.compareTo(rhs.toByteStringRaw(rhi)));
	}

	//@Override
	public Operand gtRaw(final int index, final Operand rhs, final int rhi) {

		return new IntScalar((value.compareTo(rhs.toByteStringRaw(rhi)) > 0) ? 1
				: 0);
	}

	//@Override
	public Operand ltRaw(final int index, final Operand rhs, final int rhi) {

		return new IntScalar((value.compareTo(rhs.toByteStringRaw(rhi)) < 0) ? 1
				: 0);
	}

	//@Override
	public Operand geRaw(final int index, final Operand rhs, final int rhi) {

		return new IntScalar((value.compareTo(rhs.toByteStringRaw(rhi)) >= 0) ? 1
				: 0);
	}

	//@Override
	public Operand leRaw(final int index, final Operand rhs, final int rhi) {

		return new IntScalar((value.compareTo(rhs.toByteStringRaw(rhi)) <= 0) ? 1
				: 0);
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

		return value.get(offset);
	}

	//@Override
	public void poke(int index, int offset, byte value) {

		this.value.set(offset, value);

	}

    @Override
    public void assignNeRaw(int index, Operand rhs, int rhi) {
        throw unsupportedOperator("= !=");
    }

    @Override
    public void assignGtRaw(int index, Operand rhs, int rhi) {
        throw unsupportedOperator("= >=");
    }

    @Override
    public void assignLtRaw(int index, Operand rhs, int rhi) {
        throw unsupportedOperator("= <=");
    }

    @Override
    public void assignGtEqRaw(int index, Operand rhs, int rhi) {
        throw unsupportedOperator("= >=");
    }

    @Override
    public void assignLtEqRaw(int index, Operand rhs, int rhi) {
        throw unsupportedOperator("= <=");
    }
}
