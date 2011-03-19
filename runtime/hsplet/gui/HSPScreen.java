/*
 * $Id: HSPScreen.java,v 1.4 2006/01/21 12:48:16 Yuki Exp $
 */
package hsplet.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.io.Serializable;

/**
 * HSP�̉�ʂ�����킷�C���^�[�t�F�C�X�B
 * <p>
 * ���ʂ̋@�\�� ScreenInfo �Ŏ������Ă���̂ŁA���̃C���^�[�t�F�C�X�͂���قǑ����̏���񋟂��Ȃ��B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.4 $, $Date: 2006/01/21 12:48:16 $
 */
public interface HSPScreen extends Serializable {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	static final String fileVersionID = "$Id: HSPScreen.java,v 1.4 2006/01/21 12:48:16 Yuki Exp $";

	/**
	 * �R���|�[�l���g�Ƃ��ẴC���X�^���X���擾����B
	 * @return �R���|�[�l���g�Ƃ��Ă̂��̃C���X�^���X�B
	 */
	public Component asComponent();

	/**
	 * ��ʏ��I�u�W�F�N�g���擾����B
	 * @return  ��ʏ��I�u�W�F�N�g�B
	 */
	public Bmscr getBmscr();

	/**
	 * ��ʃT�C�Y���擾����B
	 * @return ��ʃT�C�Y�B
	 */
	public Dimension getSize();

	/**
	 * �^�C�g����ݒ肷��B
	 * @param text �ݒ肷��e�L�X�g�B
	 */
	public void setTitle(final String text);
}
