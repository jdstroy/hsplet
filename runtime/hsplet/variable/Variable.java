/*
 * $Id: Variable.java,v 1.3.2.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.variable;

import hsplet.HSPError;

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
	public Operand checkVar() {
		return this;
	}

	//@Override
	public String toStringRaw(final int index) {

		return value.toStringRaw(index);
	}

	//@Override
	public ByteString toByteStringRaw(final int index) {

		return value.toByteStringRaw(index);
	}

	//@Override
	public int toIntRaw(final int index) {

		return value.toIntRaw(index);
	}

	//@Override
	public double toDoubleRaw(final int index) {

		return value.toDoubleRaw(index);
	}

	//@Override
	public Operand dupRaw(int index) {
		return value.dupRaw(index);
	}

	public void checkIncrementSize(int size) {
		value.checkIncrementSize(size);
	}
    public int checkSize0(int size) {
        return value.checkSize0(size);
    }
    public int checkSize1(int size) {
        return value.checkSize1(size);
    }
    public int checkSize2(int size) {
        return value.checkSize2(size);
    }
    public int checkSize3(int size) {
        return value.checkSize3(size);
    }

	public int checkResize0(int size) {
        return value.checkResize0(size);
	}
	public int checkResize1(int size) {
        return value.checkResize1(size);
	}
	public int checkResize2(int size) {
        return value.checkResize2(size);
	}
	public int checkResize3(int size) {
        return value.checkResize3(size);
	}
	//@Override
	public int getIndex(final int i0) {

		return value.getIndex(i0);
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
    public int getResizeIndex(final int i0) {

        return value.getResizeIndex(i0);
    }

    //@Override
    public int getResizeIndex(final int i0, final int i1) {

        return value.getResizeIndex(i0, i1);
    }

    //@Override
    public int getResizeIndex(final int i0, final int i1, final int i2) {

        return value.getResizeIndex(i0, i1, i2);
    }

    //@Override
    public int getResizeIndex(final int i0, final int i1, final int i2, final int i3) {

        return value.getResizeIndex(i0, i1, i2, i3);
    }

	//@Override
	public void incRaw(final int index) {

		value.incRaw(index);
	}

	//@Override
	public void decRaw(final int index) {

		value.decRaw(index);
	}

	public void assignRaw(final int index, final int newValue) {
		if(value.getType() != Type.INTEGER) {
			if(index != 0) {
				error(HSPError.AssignToDifferentType, "", "Invalid type of array");
				return;
			}
			value = new IntArray();
		}
		value.assignRaw(index, newValue);
	}
	public void assignRaw(final int index, final double newValue){
		if(value.getType() != Type.DOUBLE) {
			if(index != 0) {
				error(HSPError.AssignToDifferentType, "", "Invalid type of array");
				return;
			}
			value = new DoubleArray();
		}
		value.assignRaw(index, newValue);
	}
	public void assignRaw(final int index, final String newValue){
		if(value.getType() != Type.STRING) {
			if(index != 0) {
				error(HSPError.AssignToDifferentType, "", "Invalid type of array");
				return;
			}
			value = new StringArray();
		}
		value.assignRaw(index, newValue);
	}

	//@Override
	public void assignRaw(final int index, final Operand rhs, final int rhi) {

		// �^���Ⴄ����A�����������
		if (value.getType() != rhs.getType()) {
			if(index != 0) {
				error(HSPError.AssignToDifferentType, "", "Invalid type of array");
				return;
			}
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

		value.assignRaw(index, rhs, rhi);
	}

	//@Override
	public void assignAddRaw(final int index, final Operand rhs, final int rhi) {

		value.assignAddRaw(index, rhs, rhi);
	}

	//@Override
	public void assignSubRaw(final int index, final Operand rhs, final int rhi) {

		value.assignSubRaw(index, rhs, rhi);
	}

	//@Override
	public void assignMulRaw(final int index, final Operand rhs, final int rhi) {

		value.assignMulRaw(index, rhs, rhi);
	}

	//@Override
	public void assignDivRaw(final int index, final Operand rhs, final int rhi) {

		value.assignDivRaw(index, rhs, rhi);
	}

	//@Override
	public void assignModRaw(final int index, final Operand rhs, final int rhi) {

		value.assignModRaw(index, rhs, rhi);
	}

	//@Override
	public void assignAndRaw(final int index, final Operand rhs, final int rhi) {

		value.assignAndRaw(index, rhs, rhi);
	}

	//@Override
	public void assignOrRaw(final int index, final Operand rhs, final int rhi) {

		value.assignOrRaw(index, rhs, rhi);
	}

	//@Override
	public void assignXorRaw(final int index, final Operand rhs, final int rhi) {

		value.assignXorRaw(index, rhs, rhi);
	}

	//@Override
	public void assignSrRaw(final int index, final Operand rhs, final int rhi) {

		value.assignSrRaw(index, rhs, rhi);
	}

	//@Override
	public void assignSlRaw(final int index, final Operand rhs, final int rhi) {

		value.assignSlRaw(index, rhs, rhi);
	}

	//@Override
	public Operand eqRaw(final int index, final Operand rhs, final int rhi) {

		return value.eqRaw(index, rhs, rhi);
	}

	//@Override
	public Operand neRaw(final int index, final Operand rhs, final int rhi) {

		return value.neRaw(index, rhs, rhi);
	}

	//@Override
	public Operand gtRaw(final int index, final Operand rhs, final int rhi) {

		return value.gtRaw(index, rhs, rhi);
	}

	//@Override
	public Operand ltRaw(final int index, final Operand rhs, final int rhi) {

		return value.ltRaw(index, rhs, rhi);
	}

	//@Override
	public Operand geRaw(final int index, final Operand rhs, final int rhi) {

		return value.geRaw(index, rhs, rhi);
	}

	//@Override
	public Operand leRaw(final int index, final Operand rhs, final int rhi) {

		return value.leRaw(index, rhs, rhi);
	}

	//@Override
	public Operand addRaw(final int index, final Operand rhs, final int rhi) {

		return value.addRaw(index, rhs, rhi);
	}

	//@Override
	public Operand subRaw(final int index, final Operand rhs, final int rhi) {

		return value.subRaw(index, rhs, rhi);
	}

	//@Override
	public Operand mulRaw(final int index, final Operand rhs, final int rhi) {

		return value.mulRaw(index, rhs, rhi);
	}

	//@Override
	public Operand divRaw(final int index, final Operand rhs, final int rhi) {

		return value.divRaw(index, rhs, rhi);
	}

	//@Override
	public Operand modRaw(final int index, final Operand rhs, final int rhi) {

		return value.modRaw(index, rhs, rhi);
	}

	//@Override
	public Operand andRaw(final int index, final Operand rhs, final int rhi) {

		return value.andRaw(index, rhs, rhi);
	}

	//@Override
	public Operand orRaw(final int index, final Operand rhs, final int rhi) {

		return value.orRaw(index, rhs, rhi);
	}

	//@Override
	public Operand xorRaw(final int index, final Operand rhs, final int rhi) {

		return value.xorRaw(index, rhs, rhi);
	}

	//@Override
	public Operand slRaw(final int index, final Operand rhs, final int rhi) {

		return value.slRaw(index, rhs, rhi);
	}

	//@Override
	public Operand srRaw(final int index, final Operand rhs, final int rhi) {

		return value.srRaw(index, rhs, rhi);
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
    public void assignNeRaw(int index, Operand rhs, int rhi) {
        value.assignNeRaw(index, rhs, rhi);
    }

    @Override
    public void assignGtRaw(int index, Operand rhs, int rhi) {
        value.assignGtRaw(index, rhs, rhi);
    }

    @Override
    public void assignLtRaw(int index, Operand rhs, int rhi) {
        value.assignLtRaw(index, rhs, rhi);
    }

    @Override
    public void assignGtEqRaw(int index, Operand rhs, int rhi) {
        value.assignGtEqRaw(index, rhs, rhi);
    }

    @Override
    public void assignLtEqRaw(int index, Operand rhs, int rhi) {
        value.assignLtEqRaw(index, rhs, rhi);
    }

}
