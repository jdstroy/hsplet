package hsplet.variable;

/**
 * �ϐ��̒l���Ǘ�����I�u�W�F�N�g�B
 * <p>
 * �ϐ��̒l���ύX����A�ŐV�̏�񂪕K�v�ɂȂ����Ƃ��ɌĂяo�����B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/02/11 17:19:35 $
 */
public interface VolatileValueUpdater {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	static final String fileVersionID = "$Id: VolatileValueUpdater.java,v 1.2 2006/02/11 17:19:35 Yuki Exp $";

	/**
	 * �����ɓn���ꂽ�ϐ��̒l���ŐV�ɂ���B
	 * 
	 * @param value �X�V����ϐ��B
	 */
	public void update(final Operand value);
}
