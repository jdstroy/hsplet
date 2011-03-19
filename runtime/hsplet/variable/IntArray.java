/*
 * $Id: IntArray.java,v 1.2.4.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.variable;

/**
 * int の配列を表すクラス。
 * 
 * 
 * @author Yuki
 * @version $Revision: 1.2.4.1 $, $Date: 2006/08/02 12:13:06 $
 */
public final class IntArray extends Array {

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = -4406165790569093040L;

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: IntArray.java,v 1.2.4.1 2006/08/02 12:13:06 Yuki Exp $";

	/** オブジェクトの値。 */
	private int[] values;

	/**
	 * 要素数 16 の配列を構築する。
	 */
	public IntArray() {

		this(16, 1, 1, 1);
	}

	/**
	 * 要素数を指定してオブジェクトを構築する。
	 * <p>
	 * オリジナル HSP と違い、要素数に 0 を指定することは出来ない。 つまり、配列は必ず四次元あることになる。 そのほうが要素数などの計算が簡単。
	 * </p>
	 * 
	 * @param l0 一次元目の要素数。
	 * @param l1 二次元目の要素数。
	 * @param l2 三次元目の要素数。
	 * @param l3 四次元目の要素数。
	 */
	public IntArray(final int l0, final int l1, final int l2, final int l3) {

		super(l0, l1, l2, l3);
		values = new int[l0 * l1 * l2 * l3];
	}

	//@Override
	public int getType() {

		return Type.INTEGER;
	}

	//@Override
	public String toString(final int index) {

		return Integer.toString(values[index]);
	}

	//@Override
	public ByteString toByteString(final int index) {

		return new ByteString(Integer.toString(values[index]));
	}

	//@Override
	public int toInt(final int index) {

		return values[index];
	}

	//@Override
	public double toDouble(final int index) {

		return values[index];
	}

	//@Override
	public Operand dup(int index) {
		return Scalar.fromValue(values[index]);
	}

	//@Override
	public void inc(final int index) {

		++values[index];
	}

	//@Override
	public void dec(final int index) {

		--values[index];
	}

	//@Override
	public void assign(final int index, final Operand rhs, final int rhi) {

		if (index >= values.length) {
			expand(index);
		}
		values[index] = rhs.toInt(rhi);
	}

	//@Override
	public void assignAdd(final int index, final Operand rhs, final int rhi) {

		if (index >= values.length) {
			expand(index);
		}
		values[index] += rhs.toInt(rhi);
	}

	//@Override
	public void assignSub(final int index, final Operand rhs, final int rhi) {

		if (index >= values.length) {
			expand(index);
		}
		values[index] -= rhs.toInt(rhi);
	}

	//@Override
	public void assignMul(final int index, final Operand rhs, final int rhi) {

		if (index >= values.length) {
			expand(index);
		}
		values[index] *= rhs.toInt(rhi);
	}

	//@Override
	public void assignDiv(final int index, final Operand rhs, final int rhi) {

		if (index >= values.length) {
			expand(index);
		}
		values[index] /= rhs.toInt(rhi);
	}

	//@Override
	public void assignMod(final int index, final Operand rhs, final int rhi) {

		if (index >= values.length) {
			expand(index);
		}
		values[index] %= rhs.toInt(rhi);
	}

	//@Override
	public void assignAnd(final int index, final Operand rhs, final int rhi) {

		if (index >= values.length) {
			expand(index);
		}
		values[index] &= rhs.toInt(rhi);
	}

	//@Override
	public void assignOr(final int index, final Operand rhs, final int rhi) {

		if (index >= values.length) {
			expand(index);
		}
		values[index] |= rhs.toInt(rhi);
	}

	//@Override
	public void assignXor(final int index, final Operand rhs, final int rhi) {

		if (index >= values.length) {
			expand(index);
		}
		values[index] ^= rhs.toInt(rhi);
	}

	//@Override
	public void assignSr(final int index, final Operand rhs, final int rhi) {

		if (index >= values.length) {
			expand(index);
		}
		values[index] >>= rhs.toInt(rhi);
	}

	//@Override
	public void assignSl(final int index, final Operand rhs, final int rhi) {

		if (index >= values.length) {
			expand(index);
		}

		values[index] <<= rhs.toInt(rhi);
	}

	//@Override
	public void expand(final int index) {

		super.expand(index);

		final int[] newValues = new int[l0 * l1 * l2 * l3];

		System.arraycopy(values, 0, newValues, 0, values.length);

		values = newValues;

	}

	//@Override
	public Operand add(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(values[index] + rhs.toInt(rhi));
	}

	//@Override
	public Operand eq(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue((values[index] == rhs.toInt(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand ne(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue((values[index] != rhs.toInt(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand gt(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue((values[index] > rhs.toInt(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand lt(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue((values[index] < rhs.toInt(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand ge(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue((values[index] >= rhs.toInt(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand le(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue((values[index] <= rhs.toInt(rhi)) ? 1 : 0);
	}

	//@Override
	public Operand sub(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(values[index] - rhs.toInt(rhi));
	}

	//@Override
	public Operand mul(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(values[index] * rhs.toInt(rhi));
	}

	//@Override
	public Operand div(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(values[index] / rhs.toInt(rhi));
	}

	//@Override
	public Operand mod(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(values[index] % rhs.toInt(rhi));
	}

	//@Override
	public Operand and(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(values[index] & rhs.toInt(rhi));
	}

	//@Override
	public Operand or(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(values[index] | rhs.toInt(rhi));
	}

	//@Override
	public Operand xor(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(values[index] ^ rhs.toInt(rhi));
	}

	//@Override
	public Operand sl(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(values[index] << rhs.toInt(rhi));
	}

	//@Override
	public Operand sr(final int index, final Operand rhs, final int rhi) {

		return Scalar.fromValue(values[index] >> rhs.toInt(rhi));
	}

	//@Override
	public byte peek(int index, int offset) {

		return (byte) (values[index + offset / 4] >> (offset % 4 * 8));
	}

	//@Override
	public void poke(int index, int offset, byte value) {

		int bits = values[index + offset / 4];

		bits &= ~(0xFF << (offset % 4 * 8));
		bits |= (value & 0xFF) << (offset % 4 * 8);

		values[index + offset / 4] = bits;

	}
}
