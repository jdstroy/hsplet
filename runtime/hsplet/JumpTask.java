/*
 * $Id: JumpTask.java,v 1.3 2006/01/13 20:32:10 Yuki Exp $
 */
package hsplet;

import hsplet.function.JumpStatement;
import hsplet.function.ProgramCommand;

/**
 * �{�^����L�[���͂Ȃǂɂ���Ĕ������銄�荞�݃W�����v�^�X�N�B
 * 
 * @author Yuki
 * @version $Revision: 1.3 $, $Date: 2006/01/13 20:32:10 $
 */
public class JumpTask implements Task {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: JumpTask.java,v 1.3 2006/01/13 20:32:10 Yuki Exp $";

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = -6548767826568493395L;

	private final JumpStatement jump;

	private final int label;

	private final Integer iparam;

	private final Integer wparam;

	private final Integer lparam;

	/**
	 * �I�u�W�F�N�g���\�z����B
	 * @param jump �W�����v�̎�ށB
	 * @param label �W�����v�惉�x���B
	 * @param iparam �V�X�e���ϐ� iparam �ɃZ�b�g����l�B
	 * �Z�b�g����K�v�������Ƃ��� null�B
	 * @param wparam �V�X�e���ϐ� wparam �ɃZ�b�g����l�B
	 * �Z�b�g����K�v�������Ƃ��� null�B
	 * @param lparam �V�X�e���ϐ� lparam �ɃZ�b�g����l�B
	 * �Z�b�g����K�v�������Ƃ��� null�B
	 */
	public JumpTask(final JumpStatement jump, final int label,
			final Integer iparam, final Integer wparam, final Integer lparam) {

		this.jump = jump;
		this.label = label;
		this.iparam = iparam;
		this.wparam = wparam;
		this.lparam = lparam;
	}

	public void run(Context context) {

		if (iparam != null) {
			context.iparam.value = iparam.intValue();
		}
		if (lparam != null) {
			context.lparam.value = lparam.intValue();
		}
		if (wparam != null) {
			context.wparam.value = wparam.intValue();
		}

		if ( jump==JumpStatement.Goto ){
			ProgramCommand.goto_(context, label, true);
		}else {
			ProgramCommand.gosub(context, label);
		}
	}

}
