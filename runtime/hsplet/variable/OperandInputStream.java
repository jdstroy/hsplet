/*
 * $Id: OperandInputStream.java,v 1.1 2006/01/21 12:48:19 Yuki Exp $
 */
package hsplet.variable;

import java.io.InputStream;

/**
 * �ϐ�����ǂݎ����̓X�g���[���B
 * 
 * @author Yuki
 * @version $Revision: 1.1 $, $Date: 2006/01/21 12:48:19 $
 */
public class OperandInputStream extends InputStream implements Cloneable {

	/**
	 * �I�u�W�F�N�g���\�z����B
	 * @param o �I�y�����h�B
	 * @param oi �I�y�����h�̔z��v�f�C���f�b�N�X�B
	 * @param base �ǂݎ��C���f�b�N�X�B
	 * @param size �f�[�^�T�C�Y�B
	 */
	public OperandInputStream(final Operand o, final int oi, final int base, final int size) {
		this.o = o;
		this.oi = oi;
		this.base = base;
		this.size = size;
		index = 0;
	}

	public Object clone() {
		return new OperandInputStream(o, oi, base, size);
	}

	private final Operand o;

	private final int oi;

	private final int base;

	private final int size;

	/**
	 * �f�[�^�T�C�Y�擾�B
	 * @return �f�[�^�T�C�Y�B
	 */
	public int getSize() {
		return size;
	}

	private int index;

	public int read() {

		if (size > 0 && index >= size) {
			return -1;
		}

		try {

			return o.peek(oi, base + (index++))&0xFF;

		} catch (Exception e) {
			return -1;
		}
	}

}
