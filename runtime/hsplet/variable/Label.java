/*
 * $Id: Label.java,v 1.2 2006/01/13 20:32:12 Yuki Exp $
 */
package hsplet.variable;

/**
 * ���x��������킷�I�y�����h�B
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/01/13 20:32:12 $
 */
public class Label extends IntScalar {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: Label.java,v 1.2 2006/01/13 20:32:12 Yuki Exp $";

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = 6798195317537227773L;

	/**
	 * �l���w�肵�ăI�u�W�F�N�g���\�z����B
	 * 
	 * @param value �����l�B
	 */
	public Label(final int value) {

		super(value);
	}

	//@Override
	public int getType() {

		return Type.LABEL;
	}

}
