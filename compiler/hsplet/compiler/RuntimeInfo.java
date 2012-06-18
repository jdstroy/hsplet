/*
 * $Id: RuntimeInfo.java,v 1.2 2006/01/13 05:20:55 Yuki Exp $
 */
package hsplet.compiler;

import hsplet.compiler.ByteCode.Code;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * �����^�C���Ɋւ�����i�o�C�g�R�[�h�ƃ��\�b�h�̊֘A�t���j��񋟂���C���^�[�t�F�C�X�B
 * 
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/01/13 05:20:55 $
 */
public interface RuntimeInfo extends Serializable {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	static final String fileVersionID = "$Id: RuntimeInfo.java,v 1.2 2006/01/13 05:20:55 Yuki Exp $";

	/**
	 * �R�[�h�����s����N���X���擾����B
	 * @param ax �o�C�g�R�[�h�B
	 * @param code ���߃R�[�h�B
	 * @return ���s����N���X�B
	 */
	public Class getClassFor(final ByteCode ax, final Code code);

	/**
	 * �R�[�h�����s���郁�\�b�h���擾����B
	 * <p>���̃��\�b�h�̐錾����Ă���N���X���A���ۂɖ��߂����s����ۏ؂͖����B
	 * 	���ۂɖ��߂����s����N���X�� {@link RuntimeInfo#getClassFor(ByteCode,Code)} �Ŏ擾����B
	 * </p>
	 * @param ax �o�C�g�R�[�h�B
	 * @param code ���߃R�[�h�B
	 * @return ���s���郁�\�b�h�B
	 */
	public Method getMethodFor(final ByteCode ax, final ByteCode.Code code);

	//Compiler provides the name, runtimeinfo gets the actual method
	public Method getMethodFor(final ByteCode ax, final ByteCode.Code code, final String name);
}
