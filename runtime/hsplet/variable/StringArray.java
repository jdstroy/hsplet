/*
 * $Id: StringArray.java,v 1.4.2.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.variable;

import hsplet.util.Conversion;

/**
 * ������̔z���\���N���X�B
 * 
 * 
 * @author Yuki
 * @version $Revision: 1.4.2.1 $, $Date: 2006/08/02 12:13:06 $
 */
public final class StringArray extends Array {

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = 5025854224973749708L;

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: StringArray.java,v 1.4.2.1 2006/08/02 12:13:06 Yuki Exp $";

	/** �I�u�W�F�N�g�̒l�B */
	private ByteString[] values;

	/**
	 * ���� 64�A�v�f�� 1 �̔z����\�z����B
	 */
	public StringArray() {

		this(64, 1, 1, 1, 1);
	}

	/**
	 * ���������Ɨv�f�����w�肵�ăI�u�W�F�N�g���\�z����B
	 * <p>
	 * �I���W�i�� HSP �ƈႢ�A�v�f���� 0 ���w�肷�邱�Ƃ͏o���Ȃ��B �܂�A�z��͕K���l�������邱�ƂɂȂ�B ���̂ق����v�f���Ȃǂ̌v�Z���ȒP�B
	 * </p>
	 * 
	 * @param length ������Ԃł̕�����o�b�t�@�T�C�Y�B
	 * @param l0 �ꎟ���ڂ̗v�f���B
	 * @param l1 �񎟌��ڂ̗v�f���B
	 * @param l2 �O�����ڂ̗v�f���B
	 * @param l3 �l�����ڂ̗v�f���B
	 */
	public StringArray(final int length, final int l0, final int l1,
			final int l2, final int l3) {

		super(l0, l1, l2, l3);
		values = new ByteString[l0 * l1 * l2 * l3];
		for (int i = 0; i < values.length; ++i) {
			values[i] = new ByteString(new byte[length], 0, 0, false);
		}
	}

	//@Override
	public int getType() {

		return Type.STRING;
	}

	//@Override
	public String toString(final int index) {

		return values[index].toString();
	}

	//@Override
	public ByteString toByteString(final int index) {

		return values[index];
	}

	//@Override
	public int toInt(final int index) {

		return Conversion.strtoi(values[index].toString());
	}

	//@Override
	public double toDouble(final int index) {

		return Conversion.strtod(values[index].toString());
	}

	//@Override
	public Operand dup(int index) {
		return Scalar.fromValue(values[index]);
	}

	//@Override
	public void inc(final int index) {

	}

	//@Override
	public void dec(final int index) {

	}

	//@Override
	public void assign(final int index, final Operand rhs, final int rhi) {

		if (index >= values.length) {
			expand(index);
		}
		values[index].assign(rhs.toByteString(rhi));
	}

	//@Override
	public void assignAdd(final int index, final Operand rhs, final int rhi) {

		if (index >= values.length) {
			expand(index);
		}
		values[index].append(rhs.toByteString(rhi));
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
	public void expand(final int index) {

		super.expand(index);

		final ByteString[] newValues = new ByteString[l0 * l1 * l2 * l3];

		System.arraycopy(values, 0, newValues, 0, values.length);
		for (int i = values.length; i < newValues.length; ++i) {
			newValues[i] = new ByteString(new byte[256], 0, 0, false);
		}

		values = newValues;

	}

	//@Override
	public Operand add(final int index, final Operand rhs, final int rhi) {

		return new StringScalar(ByteString.concat(values[index], rhs
				.toByteString(rhi)));
	}

	//@Override
	public Operand eq(final int index, final Operand rhs, final int rhi) {

		return new IntScalar(values[index].equals(rhs.toByteString(rhi)) ? 1: 0);
	}

	//@Override
	public Operand ne(final int index, final Operand rhs, final int rhi) {

		return new IntScalar(values[index].compareTo(rhs.toByteString(rhi)));
	}

	//@Override
	public Operand gt(final int index, final Operand rhs, final int rhi) {

		return new IntScalar(
				(values[index].compareTo(rhs.toByteString(rhi)) > 0) ? 1 : 0);
	}

	//@Override
	public Operand lt(final int index, final Operand rhs, final int rhi) {

		return new IntScalar(
				(values[index].compareTo(rhs.toByteString(rhi)) < 0) ? 1 : 0);
	}

	//@Override
	public Operand ge(final int index, final Operand rhs, final int rhi) {

		return new IntScalar(
				(values[index].compareTo(rhs.toByteString(rhi)) >= 0) ? 1 : 0);
	}

	//@Override
	public Operand le(final int index, final Operand rhs, final int rhi) {

		return new IntScalar(
				(values[index].compareTo(rhs.toByteString(rhi)) <= 0) ? 1 : 0);
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

		return values[index].get(offset);
	}

	//@Override
	public void poke(int index, int offset, byte value) {

		values[index].set(offset, value);
	}
}
