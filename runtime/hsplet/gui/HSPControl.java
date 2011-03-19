/*
 * $Id: HSPControl.java,v 1.2 2006/01/13 05:20:51 Yuki Exp $
 */
package hsplet.gui;

import hsplet.variable.Operand;

import java.awt.Component;
import java.io.Serializable;

/**
 * HSP�̃R���g���[��������킷�C���^�[�t�F�C�X�B
 * 
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/01/13 05:20:51 $
 */
public interface HSPControl extends Serializable {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	static final String fileVersionID = "$Id: HSPControl.java,v 1.2 2006/01/13 05:20:51 Yuki Exp $";

	/**
	 * �R���|�[�l���g�Ƃ��ẴC���X�^���X���擾����B
	 * @return �R���|�[�l���g�Ƃ��Ă̂��̃C���X�^���X�B
	 */
	public Component asComponent();

	/**
	 * �R���g���[���ɒl��ݒ肷��B
	 * <p>objprm �Ŏg�p�����B</p>
	 * @param v �ݒ肷��ϐ��B
	 * @param vi �ݒ肷��ϐ��̃C���f�b�N�X�B
	 */
	public void setValue(final Operand v, final int vi);

}
