/*
 * $Id: Variable.java,v 1.3.2.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.variable;

/**
 * HSP �̕ϐ���\���N���X�B
 * <p>
 * HSP �̕ϐ��� dim/sdim �� dup �Ȃǂɂ���Č^��Q�Ɛ�𓮓I�ɕύX�ł��Ȃ���΂����Ȃ��B
 * </p>
 * </p>
 * Array �� Scalar �Ȃǂ̃T�u�N���X�𒼐ڎg�p����̂ł͂Ȃ��A�ꖇ������Ԃ���B
 * </p>
 * <p>
 * �{���I�� Reference �Ɠ��������A�x�[�X�C���f�b�N�X�� 0 �ł��邱�Ƃ�O��ɂ��Ă��镪�����B �܂��A���ۂɂ̓R���p�C���ɂ���ăC�����C���W�J�����̂ŁA�p�t�H�[�}���X�̒ቺ�͍ŏ����ɗ}������B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.3.2.1 $, $Date: 2006/08/02 12:13:06 $
 */
public final class Variable extends Operand {

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = 3981954990729087063L;

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: Variable.java,v 1.3.2.1 2006/08/02 12:13:06 Yuki Exp $";
	//private static int globalErrorIndex=1;
	//public int myErrorIndex;

	/** �l��ێ�����I�u�W�F�N�g�B */
	public Operand value;

	/**
	 * int �z��^�̕ϐ����\�z����B
	 */
	public Variable() {

		//myErrorIndex=globalErrorIndex++;
		this.value = new IntArray();
	}

	/**
	 * �ϐ��̒l���O������ύX�����\��������Ƃ݂Ȃ��B
	 * @return �O������ύX�����l�I�u�W�F�N�g.
	 */
	public VolatileValue makeVolatile() {

		if (value instanceof VolatileValue) {
			return (VolatileValue) value;
		}

		final VolatileValue result = new VolatileValue(value);
		this.value = result;
		return result;
	}

	//@Override
	public int getType() {

		return value.getType();
	}

	//@Override
	public String toString(final int index) {

		return value.toString(index);
	}

	//@Override
	public ByteString toByteString(final int index) {

		return value.toByteString(index);
	}

	//@Override
	public int toInt(final int index) {

		return value.toInt(index);
	}

	//@Override
	public double toDouble(final int index) {

		return value.toDouble(index);
	}

	//@Override
	public Operand dup(int index) {
		return value.dup(index);
	}
	
	//@Override
	public int getIndex(final int i0, final int i1) {

		return value.getIndex(i0, i1);
	}

	//@Override
	public int getIndex(final int i0, final int i1, final int i2) {

		return value.getIndex(i0, i1, i2);
	}

	//@Override
	public int getIndex(final int i0, final int i1, final int i2, final int i3) {

		return value.getIndex(i0, i1, i2, i3);
	}

	//@Override
	public void inc(final int index) {

		value.inc(index);
	}

	//@Override
	public void dec(final int index) {

		value.inc(index);
	}

	//@Override
	public void assign(final int index, final Operand rhs, final int rhi) {

		// �^���Ⴄ����A�����������
		if (getType() != rhs.getType()) {
			//System.out.println(myErrorIndex+" Assign change: "+getType()+" "+rhs.getType());
			switch (rhs.getType()) {
			case Type.INTEGER:
				value = new IntArray();
				break;
			case Type.DOUBLE:
				value = new DoubleArray();
				break;
			case Type.STRING:
				value = new StringArray();
				break;
			default:
				throw new UnsupportedOperationException("�^ " + rhs.getType() + " �̓T�|�[�g����Ă��܂���B");
			}
		}

		value.assign(index, rhs, rhi);
	}

	//@Override
	public void assignAdd(final int index, final Operand rhs, final int rhi) {

		value.assignAdd(index, rhs, rhi);
	}

	//@Override
	public void assignSub(final int index, final Operand rhs, final int rhi) {

		value.assignSub(index, rhs, rhi);
	}

	//@Override
	public void assignMul(final int index, final Operand rhs, final int rhi) {

		value.assignMul(index, rhs, rhi);
	}

	//@Override
	public void assignDiv(final int index, final Operand rhs, final int rhi) {

		value.assignDiv(index, rhs, rhi);
	}

	//@Override
	public void assignMod(final int index, final Operand rhs, final int rhi) {

		value.assignMod(index, rhs, rhi);
	}

	//@Override
	public void assignAnd(final int index, final Operand rhs, final int rhi) {

		value.assignAnd(index, rhs, rhi);
	}

	//@Override
	public void assignOr(final int index, final Operand rhs, final int rhi) {

		value.assignOr(index, rhs, rhi);
	}

	//@Override
	public void assignXor(final int index, final Operand rhs, final int rhi) {

		value.assignXor(index, rhs, rhi);
	}

	//@Override
	public void assignSr(final int index, final Operand rhs, final int rhi) {

		value.assignSr(index, rhs, rhi);
	}

	//@Override
	public void assignSl(final int index, final Operand rhs, final int rhi) {

		value.assignSl(index, rhs, rhi);
	}

	//@Override
	public Operand eq(final int index, final Operand rhs, final int rhi) {

		return value.eq(index, rhs, rhi);
	}

	//@Override
	public Operand ne(final int index, final Operand rhs, final int rhi) {

		return value.ne(index, rhs, rhi);
	}

	//@Override
	public Operand gt(final int index, final Operand rhs, final int rhi) {

		return value.gt(index, rhs, rhi);
	}

	//@Override
	public Operand lt(final int index, final Operand rhs, final int rhi) {

		return value.lt(index, rhs, rhi);
	}

	//@Override
	public Operand ge(final int index, final Operand rhs, final int rhi) {

		return value.ge(index, rhs, rhi);
	}

	//@Override
	public Operand le(final int index, final Operand rhs, final int rhi) {

		return value.le(index, rhs, rhi);
	}

	//@Override
	public Operand add(final int index, final Operand rhs, final int rhi) {

		return value.add(index, rhs, rhi);
	}

	//@Override
	public Operand sub(final int index, final Operand rhs, final int rhi) {

		return value.sub(index, rhs, rhi);
	}

	//@Override
	public Operand mul(final int index, final Operand rhs, final int rhi) {

		return value.mul(index, rhs, rhi);
	}

	//@Override
	public Operand div(final int index, final Operand rhs, final int rhi) {

		return value.div(index, rhs, rhi);
	}

	//@Override
	public Operand mod(final int index, final Operand rhs, final int rhi) {

		return value.mod(index, rhs, rhi);
	}

	//@Override
	public Operand and(final int index, final Operand rhs, final int rhi) {

		return value.and(index, rhs, rhi);
	}

	//@Override
	public Operand or(final int index, final Operand rhs, final int rhi) {

		return value.or(index, rhs, rhi);
	}

	//@Override
	public Operand xor(final int index, final Operand rhs, final int rhi) {

		return value.xor(index, rhs, rhi);
	}

	//@Override
	public Operand sl(final int index, final Operand rhs, final int rhi) {

		return value.sl(index, rhs, rhi);
	}

	//@Override
	public Operand sr(final int index, final Operand rhs, final int rhi) {

		return value.sr(index, rhs, rhi);
	}

	//@Override
	public int l0() {

		return value.l0();
	}

	//@Override
	public int l1() {

		return value.l1();
	}

	//@Override
	public int l2() {

		return value.l2();
	}

	//@Override
	public int l3() {

		return value.l3();
	}

	//@Override
	public byte peek(int index, int offset) {

		return value.peek(index, offset);
	}

	//@Override
	public void poke(int index, int offset, byte value) {

		this.value.poke(index, offset, value);

	}

    @Override
    public void assignNe(int index, Operand rhs, int rhi) {
        value.assignNe(index, rhs, rhi);
    }

    @Override
    public void assignGt(int index, Operand rhs, int rhi) {
        value.assignGt(index, rhs, rhi);
    }

    @Override
    public void assignLt(int index, Operand rhs, int rhi) {
        value.assignLt(index, rhs, rhi);
    }

    @Override
    public void assignGtEq(int index, Operand rhs, int rhi) {
        value.assignGtEq(index, rhs, rhi);
    }

    @Override
    public void assignLtEq(int index, Operand rhs, int rhi) {
        value.assignLtEq(index, rhs, rhi);
    }

}
