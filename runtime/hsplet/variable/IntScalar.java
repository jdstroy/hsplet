/*
 * $Id: IntScalar.java,v 1.2.4.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.variable;

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
	public String toString(final int index) {

		return Integer.toString(value);
	}

	//@Override
	public ByteString toByteString(final int index) {

		return new ByteString(Integer.toString(value));
	}

	//@Override
	public int toInt(final int index) {

		return value;
	}

	//@Override
	public double toDouble(final int index) {

		return value;
	}

	//@Override
	public Operand dup(int index) {
		return Scalar.fromValue(value);
	}

	//@Override
	public void inc(final int index) {

		++value;
	}

	//@Override
	public void dec(final int index) {

		--value;
	}

	//@Override
	public void assign(final int index, final Operand rhs, final int rhi) {

		value = rhs.toInt(rhi);
	}

	//@Override
	public void assignAdd(final int index, final Operand rhs, final int rhi) {

		value += rhs.toInt(rhi);
	}

	//@Override
	public void assignSub(final int index, final Operand rhs, final int rhi) {

		value -= rhs.toInt(rhi);
	}

	//@Override
	public void assignMul(final int index, final Operand rhs, final int rhi) {

		value *= rhs.toInt(rhi);
	}

	//@Override
	public void assignDiv(final int index, final Operand rhs, final int rhi) {

		value /= rhs.toInt(rhi);
	}

	//@Override
	public void assignMod(final int index, final Operand rhs, final int rhi) {

		value %= rhs.toInt(rhi);
	}

	//@Override
	public void assignAnd(final int index, final Operand rhs, final int rhi) {

		value &= rhs.toInt(rhi);
	}

	//@Override
	public void assignOr(final int index, final Operand rhs, final int rhi) {

		value |= rhs.toInt(rhi);
	}

	//@Override
	public void assignXor(final int index, final Operand rhs, final int rhi) {

		value ^= rhs.toInt(rhi);

	}

	//@Override
	public void assignSr(final int index, final Operand rhs, final int rhi) {

		value >>= rhs.toInt(rhi);
	}

	//@Override
	public void assignSl(final int index, final Operand rhs, final int rhi) {

		value <<= rhs.toInt(rhi);
	}

	//@Override
	public Operand add(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(value + rhs.toInt(rhi));
	}

	//@Override
	public Operand eq(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue((value == rhs.toInt(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand ne(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue((value != rhs.toInt(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand gt(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue((value > rhs.toInt(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand lt(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue((value < rhs.toInt(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand ge(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue((value >= rhs.toInt(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand le(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue((value <= rhs.toInt(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand sub(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(value - rhs.toInt(rhi));
	}

	//@Override
	public Operand mul(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(value * rhs.toInt(rhi));
	}

	//@Override
	public Operand div(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(value / rhs.toInt(rhi));
	}

	//@Override
	public Operand mod(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(value % rhs.toInt(rhi));
	}

	//@Override
	public Operand and(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(value & rhs.toInt(rhi));
	}

	//@Override
	public Operand or(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(value | rhs.toInt(rhi));
	}

	//@Override
	public Operand xor(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(value ^ rhs.toInt(rhi));
	}

	//@Override
	public Operand sl(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(value << rhs.toInt(rhi));
	}

	//@Override
	public Operand sr(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(value >> rhs.toInt(rhi));
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
}
