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

    public static final int TIMERR_NOERROR = 0, TIMERR_BASE = 96, TIMERR_NOCANDO = 97, TIMERR_STRUCT = 96 + 33;
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

    /** 
     * Requests a minimum timer resolution
     * 
     * @param i The requested minimum resolution of the timer in milliseconds
     * @return TIMERR_NOERROR on success; TIMERR_NOCANDO if the requested 
     * resolution cannot be fulfilled.
     */
    public static int timeBeginPeriod(int i) {
        return TIMERR_NOCANDO;
    }

    /**
     * Releases a request for a minimum timer resolution.
     * @param i The formerly requested minimum resolution of the timer in 
     * milliseconds.
     * @return TIMERR_NOERROR on success; TIMERR_NOCANDO if the requested 
     * resolution cannot be fulfilled.
     */
    public static int timeEndPeriod(int i) {
        return TIMERR_NOCANDO;
    }
}
