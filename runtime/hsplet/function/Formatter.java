/*
 * $Id: Formatter.java,v 1.3 2006/01/21 12:48:20 Yuki Exp $
 */
package hsplet.function;

import com.braju.format.Format;

/**
 * strf ����������N���X�B
 * <p>���ۂɂ� hb16 �ɏ������ۓ�������B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.3 $, $Date: 2006/01/21 12:48:20 $
 */
public class Formatter {

	/**
	 * �I�u�W�F�N�g�𕶎��񉻂���B
	 * @param format �����B
	 * @param param �I�u�W�F�N�g�B
	 * @return ���������ꂽ������B
	 */
	public static String format(final String format, final Object param) {
		return Format.sprintf(format, new Object[] { param });
	}
}
