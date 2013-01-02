/*
 * $Id: Scalar.java,v 1.3 2006/02/11 18:00:37 Yuki Exp $
 */
package hsplet.variable;

/**
 * �P��̗v�f���������I�y�����h������킷��{�N���X�B
 * <p>
 * Array �������̗v�f�����̂ɑ΂��āA���̃N���X�͈�����v�f�����B
 * </p>
 * <p>
 * �萔�≉�Z�̌��ʁA�֐��̖߂�l�ȂǁA�͂��߂���v�f��������Ȃ��Ƃ킩���Ă���Ƃ��́A�z���肱������g�p�����ق��������Ɏ��s�ł���B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.3 $, $Date: 2006/02/11 18:00:37 $
 */
public abstract class Scalar extends Operand {
    
	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: Scalar.java,v 1.3 2006/02/11 18:00:37 Yuki Exp $";

	private static final Scalar[] intCache = new Scalar[256];

	static {

		for (int i = 0; i < intCache.length; ++i) {
			intCache[i] = new IntScalar(i - 128);
		}
	}

        public static final Scalar UNITY = Scalar.fromValue(1);
	/**
	 * ����̒l������킷�I�u�W�F�N�g���擾����B
	 * <p>
	 * ���̃��\�b�h����Ԃ��ꂽ�I�u�W�F�N�g�̒l�͕ύX���Ă͂Ȃ�Ȃ��B
	 * </p>
	 * 
	 * @param rawValue ���̒l�B
	 * @return �w�肳�ꂽ�l��\���X�J���[�I�u�W�F�N�g�B
	 */
	public static Scalar fromValue(final String rawValue) {

		return new StringScalar(rawValue);
	}

	/**
	 * ����̒l������킷�I�u�W�F�N�g���擾����B
	 * <p>
	 * ���̃��\�b�h����Ԃ��ꂽ�I�u�W�F�N�g�̒l�͕ύX���Ă͂Ȃ�Ȃ��B
	 * </p>
	 * 
	 * @param rawValue ���̒l�B
	 * @return �w�肳�ꂽ�l��\���X�J���[�I�u�W�F�N�g�B
	 */
	public static Scalar fromValue(final ByteString rawValue) {

		return new StringScalar(rawValue);
	}

	/**
	 * ����̒l������킷�I�u�W�F�N�g���擾����B
	 * <p>
	 * ���̃��\�b�h����Ԃ��ꂽ�I�u�W�F�N�g�̒l�͕ύX���Ă͂Ȃ�Ȃ��B
	 * </p>
	 * 
	 * @param rawValue ���̒l�B
	 * @return �w�肳�ꂽ�l��\���X�J���[�I�u�W�F�N�g�B
	 */
	public static Scalar fromValue(final double rawValue) {

		return new DoubleScalar(rawValue);
	}

	/**
	 * ����̒l������킷�I�u�W�F�N�g���擾����B
	 * <p>
	 * ���̃��\�b�h����Ԃ��ꂽ�I�u�W�F�N�g�̒l�͕ύX���Ă͂Ȃ�Ȃ��B
	 * </p>
	 * 
	 * @param rawValue ���̒l�B
	 * @return �w�肳�ꂽ�l��\���X�J���[�I�u�W�F�N�g�B
	 */
	public static Scalar fromValue(final int rawValue) {

		if (rawValue >= -128 && rawValue <= 127) {
			return intCache[rawValue + 128];
		} else {
			return new IntScalar(rawValue);
		}
	}

	/**
	 * ����̒l������킷�I�u�W�F�N�g���擾����B
	 * <p>
	 * ���̃��\�b�h����Ԃ��ꂽ�I�u�W�F�N�g�̒l�͕ύX���Ă͂Ȃ�Ȃ��B
	 * </p>
	 * 
	 * @param rawValue ���̒l�B
	 * @return �w�肳�ꂽ�l��\���X�J���[�I�u�W�F�N�g�B
	 */
	public static Scalar fromLabel(final int rawValue) {

		return new Label(rawValue);
	}

	//Everything here's impossible to call. Implementing anyways.
	public void checkIncrementSize(int size) {
	}
	public int checkSize0(int size) {
		error(7, "", "Array overflow");
		return size;
	}
	public int checkSize1(int size) {
		error(7, "", "Array overflow");
		return size;
	}
	public int checkSize2(int size) {
		error(7, "", "Array overflow");
		return size;
	}
	public int checkSize3(int size) {
		error(7, "", "Array overflow");
		return size;
	}
	public int checkResize0(int size) {
		error(7, "", "Array overflow");
		return size;
	}
	public int checkResize1(int size) {
		error(7, "", "Array overflow");
		return size;
	}
	public int checkResize2(int size) {
		error(7, "", "Array overflow");
		return size;
	}
	public int checkResize3(int size) {
		error(7, "", "Array overflow");
		return size;
	}
	public int getIndex(final int i0) {
		if(i0 != 0)
			error(7, "", "Array overflow");
		return 0;
	}
	public int getIndex(final int i0, final int i1) {
		if(i0 != 0 || i1 != 0)
			error(7, "", "Array overflow");
		return 0;
	}
	public int getIndex(final int i0, final int i1, final int i2) {
		if(i0 != 0 || i1 != 0 || i2 != 0)
			error(7, "", "Array overflow");
		return 0;
	}
	public int getIndex(final int i0, final int i1, final int i2, final int i3) {
		if(i0 != 0 || i1 != 0 || i2 != 0 || i3 != 0)
			error(7, "", "Array overflow");
		return 0;
	}
	public int getResizeIndex(final int i0) {
		if(i0 != 0)
			error(7, "", "Array overflow");
		return 0;
	}
	public int getResizeIndex(final int i0, final int i1) {
		if(i0 != 0 || i1 != 0)
			error(7, "", "Array overflow");
		return 0;
	}
	public int getResizeIndex(final int i0, final int i1, final int i2) {
		if(i0 != 0 || i1 != 0 || i2 != 0)
			error(7, "", "Array overflow");
		return 0;
	}
	public int getResizeIndex(final int i0, final int i1, final int i2, final int i3) {
		if(i0 != 0 || i1 != 0 || i2 != 0 || i3 != 0)
			error(7, "", "Array overflow");
		return 0;
	}

	//@Override
	public int l0() {
		return 1;
	}

	//@Override
	public int l1() {
		return 0;
	}

	//@Override
	public int l2() {
		return 0;
	}

	//@Override
	public int l3() {
		return 0;
	}
}