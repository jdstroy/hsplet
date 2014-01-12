/*
 * $Id: IntScalar.java,v 1.2.4.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.variable;
import hsplet.util.Conversion;

/**
 * int の配列を表すクラス。
 * 
 * @author Yuki
 * @version $Revision: 1.2.4.1 $, $Date: 2006/08/02 12:13:06 $
 */
public class IntScalar extends Scalar {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: IntScalar.java,v 1.2.4.1 2006/08/02 12:13:06 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = -798941318580432310L;

	/** オブジェクトの値。 */
	public int value;

	/**
	 * 値を指定してオブジェクトを構築する。
	 * 
	 * @param value 初期値。
	 */
	public IntScalar(final int value) {

		this.value = value;
	}

	//@Override
	public int getType() {

		return Type.INTEGER;
	}

	//@Override
	public String toStringRaw(final int index) {

		return Integer.toString(value);
	}

	//@Override
	public ByteString toByteStringRaw(final int index) {

		return new ByteString(Integer.toString(value));
	}

	//@Override
	public int toIntRaw(final int index) {

		return value;
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
		value = newValue;
	}
	public void assignRaw(final int index, final double newValue){
		value = (int)newValue;
	}
	public void assignRaw(final int index, final String newValue){
		value = Conversion.strtoi(newValue);;
	}

	//@Override
	public void assignRaw(final int index, final Operand rhs, final int rhi) {

		value = rhs.toIntRaw(rhi);
	}

	//@Override
	public void assignAddRaw(final int index, final Operand rhs, final int rhi) {

		value += rhs.toIntRaw(rhi);
	}

	//@Override
	public void assignSubRaw(final int index, final Operand rhs, final int rhi) {

		value -= rhs.toIntRaw(rhi);
	}

	//@Override
	public void assignMulRaw(final int index, final Operand rhs, final int rhi) {

		value *= rhs.toIntRaw(rhi);
	}

	//@Override
	public void assignDivRaw(final int index, final Operand rhs, final int rhi) {

		value /= rhs.toIntRaw(rhi);
	}

	//@Override
	public void assignModRaw(final int index, final Operand rhs, final int rhi) {

		value %= rhs.toIntRaw(rhi);
	}

	//@Override
	public void assignAndRaw(final int index, final Operand rhs, final int rhi) {

		value &= rhs.toIntRaw(rhi);
	}

	//@Override
	public void assignOrRaw(final int index, final Operand rhs, final int rhi) {

		value |= rhs.toIntRaw(rhi);
	}

	//@Override
	public void assignXorRaw(final int index, final Operand rhs, final int rhi) {

		value ^= rhs.toIntRaw(rhi);

	}

	//@Override
	public void assignSrRaw(final int index, final Operand rhs, final int rhi) {

		value >>= rhs.toIntRaw(rhi);
	}

	//@Override
	public void assignSlRaw(final int index, final Operand rhs, final int rhi) {

		value <<= rhs.toIntRaw(rhi);
	}

	//@Override
	public Operand addRaw(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(value + rhs.toIntRaw(rhi));
	}

	//@Override
	public Operand eqRaw(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue((value == rhs.toIntRaw(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand neRaw(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue((value != rhs.toIntRaw(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand gtRaw(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue((value > rhs.toIntRaw(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand ltRaw(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue((value < rhs.toIntRaw(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand geRaw(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue((value >= rhs.toIntRaw(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand leRaw(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue((value <= rhs.toIntRaw(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand subRaw(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(value - rhs.toIntRaw(rhi));
	}

	//@Override
	public Operand mulRaw(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(value * rhs.toIntRaw(rhi));
	}

	//@Override
	public Operand divRaw(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(value / rhs.toIntRaw(rhi));
	}

	//@Override
	public Operand modRaw(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(value % rhs.toIntRaw(rhi));
	}

	//@Override
	public Operand andRaw(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(value & rhs.toIntRaw(rhi));
	}

	//@Override
	public Operand orRaw(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(value | rhs.toIntRaw(rhi));
	}

	//@Override
	public Operand xorRaw(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(value ^ rhs.toIntRaw(rhi));
	}

	//@Override
	public Operand slRaw(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(value << rhs.toIntRaw(rhi));
	}

	//@Override
	public Operand srRaw(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(value >> rhs.toIntRaw(rhi));
	}

	//@Override
	public byte peek(int index, int offset) {

		return (byte) (value >> (offset % 4 * 8));
	}

	//@Override
	public void poke(int index, int offset, byte value) {

		this.value &= ~(0xFF << (offset % 4 * 8));
		this.value |= (value & 0xFF) << (offset % 4 * 8);

	}

    @Override
    public void assignNeRaw(int index, Operand rhs, int rhi) {
        value = (value != rhs.toIntRaw(rhi)) ? 1 : 0;
    }

    @Override
    public void assignGtRaw(int index, Operand rhs, int rhi) {
        value = (value > rhs.toIntRaw(rhi)) ? 1 : 0;
    }

    @Override
    public void assignLtRaw(int index, Operand rhs, int rhi) {
        value = (value < rhs.toIntRaw(rhi)) ? 1 : 0;
    }

    @Override
    public void assignGtEqRaw(int index, Operand rhs, int rhi) {
        value = (value >= rhs.toIntRaw(rhi)) ? 1 : 0;
    }

    @Override
    public void assignLtEqRaw(int index, Operand rhs, int rhi) {
        value = (value <= rhs.toIntRaw(rhi)) ? 1 : 0;
    }
}
