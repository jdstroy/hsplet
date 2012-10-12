/*
 * $Id: DoubleScalar.java,v 1.2.4.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.variable;
import hsplet.util.Conversion;

/**
 * double を表すクラス。
 * 
 * 
 * @author Yuki
 * @version $Revision: 1.2.4.1 $, $Date: 2006/08/02 12:13:06 $
 */
public final class DoubleScalar extends Scalar {

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = -3521657637646866475L;

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: DoubleScalar.java,v 1.2.4.1 2006/08/02 12:13:06 Yuki Exp $";

	/** オブジェクトの値。 */
	public double value;

	/**
	 * 値を指定してオブジェクトを構築する。
	 * 
	 * @param value 初期値。
	 */
	public DoubleScalar(final double value) {

		this.value = value;
	}

	//@Override
	public int getType() {

		return Type.DOUBLE;
	}

	//@Override
	public String toStringRaw(final int index) {

		return Double.toString(value);
	}

	//@Override
	public ByteString toByteStringRaw(final int index) {

		return new ByteString(Double.toString(value));
	}

	//@Override
	public int toIntRaw(final int index) {

		return (int) value;
	}

	//@Override
	public double toDoubleRaw(final int index) {

		return value;
	}

	//@Override
	public Operand dupRaw(int index) {
		return Scalar.fromValue(value);
	}

	//@Override
	public void incRaw(final int index) {

		++value;
	}

	//@Override
	public void decRaw(final int index) {

		--value;
	}

	public void assignRaw(final int index, final int newValue){
		value = (double)newValue;
	}
	public void assignRaw(final int index, final double newValue){
		value = newValue;
	}
	public void assignRaw(final int index, final String newValue){
		value = Conversion.strtod(newValue);
	}

	//@Override
	public void assignRaw(final int index, final Operand rhs, final int rhi) {

		value = rhs.toDoubleRaw(rhi);
	}

	//@Override
	public void assignAddRaw(final int index, final Operand rhs, final int rhi) {

		value += rhs.toDoubleRaw(rhi);
	}

	//@Override
	public void assignSubRaw(final int index, final Operand rhs, final int rhi) {

		value -= rhs.toDoubleRaw(rhi);
	}

	//@Override
	public void assignMulRaw(final int index, final Operand rhs, final int rhi) {

		value *= rhs.toDoubleRaw(rhi);
	}

	//@Override
	public void assignDivRaw(final int index, final Operand rhs, final int rhi) {

		value /= rhs.toDoubleRaw(rhi);
	}

	//@Override
	public void assignModRaw(final int index, final Operand rhs, final int rhi) {

		value %= rhs.toDoubleRaw(rhi);
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

		return new DoubleScalar(value + rhs.toDoubleRaw(rhi));
	}

	//@Override
	public Operand eqRaw(final int index, final Operand rhs, final int rhi) {

		return new IntScalar((value == rhs.toDoubleRaw(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand neRaw(final int index, final Operand rhs, final int rhi) {

		return new IntScalar((value != rhs.toDoubleRaw(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand gtRaw(final int index, final Operand rhs, final int rhi) {

		return new IntScalar((value > rhs.toDoubleRaw(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand ltRaw(final int index, final Operand rhs, final int rhi) {

		return new IntScalar((value < rhs.toDoubleRaw(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand geRaw(final int index, final Operand rhs, final int rhi) {

		return new IntScalar((value >= rhs.toDoubleRaw(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand leRaw(final int index, final Operand rhs, final int rhi) {

		return new IntScalar((value <= rhs.toDoubleRaw(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand subRaw(final int index, final Operand rhs, final int rhi) {

		return new DoubleScalar(value - rhs.toDoubleRaw(rhi));
	}

	//@Override
	public Operand mulRaw(final int index, final Operand rhs, final int rhi) {

		return new DoubleScalar(value * rhs.toDoubleRaw(rhi));
	}

	//@Override
	public Operand divRaw(final int index, final Operand rhs, final int rhi) {

		return new DoubleScalar(value / rhs.toDoubleRaw(rhi));
	}

	//@Override
	public Operand modRaw(final int index, final Operand rhs, final int rhi) {

		return new DoubleScalar(value % rhs.toDoubleRaw(rhi));
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

		return (byte) (Double.doubleToRawLongBits(value) >> (offset % 8 * 8));
	}

	//@Override
	public void poke(int index, int offset, byte value) {

		long bits = Double.doubleToRawLongBits(value);

		bits &= ~(0xFFL << (offset % 8 * 8));
		bits |= (value & 0xFFL) << (offset % 8 * 8);

		this.value = Double.longBitsToDouble(bits);
	}

    @Override
    public void assignNeRaw(int index, Operand rhs, int rhi) {
        value = (value != rhs.toDoubleRaw(rhi)) ? 1.0 : 0.0;
    }

    @Override
    public void assignGtRaw(int index, Operand rhs, int rhi) {
        value = (value > rhs.toDoubleRaw(rhi)) ? 1.0 : 0.0;
    }

    @Override
    public void assignLtRaw(int index, Operand rhs, int rhi) {
        value = (value < rhs.toDoubleRaw(rhi)) ? 1.0 : 0.0;
    }

    @Override
    public void assignGtEqRaw(int index, Operand rhs, int rhi) {
        value = (value >= rhs.toDoubleRaw(rhi)) ? 1.0 : 0.0;
    }

    @Override
    public void assignLtEqRaw(int index, Operand rhs, int rhi) {
        value = (value <= rhs.toDoubleRaw(rhi)) ? 1.0 : 0.0;
    }
}
