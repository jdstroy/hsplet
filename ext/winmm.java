/*
 * $Id: winmm.java,v 1.1 2006/01/09 12:07:06 Yuki Exp $
 */

import hsplet.function.FunctionBase;

/**
 * winmm.dll �̊֐��Q����������N���X�B
 * <p>
 * ���̃N���X�� DLL �̎������@���ɗe�Ղł��邩���悭����킵�Ă���B
 * </p>
 * <p>
 * ���Ȃ͂��ADLL ����t�����N���X���f�t�H���g�p�b�P�[�W�ɔz�u���A public static �ȃ��\�b�h���������邾���ł悢�B
 * </p>
 * <p>
 * �߂�l�ɂ� void/int/double/String/ByteString/Operand ���g�p�ł���B
 * </p>
 * <p>
 * �����ɂ� int/double/string/ByteString/Operand/Context ���󂯎�邱�Ƃ��o����B
 * </p>
 * <p>
 * Operand ���󂯎��Ƃ��́A�K�����̈����� int �ɂ��A�z��̃C���f�b�N�X���󂯎��Ȃ���΂����Ȃ��B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.1 $, $Date: 2006/01/09 12:07:06 $
 */
public class winmm extends FunctionBase {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: winmm.java,v 1.1 2006/01/09 12:07:06 Yuki Exp $";

	private static final long startMillis = System.currentTimeMillis();

	/**
	 * �N�����Ă��猻�݂܂ł̃~���b���擾����B
	 * 
	 * @return �N�����Ă��猻�݂܂ł̃~���b�B
	 */
	public static int timeGetTime() {

		return (int) (System.currentTimeMillis() - startMillis);
	}


        public static int timeBeginPeriod(int i) {
            throw new UnsupportedOperationException("Not yet implemented.");
        }


        public static int timeEndPeriod(int i) {
            throw new UnsupportedOperationException("Not yet implemented.");
        }
}
