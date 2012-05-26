/*
 * $Id: Operand.java,v 1.2.4.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.variable;

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

	/**
	 * �f�[�^�̌^���擾����B
	 * 
	 * @return �f�[�^�̌^�B
	 */
	public abstract int getType();

	/**
	 * �z��̈ꎟ���ڂ̗v�f�����擾����B
	 * 
	 * @return �z��̈ꎟ���ڂ̗v�f���B
	 */
	public abstract int l0();

	/**
	 * �z��̓񎟌��ڂ̗v�f�����擾����B
	 * 
	 * @return �z��̓񎟌��ڂ̗v�f���B
	 */
	public abstract int l1();

	/**
	 * �z��̎O�����ڂ̗v�f�����擾����B
	 * 
	 * @return �z��̎O�����ڂ̗v�f���B
	 */
	public abstract int l2();

	/**
	 * �z��̎l�����ڂ̗v�f�����擾����B
	 * 
	 * @return �z��̎l�����ڂ̗v�f���B
	 */
	public abstract int l3();

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
	public abstract int getIndex(final int i0, final int i1, final int i2,
			final int i3);

	/**
	 * ���̃I�u�W�F�N�g�̕�����l���擾����B
	 * <p>
	 * ���̃I�u�W�F�N�g�̌^�������񂶂�Ȃ��Ƃ��͕ϊ����s����B
	 * </p>
	 * 
	 * @return �I�u�W�F�N�g�̕�����l�B
	 */
	//@Override
	public final String toString() {

		return toString(0);
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
	public abstract String toString(final int index);

	/**
	 * ���̃I�u�W�F�N�g�̕�����l���擾����B
	 * <p>
	 * ���̃I�u�W�F�N�g�̌^�������񂶂�Ȃ��Ƃ��͕ϊ����s����B
	 * </p>
	 * 
	 * @param index ������l���擾����v�f�ԍ��B
	 * @return �I�u�W�F�N�g�̕�����l�B
	 */
	public abstract ByteString toByteString(final int index);

	/**
	 * ���̃I�u�W�F�N�g�̐����l���擾����B
	 * <p>
	 * ���̃I�u�W�F�N�g�̌^����������Ȃ��Ƃ��͕ϊ����s����B
	 * </p>
	 * 
	 * @param index �����l���擾����v�f�ԍ��B
	 * @return �I�u�W�F�N�g�̐����l�B
	 */
	public abstract int toInt(final int index);

	/**
	 * ���̃I�u�W�F�N�g�̏����l���擾����B
	 * <p>
	 * ���̃I�u�W�F�N�g�̌^����������Ȃ��Ƃ��͕ϊ����s����B
	 * </p>
	 * 
	 * @param index �����l���擾����v�f�ԍ��B
	 * @return �I�u�W�F�N�g�̏����l�B
	 */
	public abstract double toDouble(final int index);

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
	public abstract Operand dup(final int index);

	/**
	 * �I�u�W�F�N�g�̒l���C���N�������g����B
	 * 
	 * @param index �C���N�������g����v�f�ԍ��B
	 */
	public abstract void inc(final int index);

	/**
	 * �I�u�W�F�N�g�̒l���f�N�������g����B
	 * 
	 * @param index �f�N�������g����v�f�ԍ��B
	 */
	public abstract void dec(final int index);

        /*
         * "assignNe", "assignGt", "assignLt", "assignGtEq", "assignLtEq",
         */
        /**
         * assignNe - assign Not Equal
         */
        public abstract void assignNe(final int index, final Operand rhs, final int rhi);
        /**
         * assignNe - assign Greater Than
         */
        public abstract void assignGt(final int index, final Operand rhs, final int rhi);
        /**
         * assignNe - assign Less Than
         */
        public abstract void assignLt(final int index, final Operand rhs, final int rhi);
        /**
         * assignNe - assign Greater Than or Equal
         */
        public abstract void assignGtEq(final int index, final Operand rhs, final int rhi);
        /**
         * assignNe - assign Less Than or Equal
         */
        public abstract void assignLtEq(final int index, final Operand rhs, final int rhi);
	/**
	 * ������s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assign(final int index, final Operand rhs,
			final int rhi);

	/**
	 * ���Z������s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assignAdd(final int index, final Operand rhs,
			final int rhi);

	/**
	 * ���Z������s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assignSub(final int index, final Operand rhs,
			final int rhi);

	/**
	 * ��Z������s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assignMul(final int index, final Operand rhs,
			final int rhi);

	/**
	 * ���Z������s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assignDiv(final int index, final Operand rhs,
			final int rhi);

	/**
	 * ��]������s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assignMod(final int index, final Operand rhs,
			final int rhi);

	/**
	 * �r�b�g�_���ϑ�����s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assignAnd(final int index, final Operand rhs,
			final int rhi);

	/**
	 * �r�b�g�_���a������s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assignOr(final int index, final Operand rhs,
			final int rhi);

	/**
	 * �r�b�g�r���_���a������s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assignXor(final int index, final Operand rhs,
			final int rhi);

	/**
	 * �E�r�b�g�V�t�g������s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assignSr(final int index, final Operand rhs,
			final int rhi);

	/**
	 * ���r�b�g�V�t�g������s���B
	 * 
	 * @param index �����v�f�ԍ��B
	 * @param rhs ������I�u�W�F�N�g�B
	 * @param rhi ������v�f�ԍ��B
	 */
	public abstract void assignSl(final int index, final Operand rhs,
			final int rhi);

	/**
	 * ���Z���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand add(final int index, final Operand rhs,
			final int rhi);

	/**
	 * ���Z���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand sub(final int index, final Operand rhs,
			final int rhi);

	/**
	 * ��Z���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand mul(final int index, final Operand rhs,
			final int rhi);

	/**
	 * ���Z���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand div(final int index, final Operand rhs,
			final int rhi);

	/**
	 * ��]�����߂�B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand mod(final int index, final Operand rhs,
			final int rhi);

	/**
	 * �r�b�g�_���ς����߂�B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand and(final int index, final Operand rhs,
			final int rhi);

	/**
	 * �r�b�g�_���a�����߂�B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand or(final int index, final Operand rhs, final int rhi);

	/**
	 * �r�b�g�r���_���a�����߂�B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand xor(final int index, final Operand rhs,
			final int rhi);

	/**
	 * �E�r�b�g�V�t�g���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand sl(final int index, final Operand rhs, final int rhi);

	/**
	 * ���r�b�g�V�t�g���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand sr(final int index, final Operand rhs, final int rhi);

	/**
	 * ==��r���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand eq(final int index, final Operand rhs, final int rhi);

	/**
	 * !=��r���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand ne(final int index, final Operand rhs, final int rhi);

	/**
	 * &gt;��r���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand gt(final int index, final Operand rhs, final int rhi);

	/**
	 * &lt;��r���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand lt(final int index, final Operand rhs, final int rhi);

	/**
	 * &gt;=��r���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand ge(final int index, final Operand rhs, final int rhi);

	/**
	 * &lt;=��r���s���B
	 * 
	 * @param index �v�f�ԍ��B
	 * @param rhs �E�I�y�����h�B
	 * @param rhi �E�I�y�����h�̗v�f�ԍ��B
	 * @return ���Z���ʁB
	 */
	public abstract Operand le(final int index, final Operand rhs, final int rhi);

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
