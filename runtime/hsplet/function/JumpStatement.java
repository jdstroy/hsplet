/*
 * $Id: JumpStatement.java,v 1.2 2006/01/13 20:32:09 Yuki Exp $
 */
package hsplet.function;

import java.io.Serializable;

/**
 * �W�����v�X�e�[�g�����g������킷�����l�B
 * <p>
 * button �ȂǁAgoto or gosub ��ǉ��Ŏ󂯎��R�}���h�̈����Ɏg�p����l�B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/01/13 20:32:09 $
 */
public final class JumpStatement implements Serializable {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: JumpStatement.java,v 1.2 2006/01/13 20:32:09 Yuki Exp $";

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = -4548306193459980074L;

	private int code;

	/**
	 * �I�u�W�F�N�g���\�z����B
	 * @param code �W�����v�̎�ނ�\���萔�B
	 */
	private JumpStatement(final int code) {
		this.code = code;
	}

	/** goto ���w�肳�ꂽ�B */
	public static final JumpStatement Goto = new JumpStatement(0);

	/** gosub ���w�肳�ꂽ�B */
	public static final JumpStatement Gosub = new JumpStatement(1);

	private Object readResolve() {
        return code==0 ? Goto : Gosub;
    }

}
