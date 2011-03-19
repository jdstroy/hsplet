/*
 * $Id: Conversion.java,v 1.3 2006/05/20 06:12:07 Yuki Exp $
 */
package hsplet.util;

/**
 * �^�ϊ���񋟂��郆�[�e�B���e�B�N���X�B
 * 
 * @author Yuki
 * @version $Revision: 1.3 $, $Date: 2006/05/20 06:12:07 $
 */
public class Conversion {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: Conversion.java,v 1.3 2006/05/20 06:12:07 Yuki Exp $";

	/**
	 * ��������\�Ȍ��萔���ɕϊ�����B
	 * <p>
	 * {@link java.lang.Integer#parseInt(java.lang.String)} �ƈႢ�A���̃��\�b�h�͗�O�𓊂��Ȃ��B
	 * </p>
	 * 
	 * @param text �ϊ���������B
	 * @return �ϊ���̐����B
	 */
	public static int strtoi(final String text) {

		if (!text.trim().equals(text)) {
			return strtoi(text.trim());
		}

		if (text.length() == 0) {
			return 0;
		}

		// �ϊ��\�Ȃ����Ƃ����������ŕϊ�����

		int result = 0;

		for (int i = 0; i < text.length(); ++i) {
			if (i == 0 && (text.charAt(i) == '-' || text.charAt(i) == '+')) {
				continue;
			}
			try {
				result = Integer.parseInt(text.substring(0, i+1));
			} catch (NumberFormatException e) {
				break;
			}
		}

		return result;
	}

	/**
	 * ��������\�Ȍ��萔���ɕϊ�����B
	 * <p>
	 * {@link java.lang.Double#parseDouble(java.lang.String)} �ƈႢ�A���̃��\�b�h�͗�O�𓊂��Ȃ��B
	 * </p>
	 * 
	 * @param text �ϊ���������B
	 * @return �ϊ���̐����B
	 */
	public static double strtod(final String text) {

		if (!text.trim().equals(text)) {
			return strtod(text.trim());
		}

		if (text.length() == 0) {
			return 0.0;
		}

		// �ϊ��\�Ȃ����Ƃ����������ŕϊ�����

		double result = 0;

		for (int i = 0; i < text.length(); ++i) {
			if (i == 0 && (text.charAt(i) == '-' || text.charAt(i) == '+')) {
				continue;
			}
			try {
				result = Double.parseDouble(text.substring(0, i+1));
			} catch (NumberFormatException e) {
				break;
			}
		}

		return result;
	}

}
