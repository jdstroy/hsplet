/*
 * $Id: RunnableCode.java,v 1.2 2006/05/09 11:57:31 Yuki Exp $
 */
package hsplet;

import hsplet.function.EndException;
import hsplet.variable.Operand;

import java.io.Serializable;

/**
 * �R���p�C���ɂ����*.ax���琶�������A���s�\�ȃR�[�h�̃N���X�̊�{�N���X�B <p>
 * ���ۂ̃N���X�̓R���p�C���ɂ���Đ��������܂ł킩��Ȃ��̂ŁA���̃C���^�[�t�F�C�X�����Ɏg�p����B </p>
 *
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/05/09 11:57:31 $
 */
public abstract class RunnableCode implements Runnable, Serializable {

    public final void run() {

        try {
            run(-1);
        } catch (EndException e) {
        }
    }

    /**
     * �R�[�h�����s����B
     *
     * @param label ���s���J�n���郉�x���B�擪����J�n����Ƃ��� -1�B
     * @return �R�[�h�̖߂�l�B�߂�l�������Ƃ��� null�B
     */
    public abstract Operand run(final int label);
}
