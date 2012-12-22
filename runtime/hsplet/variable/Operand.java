/*
 * $Id: Operand.java,v 1.2.4.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.variable;
import hsplet.Context;
import hsplet.HSPError;

import java.io.Serializable;

/**
 * HSP �̃I�y�����h������킷���N���X�B
 * <p>
 * HSP �Ŏg�p����ϐ��E�萔�E���Z���ʂ͂��ׂĂ��̃N���X���p������B
 * </p>
 * �I�y�����h�͎��
 * <ul>
 * <li>�z��̃C���f�b�N�X����I�t�Z�b�g���擾���郁�\�b�h�B</li>
 * <li>Java �̃v���~�e�B�u�╶����ɕϊ����郁�\�b�h�B</li>
 * <li>����E�ύX�n���Z�q���\�b�h�B</li>
 * <li>�񍀉��Z�q���\�b�h�B</li>
 * <li>�f�[�^�̃o�C�g�ւ̒��ڃA�N�Z�X���\�b�h�B</li>
 * </ul>
 * ���琬��B
 * <p>
 * �قƂ�ǂ̃��\�b�h�� abstract �Ȃ̂ŁA�p�������N���X�͓K�؂ɂ������������B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.2.4.1 $, $Date: 2006/08/02 12:13:06 $
 */
public abstract class Operand implements Serializable {

	/** Hackish way to have all Operands be able to refer to context.
	 *  This is initialized in Applet.init(Class) */
	public static Context context;

        protected void error(int error, String command) {
            getContext().error(error, command);
        }

        protected void error(int error, String command, String message) {
            getContext().error(error, command, message);
        }
        
        protected Context getContext() {
            return context;
        }
        
        protected static final Scalar UNITY = Scalar.fromValue(1);

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: Operand.java,v 1.2.4.1 2006/08/02 12:13:06 Yuki Exp $";
	protected static int allErrorIndex=1;
	protected int errorIndex=0;
	public int errorIndex(){
		if(errorIndex==0) errorIndex=allErrorIndex++;
		return errorIndex;
	}

	/**
	 * �f�[�^�̌^�B
	 * 
	 * @author Yuki
	 */
	public static final class Type {

		/** �f�[�^�̌^�A���g�p�B */
		public static final int UNKNOWN = 0;

		/** �f�[�^�̌^�A���x����\���萔�B */
		public static final int LABEL = 1;

		/** �f�[�^�̌^�A�������\���萔�B */
		public static final int STRING = 2;

		/** �f�[�^�̌^�A������\���萔�B */
		public static final int DOUBLE = 3;

		/** �f�[�^�̌^�A������\���萔�B */
		public static final int INTEGER = 4;

		/** �f�[�^�̌^�A�p�r�͕s���B */
		public static final int MODULE = 5;
	}
	public static int[] typeSizes = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 8, 4, Integer.MAX_VALUE};

	/**
	 * �f�[�^�̌^���擾����B
	 * 
	 * @return �f�[�^�̌^�B
	 */
	public abstract int getType();
	
	public Operand checkVar() {
		context.error(HSPError.VariableNameNotSpecified, "", "Variable required.");
		return this;
	}

	/**
	 * �z��̈ꎟ���ڂ̗v�f�����擾����B
	 * 
	 * @return �z��̈ꎟ���ڂ̗v�f���B
	 */
	public abstract int l0();
	public abstract int checkSize0(int size);
	public abstract int checkResize0(int size);
	public abstract void checkIncrementSize(int size);

	/**
	 * �z��̓񎟌��ڂ̗v�f�����擾����B
	 * 
	 * @return �z��̓񎟌��ڂ̗v�f���B
	 */
	public abstract int l1();
	public abstract int checkSize1(int size);
	public abstract int checkResize1(int size);

	/**
	 * �z��̎O�����ڂ̗v�f�����擾����B
	 * 
	 * @return �z��̎O�����ڂ̗v�f���B
	 */
	public abstract int l2();
	public abstract int checkSize2(int size);
	public abstract int checkResize2(int size);

	/**
	 * �z��̎l�����ڂ̗v�f�����擾����B
	 * 
	 * @return �z��̎l�����ڂ̗v�f���B
	 */
	public abstract int l3();
	public abstract int checkSize3(int size);
	public abstract int checkResize3(int size);

	public abstract int getIndex(final int i0);
	public abstract int getResizeIndex(final int i0);
	/**
	 * �񎟌��z��́A�ꎟ���Ɋ��Z�����v�f�̃C���f�b�N�X���擾����B
	 * <p>
	 * HSPLet 3.0 �ł͍������̂��߂ɔz��͂��ׂĈꎟ���ŊǗ������B �񎟌��ȏ�̃C���f�b�N�X�͗v�f�������Ɉꎟ���Ɋ��Z���Ă��珈�������B
	 * </p>
	 * 
	 * @param i0 �ꎟ���ڂ̃C���f�b�N�X�B
	 * @param i1 �񎟌��ڂ̃C���f�b�N�X�B
	 * @return �ꎟ���Ɋ��Z�����v�f�ԍ��B
	 */
	public abstract int getIndex(final int i0, final int i1);
	public abstract int getResizeIndex(final int i0, final int i1);

	/**
	 * �O�����z��́A�ꎟ���Ɋ��Z�����v�f�̃C���f�b�N�X���擾����B
	 * <p>
	 * HSPLet 3.0 �ł͍������̂��߂ɔz��͂��ׂĈꎟ���ŊǗ������B �񎟌��ȏ�̃C���f�b�N�X�͗v�f�������Ɉꎟ���Ɋ��Z���Ă��珈�������B
	 * </p>
	 * 
	 * @param i0 �ꎟ���ڂ̃C���f�b�N�X�B
	 * @param i1 �񎟌��ڂ̃C���f�b�N�X�B
	 * @param i2 �O�����ڂ̃C���f�b�N�X�B
	 * @return �ꎟ���Ɋ��Z�����v�f�ԍ��B
	 */
	public abstract int getIndex(final int i0, final int i1, final int i2);
	public abstract int getResizeIndex(final int i0, final int i1, final int i2);

	/**
	 * �l�����z��́A�ꎟ���Ɋ��Z�����v�f�̃C���f�b�N�X���擾����B
	 * <p>
	 * HSPLet 3.0 �ł͍������̂��߂ɔz��͂��ׂĈꎟ���ŊǗ������B �񎟌��ȏ�̃C���f�b�N�X�͗v�f�������Ɉꎟ���Ɋ��Z���Ă��珈�������B
	 * </p>
	 * 
	 * @param i0 �ꎟ���ڂ̃C���f�b�N�X�B
	 * @param i1 �񎟌��ڂ̃C���f�b�N�X�B
	 * @param i2 �O�����ڂ̃C���f�b�N�X�B
	 * @param i3 �l�����ڂ̃C���f�b�N�X�B
	 * @return �ꎟ���Ɋ��Z�����v�f�ԍ��B
	 */
	public abstract int getIndex(final int i0, final int i1, final int i2, final int i3);
	public abstract int getResizeIndex(final int i0, final int i1, final int i2, final int i3);

	/**
	 * ���̃I�u�W�F�N�g�̕�����l���擾����B
	 * <p>
	 * ���̃I�u�W�F�N�g�̌^�������񂶂�Ȃ��Ƃ��͕ϊ����s����B
	 * </p>
	 * 
	 * @return �I�u�W�F�N�g�̕�����l�B
	 */
	//@Override
	public String toString() {
		return toStringRaw(0);
	}

	/**
	 * ���̃I�u�W�F�N�g�̕�����l���擾����B
	 * <p>
	 * ���̃I�u�W�F�N�g�̌^�������񂶂�Ȃ��Ƃ��͕ϊ����s����B
	 * </p>
	 * 
	 * @param index ������l���擾����v�f�ԍ��B
	 * @return �I�u�W�F�N�g�̕�����l�B
	 */
	public abstract String toStringRaw(final int index);
	public String toString(final int i0) {
		return toStringRaw(getIndex(i0));
	}
	public String toString(final int i0, final int i1) {
		return toStringRaw(getIndex(i0, i1));
	}
	public String toString(final int i0, final int i1, final int i2) {
		return toStringRaw(getIndex(i0, i1, i2));
	}
	public String toString(final int i0, final int i1, final int i2, final int i3) {
		return toStringRaw(getIndex(i0, i1, i2, i3));
	}

	/**
	 * ���̃I�u�W�F�N�g�̕�����l���擾����B
	 * <p>
	 * ���̃I�u�W�F�N�g�̌^�������񂶂�Ȃ��Ƃ��͕ϊ����s����B
	 * </p>
	 * 
	 * @param index ������l���擾����v�f�ԍ��B
	 * @return �I�u�W�F�N�g�̕�����l�B
	 */
	public abstract ByteString toByteStringRaw(final int index);
	public ByteString toByteString() {
		return toByteStringRaw(0);
	}
	public ByteString toByteString(final int i0) {
		return toByteStringRaw(getIndex(i0));
	}
	public ByteString toByteString(final int i0, final int i1) {
		return toByteStringRaw(getIndex(i0, i1));
	}
	public ByteString toByteString(final int i0, final int i1, final int i2) {
		return toByteStringRaw(getIndex(i0, i1, i2));
	}
	public ByteString toByteString(final int i0, final int i1, final int i2, final int i3) {
		return toByteStringRaw(getIndex(i0, i1, i2, i3));
	}

	/**
	 * ���̃I�u�W�F�N�g�̐����l���擾����B
	 * <p>
	 * ���̃I�u�W�F�N�g�̌^����������Ȃ��Ƃ��͕ϊ����s����B
	 * </p>
	 * 
	 * @param index �����l���擾����v�f�ԍ��B
	 * @return �I�u�W�F�N�g�̐����l�B
	 */
	public abstract int toIntRaw(final int index);
	public int toInt() {
		return toIntRaw(0);
	}
	public int toInt(final int i0) {
		return toIntRaw(getIndex(i0));
	}
	public int toInt(final int i0, final int i1) {
		return toIntRaw(getIndex(i0, i1));
	}
	public int toInt(final int i0, final int i1, final int i2) {
		return toIntRaw(getIndex(i0, i1, i2));
	}
	public int toInt(final int i0, final int i1, final int i2, final int i3) {
		return toIntRaw(getIndex(i0, i1, i2, i3));
	}

	/**
	 * ���̃I�u�W�F�N�g�̏����l���擾����B
	 * <p>
	 * ���̃I�u�W�F�N�g�̌^����������Ȃ��Ƃ��͕ϊ����s����B
	 * </p>
	 * 
	 * @param index �����l���擾����v�f�ԍ��B
	 * @return �I�u�W�F�N�g�̏����l�B
	 */
	public abstract double toDoubleRaw(final int index);
	public double toDouble() {
		return toDoubleRaw(0);
	}
	public double toDouble(final int i0) {
		return toDoubleRaw(getIndex(i0));
	}
	public double toDouble(final int i0, final int i1) {
		return toDoubleRaw(getIndex(i0, i1));
	}
	public double toDouble(final int i0, final int i1, final int i2) {
		return toDoubleRaw(getIndex(i0, i1, i2));
	}
	public double toDouble(final int i0, final int i1, final int i2, final int i3) {
		return toDoubleRaw(getIndex(i0, i1, i2, i3));
	}

	/**
	 * �I�u�W�F�N�g�ւ̎Q�Ƃ��擾����B
	 * <p>
	 * ���̃��\�b�h����Ԃ����I�u�W�F�N�g�͂��̃I�u�W�F�N�g�̎w�肳�ꂽ�v�f���Q�Ƃ��Ă���B �Q�ƃI�u�W�F�N�g�̒l��ύX������A���̃I�u�W�F�N�g�̒l��ύX����Ƃ��݂��ɔ��f�����B
	 * </p>
	 * <p>
	 * �����̒l�ɂ���Ă͂��̃I�u�W�F�N�g���g���Ԃ����\��������B
	 * </p>
	 * 
	 * @param index �v�f�ԍ��B
	 * @return ���̃I�u�W�F�N�g�ւ̎Q�ƁB
	 */
	public Operand ref(final int index) {

		if (index == 0) {
			return this;
		} else {

			return new Reference(this, index);
		}
	}
	
	/**
	 * �I�u�W�F�N�g�̕������擾����B
	 * @param index ���������悤���̃C���f�b�N�X�B
	 * @return ���̃I�u�W�F�N�g�̎w�肳�ꂽ�v�f��ێ�����I�u�W�F�N�g�B
	 */
	public abstract Operand dupRaw(final int index);
	public Operand dup() {
		return dupRaw(0);
	}
	public Operand dup(final int i0) {
		return dupRaw(getIndex(i0));
	}
	public Operand dup(final int i0, final int i1) {
		return dupRaw(getIndex(i0, i1));
	}
	public Operand dup(final int i0, final int i1, final int i2) {
		return dupRaw(getIndex(i0, i1, i2));
	}
	public Operand dup(final int i0, final int i1, final int i2, final int i3) {
		return dupRaw(getIndex(i0, i1, i2, i3));
	}

	/**
	 * �I�u�W�F�N�g�̒l���C���N�������g����B
	 * 
	 * @param index �C���N�������g����v�f�ԍ��B
	 */
	public abstract void incRaw(final int index);
	public void inc() {
		incRaw(0);
	}
	public void inc(final int i0) {
		incRaw(getIndex(i0));
	}
	public void inc(final int i0, final int i1) {
		incRaw(getIndex(i0, i1));
	}
	public void inc(final int i0, final int i1, final int i2) {
		incRaw(getIndex(i0, i1, i2));
	}
	public void inc(final int i0, final int i1, final int i2, final int i3) {
		incRaw(getIndex(i0, i1, i2, i3));
	}

	/**
	 * �I�u�W�F�N�g�̒l���f�N�������g����B
	 * 
	 * @param index �f�N�������g����v�f�ԍ��B
	 */
	public abstract void decRaw(final int index);
	public void dec() {
		decRaw(0);
	}
	public void dec(final int i0) {
		decRaw(getIndex(i0));
	}
	public void dec(final int i0, final int i1) {
		decRaw(getIndex(i0, i1));
	}
	public void dec(final int i0, final int i1, final int i2) {
		decRaw(getIndex(i0, i1, i2));
	}
	public void dec(final int i0, final int i1, final int i2, final int i3) {
		decRaw(getIndex(i0, i1, i2, i3));
	}

    /*
     * "assignNe", "assignGt", "assignLt", "assignGtEq", "assignLtEq",
     */
    /**
     * assignNe - assign Not Equal
     */
    public abstract void assignNeRaw(final int index, final Operand rhs, final int rhi);
    public void assignNeRaw(final int index, final Operand rhs) {
	    assignNeRaw(index, rhs, 0);
    }
	public void assignNe(final Operand rhs) {
		assignNeRaw(0, rhs, 0);
	}
	public void assignNe(final Operand rhs, final int rhi) {
		assignNeRaw(0, rhs, rhi);
	}
	public void assignNe(final int i0, final Operand rhs, final int rhi) {
		assignNeRaw(getIndex(i0), rhs, rhi);
	}
	public void assignNe(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignNeRaw(getIndex(i0, i1), rhs, rhi);
	}
	public void assignNe(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignNeRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignNe(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignNeRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}
    /**
     * assignNe - assign Greater Than
     */
    public abstract void assignGtRaw(final int index, final Operand rhs, final int rhi);
    public void assignGtRaw(final int index, final Operand rhs) {
	    assignGtRaw(index, rhs, 0);
    }
	public void assignGt(final Operand rhs) {
		assignGtRaw(0, rhs, 0);
	}
	public void assignGt(final Operand rhs, final int rhi) {
		assignGtRaw(0, rhs, rhi);
	}
	public void assignGt(final int i0, final Operand rhs, final int rhi) {
		assignGtRaw(getIndex(i0), rhs, rhi);
	}
	public void assignGt(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignGtRaw(getIndex(i0, i1), rhs, rhi);
	}
	public void assignGt(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignGtRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignGt(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignGtRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}
    /**
     * assignNe - assign Less Than
     */
    public abstract void assignLtRaw(final int index, final Operand rhs, final int rhi);
    public void assignLtRaw(final int index, final Operand rhs) {
	    assignLtRaw(index, rhs, 0);
    }
	public void assignLt(final Operand rhs) {
		assignLtRaw(0, rhs, 0);
	}
	public void assignLt(final Operand rhs, final int rhi) {
		assignLtRaw(0, rhs, rhi);
	}
	public void assignLt(final int i0, final Operand rhs, final int rhi) {
		assignLtRaw(getIndex(i0), rhs, rhi);
	}
	public void assignLt(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignLtRaw(getIndex(i0, i1), rhs, rhi);
	}
	public void assignLt(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignLtRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignLt(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignLtRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}
    /**
     * assignNe - assign Greater Than or Equal
     */
    public abstract void assignGtEqRaw(final int index, final Operand rhs, final int rhi);
    public void assignGtEqRaw(final int index, final Operand rhs) {
	    assignGtEqRaw(index, rhs, 0);
    }
	public void assignGtEq(final Operand rhs) {
		assignGtEqRaw(0, rhs, 0);
	}
	public void assignGtEq(final Operand rhs, final int rhi) {
		assignGtEqRaw(0, rhs, rhi);
	}
	public void assignGtEq(final int i0, final Operand rhs, final int rhi) {
		assignGtEqRaw(getIndex(i0), rhs, rhi);
	}
	public void assignGtEq(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignGtEqRaw(getIndex(i0, i1), rhs, rhi);
	}
	public void assignGtEq(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignGtEqRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignGtEq(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignGtEqRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}
    /**
     * assignNe - assign Less Than or Equal
     */
    public abstract void assignLtEqRaw(final int index, final Operand rhs, final int rhi);
    public void assignLtEqRaw(final int index, final Operand rhs) {
	    assignLtEqRaw(index, rhs, 0);
    }
	public void assignLtEq(final Operand rhs) {
		assignLtEqRaw(0, rhs, 0);
	}
	public void assignLtEq(final Operand rhs, final int rhi) {
		assignLtEqRaw(0, rhs, rhi);
	}
	public void assignLtEq(final int i0, final Operand rhs, final int rhi) {
		assignLtEqRaw(getIndex(i0), rhs, rhi);
	}
	public void assignLtEq(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignLtEqRaw(getIndex(i0, i1), rhs, rhi);
	}
	public void assignLtEq(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignLtEqRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignLtEq(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignLtEqRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}
	/**
	 * ������s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assignRaw(final int index, final int newValue);
	public void assign(final int newValue) {
		assignRaw(0, newValue);
	}
	public void assign(final int i0, final int newValue) {
		assignRaw(getResizeIndex(i0), newValue);
	}
	public void assign(final int i0, final int i1, final int newValue) {
		assignRaw(getResizeIndex(i0, i1), newValue);
	}
	public void assign(final int i0, final int i1, final int i2, final int newValue) {
		assignRaw(getResizeIndex(i0, i1, i2), newValue);
	}
	public void assign(final int i0, final int i1, final int i2, final int i3, final int newValue) {
		assignRaw(getResizeIndex(i0, i1, i2, i3), newValue);
	}
	public abstract void assignRaw(final int index, final double newValue);
	public void assign(final double newValue) {
		assignRaw(0, newValue);
	}
	public void assign(final int i0, final double newValue) {
		assignRaw(getResizeIndex(i0), newValue);
	}
	public void assign(final int i0, final int i1, final double newValue) {
		assignRaw(getResizeIndex(i0, i1), newValue);
	}
	public void assign(final int i0, final int i1, final int i2, final double newValue) {
		assignRaw(getResizeIndex(i0, i1, i2), newValue);
	}
	public void assign(final int i0, final int i1, final int i2, final int i3, final double newValue) {
		assignRaw(getResizeIndex(i0, i1, i2, i3), newValue);
	}
	public abstract void assignRaw(final int index, final String newValue);
	public void assign(final String newValue) {
		assignRaw(0, newValue);
	}
	public void assign(final int i0, final String newValue) {
		assignRaw(getResizeIndex(i0), newValue);
	}
	public void assign(final int i0, final int i1, final String newValue) {
		assignRaw(getResizeIndex(i0, i1), newValue);
	}
	public void assign(final int i0, final int i1, final int i2, final String newValue) {
		assignRaw(getResizeIndex(i0, i1, i2), newValue);
	}
	public void assign(final int i0, final int i1, final int i2, final int i3, final String newValue) {
		assignRaw(getResizeIndex(i0, i1, i2, i3), newValue);
	}

	public abstract void assignRaw(final int index, final Operand rhs, final int rhi);
	public void assignRaw(final int index, final Operand rhs) {
		assignRaw(index, rhs, 0);
	}
	public void assign(final Operand rhs) {
		assignRaw(0, rhs, 0);
	}
	public void assign(final Operand rhs, final int rhi) {
		assignRaw(0, rhs, rhi);
	}
	public void assign(final int i0, final Operand rhs, final int rhi) {
		assignRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assign(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assign(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assign(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * ���Z������s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assignAddRaw(final int index, final Operand rhs, final int rhi);
    public void assignAddRaw(final int index, final Operand rhs) {
	    assignAddRaw(index, rhs, 0);
    }
	public void assignAdd(final Operand rhs) {
		assignAddRaw(0, rhs, 0);
	}
	public void assignAdd(final Operand rhs, final int rhi) {
		assignAddRaw(0, rhs, rhi);
	}
	public void assignAdd(final int i0, final Operand rhs, final int rhi) {
		assignAddRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assignAdd(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignAddRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assignAdd(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignAddRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignAdd(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignAddRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * ���Z������s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assignSubRaw(final int index, final Operand rhs, final int rhi);
    public void assignSubRaw(final int index, final Operand rhs) {
	    assignSubRaw(index, rhs, 0);
    }
	public void assignSub(final Operand rhs) {
		assignSubRaw(0, rhs, 0);
	}
	public void assignSub(final Operand rhs, final int rhi) {
		assignSubRaw(0, rhs, rhi);
	}
	public void assignSub(final int i0, final Operand rhs, final int rhi) {
		assignSubRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assignSub(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignSubRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assignSub(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignSubRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignSub(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignSubRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * ��Z������s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assignMulRaw(final int index, final Operand rhs, final int rhi);
    public void assignMulRaw(final int index, final Operand rhs) {
	    assignMulRaw(index, rhs, 0);
    }
	public void assignMul(final Operand rhs) {
		assignMulRaw(0, rhs, 0);
	}
	public void assignMul(final Operand rhs, final int rhi) {
		assignMulRaw(0, rhs, rhi);
	}
	public void assignMul(final int i0, final Operand rhs, final int rhi) {
		assignMulRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assignMul(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignMulRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assignMul(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignMulRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignMul(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignMulRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * ���Z������s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assignDivRaw(final int index, final Operand rhs, final int rhi);
    public void assignDivRaw(final int index, final Operand rhs) {
	    assignDivRaw(index, rhs, 0);
    }
	public void assignDiv(final Operand rhs) {
		assignDivRaw(0, rhs, 0);
	}
	public void assignDiv(final Operand rhs, final int rhi) {
		assignDivRaw(0, rhs, rhi);
	}
	public void assignDiv(final int i0, final Operand rhs, final int rhi) {
		assignDivRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assignDiv(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignDivRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assignDiv(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignDivRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignDiv(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignDivRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * ��]������s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assignModRaw(final int index, final Operand rhs, final int rhi);
    public void assignModRaw(final int index, final Operand rhs) {
	    assignModRaw(index, rhs, 0);
    }
	public void assignMod(final Operand rhs) {
		assignModRaw(0, rhs, 0);
	}
	public void assignMod(final Operand rhs, final int rhi) {
		assignModRaw(0, rhs, rhi);
	}
	public void assignMod(final int i0, final Operand rhs, final int rhi) {
		assignModRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assignMod(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignModRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assignMod(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignModRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignMod(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignModRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * �r�b�g�_���ϑ�����s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assignAndRaw(final int index, final Operand rhs, final int rhi);
    public void assignAndRaw(final int index, final Operand rhs) {
	    assignAndRaw(index, rhs, 0);
    }
	public void assignAnd(final Operand rhs) {
		assignAndRaw(0, rhs, 0);
	}
	public void assignAnd(final Operand rhs, final int rhi) {
		assignAndRaw(0, rhs, rhi);
	}
	public void assignAnd(final int i0, final Operand rhs, final int rhi) {
		assignAndRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assignAnd(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignAndRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assignAnd(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignAndRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignAnd(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignAndRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * �r�b�g�_���a������s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assignOrRaw(final int index, final Operand rhs, final int rhi);
    public void assignOrRaw(final int index, final Operand rhs) {
	    assignOrRaw(index, rhs, 0);
    }
	public void assignOr(final Operand rhs) {
		assignOrRaw(0, rhs, 0);
	}
	public void assignOr(final Operand rhs, final int rhi) {
		assignOrRaw(0, rhs, rhi);
	}
	public void assignOr(final int i0, final Operand rhs, final int rhi) {
		assignOrRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assignOr(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignOrRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assignOr(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignOrRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignOr(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignOrRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * �r�b�g�r���_���a������s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assignXorRaw(final int index, final Operand rhs, final int rhi);
    public void assignXorRaw(final int index, final Operand rhs) {
	    assignXorRaw(index, rhs, 0);
    }
	public void assignXor(final Operand rhs) {
		assignXorRaw(0, rhs, 0);
	}
	public void assignXor(final Operand rhs, final int rhi) {
		assignXorRaw(0, rhs, rhi);
	}
	public void assignXor(final int i0, final Operand rhs, final int rhi) {
		assignXorRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assignXor(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignXorRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assignXor(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignXorRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignXor(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignXorRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * �E�r�b�g�V�t�g������s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assignSrRaw(final int index, final Operand rhs, final int rhi);
    public void assignSrRaw(final int index, final Operand rhs) {
	    assignSrRaw(index, rhs, 0);
    }
	public void assignSr(final Operand rhs) {
		assignSrRaw(0, rhs, 0);
	}
	public void assignSr(final Operand rhs, final int rhi) {
		assignSrRaw(0, rhs, rhi);
	}
	public void assignSr(final int i0, final Operand rhs, final int rhi) {
		assignSrRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assignSr(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignSrRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assignSr(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignSrRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignSr(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignSrRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * ���r�b�g�V�t�g������s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assignSlRaw(final int index, final Operand rhs, final int rhi);
    public void assignSlRaw(final int index, final Operand rhs) {
	    assignSlRaw(index, rhs, 0);
    }
	public void assignSl(final Operand rhs) {
		assignSlRaw(0, rhs, 0);
	}
	public void assignSl(final Operand rhs, final int rhi) {
		assignSlRaw(0, rhs, rhi);
	}
	public void assignSl(final int i0, final Operand rhs, final int rhi) {
		assignSlRaw(getResizeIndex(i0), rhs, rhi);
	}
	public void assignSl(final int i0, final int i1, final Operand rhs, final int rhi) {
		assignSlRaw(getResizeIndex(i0, i1), rhs, rhi);
	}
	public void assignSl(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		assignSlRaw(getResizeIndex(i0, i1, i2), rhs, rhi);
	}
	public void assignSl(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		assignSlRaw(getResizeIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * ���Z���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand addRaw(final int index, final Operand rhs, final int rhi);
	public Operand add(final Operand rhs) {
		return addRaw(0, rhs, 0);
	}
	public Operand add(final Operand rhs, final int rhi) {
		return addRaw(0, rhs, rhi);
	}
	public Operand add(final int i0, final Operand rhs, final int rhi) {
		return addRaw(getIndex(i0), rhs, rhi);
	}
	public Operand add(final int i0, final int i1, final Operand rhs, final int rhi) {
		return addRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand add(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return addRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand add(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return addRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * ���Z���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand subRaw(final int index, final Operand rhs, final int rhi);
	public Operand sub(final Operand rhs) {
		return subRaw(0, rhs, 0);
	}
	public Operand sub(final Operand rhs, final int rhi) {
		return subRaw(0, rhs, rhi);
	}
	public Operand sub(final int i0, final Operand rhs, final int rhi) {
		return subRaw(getIndex(i0), rhs, rhi);
	}
	public Operand sub(final int i0, final int i1, final Operand rhs, final int rhi) {
		return subRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand sub(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return subRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand sub(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return subRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * ��Z���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand mulRaw(final int index, final Operand rhs, final int rhi);
	public Operand mul(final Operand rhs) {
		return mulRaw(0, rhs, 0);
	}
	public Operand mul(final Operand rhs, final int rhi) {
		return mulRaw(0, rhs, rhi);
	}
	public Operand mul(final int i0, final Operand rhs, final int rhi) {
		return mulRaw(getIndex(i0), rhs, rhi);
	}
	public Operand mul(final int i0, final int i1, final Operand rhs, final int rhi) {
		return mulRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand mul(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return mulRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand mul(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return mulRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * ���Z���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand divRaw(final int index, final Operand rhs, final int rhi);
	public Operand div(final Operand rhs) {
		return divRaw(0, rhs, 0);
	}
	public Operand div(final Operand rhs, final int rhi) {
		return divRaw(0, rhs, rhi);
	}
	public Operand div(final int i0, final Operand rhs, final int rhi) {
		return divRaw(getIndex(i0), rhs, rhi);
	}
	public Operand div(final int i0, final int i1, final Operand rhs, final int rhi) {
		return divRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand div(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return divRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand div(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return divRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * ��]�����߂�B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand modRaw(final int index, final Operand rhs, final int rhi);
	public Operand mod(final Operand rhs) {
		return modRaw(0, rhs, 0);
	}
	public Operand mod(final Operand rhs, final int rhi) {
		return modRaw(0, rhs, rhi);
	}
	public Operand mod(final int i0, final Operand rhs, final int rhi) {
		return modRaw(getIndex(i0), rhs, rhi);
	}
	public Operand mod(final int i0, final int i1, final Operand rhs, final int rhi) {
		return modRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand mod(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return modRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand mod(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return modRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * �r�b�g�_���ς����߂�B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand andRaw(final int index, final Operand rhs, final int rhi);
	public Operand and(final Operand rhs) {
		return andRaw(0, rhs, 0);
	}
	public Operand and(final Operand rhs, final int rhi) {
		return andRaw(0, rhs, rhi);
	}
	public Operand and(final int i0, final Operand rhs, final int rhi) {
		return andRaw(getIndex(i0), rhs, rhi);
	}
	public Operand and(final int i0, final int i1, final Operand rhs, final int rhi) {
		return andRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand and(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return andRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand and(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return andRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * �r�b�g�_���a�����߂�B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand orRaw(final int index, final Operand rhs, final int rhi);
	public Operand or(final Operand rhs) {
		return orRaw(0, rhs, 0);
	}
	public Operand or(final Operand rhs, final int rhi) {
		return orRaw(0, rhs, rhi);
	}
	public Operand or(final int i0, final Operand rhs, final int rhi) {
		return orRaw(getIndex(i0), rhs, rhi);
	}
	public Operand or(final int i0, final int i1, final Operand rhs, final int rhi) {
		return orRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand or(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return orRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand or(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return orRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * �r�b�g�r���_���a�����߂�B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand xorRaw(final int index, final Operand rhs, final int rhi);
	public Operand xor(final Operand rhs) {
		return xorRaw(0, rhs, 0);
	}
	public Operand xor(final Operand rhs, final int rhi) {
		return xorRaw(0, rhs, rhi);
	}
	public Operand xor(final int i0, final Operand rhs, final int rhi) {
		return xorRaw(getIndex(i0), rhs, rhi);
	}
	public Operand xor(final int i0, final int i1, final Operand rhs, final int rhi) {
		return xorRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand xor(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return xorRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand xor(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return xorRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * �E�r�b�g�V�t�g���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand slRaw(final int index, final Operand rhs, final int rhi);
	public Operand sl(final Operand rhs) {
		return slRaw(0, rhs, 0);
	}
	public Operand sl(final Operand rhs, final int rhi) {
		return slRaw(0, rhs, rhi);
	}
	public Operand sl(final int i0, final Operand rhs, final int rhi) {
		return slRaw(getIndex(i0), rhs, rhi);
	}
	public Operand sl(final int i0, final int i1, final Operand rhs, final int rhi) {
		return slRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand sl(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return slRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand sl(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return slRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * ���r�b�g�V�t�g���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand srRaw(final int index, final Operand rhs, final int rhi);
	public Operand sr(final Operand rhs) {
		return srRaw(0, rhs, 0);
	}
	public Operand sr(final Operand rhs, final int rhi) {
		return srRaw(0, rhs, rhi);
	}
	public Operand sr(final int i0, final Operand rhs, final int rhi) {
		return srRaw(getIndex(i0), rhs, rhi);
	}
	public Operand sr(final int i0, final int i1, final Operand rhs, final int rhi) {
		return srRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand sr(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return srRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand sr(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return srRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * ==��r���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand eqRaw(final int index, final Operand rhs, final int rhi);
	public Operand eq(final Operand rhs) {
		return eqRaw(0, rhs, 0);
	}
	public Operand eq(final Operand rhs, final int rhi) {
		return eqRaw(0, rhs, rhi);
	}
	public Operand eq(final int i0, final Operand rhs, final int rhi) {
		return eqRaw(getIndex(i0), rhs, rhi);
	}
	public Operand eq(final int i0, final int i1, final Operand rhs, final int rhi) {
		return eqRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand eq(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return eqRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand eq(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return eqRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * !=��r���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand neRaw(final int index, final Operand rhs, final int rhi);
	public Operand ne(final Operand rhs) {
		return neRaw(0, rhs, 0);
	}
	public Operand ne(final Operand rhs, final int rhi) {
		return neRaw(0, rhs, rhi);
	}
	public Operand ne(final int i0, final Operand rhs, final int rhi) {
		return neRaw(getIndex(i0), rhs, rhi);
	}
	public Operand ne(final int i0, final int i1, final Operand rhs, final int rhi) {
		return neRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand ne(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return neRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand ne(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return neRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * &gt;��r���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand gtRaw(final int index, final Operand rhs, final int rhi);
	public Operand gt(final Operand rhs) {
		return gtRaw(0, rhs, 0);
	}
	public Operand gt(final Operand rhs, final int rhi) {
		return gtRaw(0, rhs, rhi);
	}
	public Operand gt(final int i0, final Operand rhs, final int rhi) {
		return gtRaw(getIndex(i0), rhs, rhi);
	}
	public Operand gt(final int i0, final int i1, final Operand rhs, final int rhi) {
		return gtRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand gt(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return gtRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand gt(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return gtRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * &lt;��r���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand ltRaw(final int index, final Operand rhs, final int rhi);
	public Operand lt(final Operand rhs) {
		return ltRaw(0, rhs, 0);
	}
	public Operand lt(final Operand rhs, final int rhi) {
		return ltRaw(0, rhs, rhi);
	}
	public Operand lt(final int i0, final Operand rhs, final int rhi) {
		return ltRaw(getIndex(i0), rhs, rhi);
	}
	public Operand lt(final int i0, final int i1, final Operand rhs, final int rhi) {
		return ltRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand lt(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return ltRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand lt(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return ltRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * &gt;=��r���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand geRaw(final int index, final Operand rhs, final int rhi);
	public Operand ge(final Operand rhs) {
		return geRaw(0, rhs, 0);
	}
	public Operand ge(final Operand rhs, final int rhi) {
		return geRaw(0, rhs, rhi);
	}
	public Operand ge(final int i0, final Operand rhs, final int rhi) {
		return geRaw(getIndex(i0), rhs, rhi);
	}
	public Operand ge(final int i0, final int i1, final Operand rhs, final int rhi) {
		return geRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand ge(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return geRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand ge(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return geRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * &lt;=��r���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand leRaw(final int index, final Operand rhs, final int rhi);
	public Operand le(final Operand rhs) {
		return leRaw(0, rhs, 0);
	}
	public Operand le(final Operand rhs, final int rhi) {
		return leRaw(0, rhs, rhi);
	}
	public Operand le(final int i0, final Operand rhs, final int rhi) {
		return leRaw(getIndex(i0), rhs, rhi);
	}
	public Operand le(final int i0, final int i1, final Operand rhs, final int rhi) {
		return leRaw(getIndex(i0, i1), rhs, rhi);
	}
	public Operand le(final int i0, final int i1, final int i2, final Operand rhs, final int rhi) {
		return leRaw(getIndex(i0, i1, i2), rhs, rhi);
	}
	public Operand le(final int i0, final int i1, final int i2, final int i3, final Operand rhs, final int rhi) {
		return leRaw(getIndex(i0, i1, i2, i3), rhs, rhi);
	}

	/**
	 * �I�u�W�F�N�g�̃o�C�g���擾����B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param offset �I�t�Z�b�g�B
	 * @return �I�u�W�F�N�g�̃o�C�g�l�B
	 */
	public abstract byte peek(int index, int offset);

	/**
	 * �I�u�W�F�N�g�̃o�C�g��ݒ肷��B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param offset �I�t�Z�b�g�B
	 * @param value �ݒ肷��l�B
	 */
	public abstract void poke(int index, int offset, byte value);

	/*
	 * TODO: Type-clipping.
	 * New method needed for every assign-operand group (int index, int type, Operand O)
	 * Call something like below on the Operand? More complicated than that though, math largely depends on type.
	 */
	/*
	public Operand clipToType(int vartype) {
		int myType=getType();
		done:
		if(vartype!=myType)
		{
			int clipTo=typeSizes[vartype];
			int mySize=typeSizes[myType];
			if(mySize <= clipTo) break done;
			
			switch(myType)
			{
				//TODO
			}
		}
		return this;
		
	}
	*/

	/**
	 * �T�|�[�g����Ȃ����Z�q���g�p���ꂽ�Ƃ��ɗ�O�𔭐�������B
	 * @param operator ���Z�q�B
	 * @return UnsupportedOperationException ������ׂ���O�B
	 */
	public UnsupportedOperationException unsupportedOperator(
			final String operator) {

		return new UnsupportedOperationException("���Z�q " + operator + " �͌^ "
				+ getType() + " �ɂ͎g�p�ł��܂���B");
	}
}
