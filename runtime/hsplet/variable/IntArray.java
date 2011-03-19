/*
 * $Id: IntArray.java,v 1.2.4.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.variable;

/**
 * int �̔z���\���N���X�B
 * 
 * 
 * @author Yuki
 * @version $Revision: 1.2.4.1 $, $Date: 2006/08/02 12:13:06 $
 */
public final class IntArray extends Array {

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = -4406165790569093040L;

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: IntArray.java,v 1.2.4.1 2006/08/02 12:13:06 Yuki Exp $";

	/** �I�u�W�F�N�g�̒l�B */
	private int[] values;

	/**
	 * �v�f�� 16 �̔z����\�z����B
	 */
	public IntArray() {

		this(16, 1, 1, 1);
	}

	/**
	 * �v�f�����w�肵�ăI�u�W�F�N�g���\�z����B
	 * <p>
	 * �I���W�i�� HSP �ƈႢ�A�v�f���� 0 ���w�肷�邱�Ƃ͏o���Ȃ��B �܂�A�z��͕K���l�������邱�ƂɂȂ�B ���̂ق����v�f���Ȃǂ̌v�Z���ȒP�B
	 * </p>
	 * 
	 * @param l0 �ꎟ���ڂ̗v�f���B
	 * @param l1 �񎟌��ڂ̗v�f���B
	 * @param l2 �O�����ڂ̗v�f���B
	 * @param l3 �l�����ڂ̗v�f���B
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
