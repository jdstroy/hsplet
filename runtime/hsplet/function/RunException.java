/*
 * $Id: RunException.java,v 1.1 2006/01/16 19:34:23 Yuki Exp $
 */
package hsplet.function;

/**
 * run ���������邽�߂̗�O�B
 * <p>
 * ���̗�O�𓊂���ƃX���b�h�̍ŏ�ʂŃL���b�`����A�V�����N���X�����s�����B
 * </p>
 * <p>
 * �ʏ킱�̗�O�𒼐ڎg�p���邱�Ƃ͖����A�܂��g�p���ׂ��ł͂Ȃ��B ����� {@link hsplet.function.ProgramCommand#run(hsplet.Context, String, String) }
 * ���g�p����B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.1 $, $Date: 2006/01/16 19:34:23 $
 */
public class RunException extends RuntimeException {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: RunException.java,v 1.1 2006/01/16 19:34:23 Yuki Exp $";

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = 2931064258661525139L;

	private final Class runClass;

	private final String cmdline;

	/**
	 * �I�u�W�F�N�g���\�z����B
	 * @param runClass �N������N���X�B
	 * @param cmdline �R�}���h���C�������B
	 */
	public RunException(final Class runClass, final String cmdline) {
		this.runClass = runClass;
		this.cmdline = cmdline;
	}

	/**
	 * �R�}���h���C���������擾����B
	 * @return �R�}���h���C�������B
	 */
	public String getCmdline() {
		return cmdline;
	}

	/**
	 * �N������N���X���擾����B
	 * @return �N������N���X�B
	 */
	public Class getRunClass() {
		return runClass;
	}

}
