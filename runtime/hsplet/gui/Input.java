/*
 * $Id: Input.java,v 1.6.2.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.gui;

import hsplet.util.Conversion;
import hsplet.variable.Operand;
import hsplet.variable.Scalar;
import hsplet.variable.Variable;
import hsplet.variable.VolatileValue;
import hsplet.variable.VolatileValueUpdater;

import java.awt.Component;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * input �R���g���[���B
 * <p>���e���ύX���ꂽ��ϐ����X�V����B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.6.2.1 $, $Date: 2006/08/02 12:13:06 $
 */
public class Input extends JTextField implements VolatileValueUpdater, HSPControl, DocumentListener {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: Input.java,v 1.6.2.1 2006/08/02 12:13:06 Yuki Exp $";

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = -1371744407933876506L;

	/** �ϐ� */
	private VolatileValue v;

	/** �ϐ��C���f�b�N�X */
	private int vi;

	/**
	 * �I�u�W�F�N�g���\�z����B
	 * @param v �\������ϐ��B
	 * @param vi �\������ϐ��̃C���f�b�N�X�B
	 * @param length �ő啶�����B
	 */
	public Input(final Variable v, final int vi, final int length) {

		setDocument(new PlainDocument() {
			public void remove(int offs, int len) throws BadLocationException {

				final String text = getText(0, offs) + getText(offs + len, getLength() - offs - len);

				if (isValidText(text)) {
					super.remove(offs, len);
				}
			}

			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

				final String text = getText(0, offs) + str + getText(offs, getLength() - offs);

				if (isValidText(text)) {
					super.insertString(offs, str, a);
				}
			}

			private boolean isValidText(final String text) {
				
				if ( text.length()==0 ) {
					return true;
				}

				if (length > 0 && text.length() == length) {
					return false;
				}

				try {
					switch (v.getType()) {
					case Operand.Type.INTEGER:
						if (!text.equals("-") && !text.equals("+")) {
							Integer.parseInt(text);
						}
						break;
					case Operand.Type.DOUBLE:
						if (!text.equals("-") && !text.equals("+")) {
							Double.parseDouble(text);
						}
						break;
					default:
						break;
					}
				} catch (NumberFormatException e) {
					return false;
				}

				return true;
			}

		});
		
		this.setText(v.toByteStringRaw(vi).toString());
		this.v = v.makeVolatile();
		this.vi = vi;

		getDocument().addDocumentListener(this);
	}

	public Component asComponent() {
		return this;
	}

	public void setValue(Operand v, int vi) {

		setText(v.toStringRaw(vi));

		this.v.updaters.add(this);
	}

	public void update(Operand value) {
		switch (value.getType()) {
		case Operand.Type.INTEGER:
			value.assignRaw(vi, Scalar.fromValue(Conversion.strtoi(getText())), 0);
			break;
		case Operand.Type.DOUBLE:
			value.assignRaw(vi, Scalar.fromValue(Conversion.strtod(getText())), 0);
			break;
		default:
			value.assignRaw(vi, Scalar.fromValue(getText()), 0);
			break;

		}

	}

	public void changedUpdate(DocumentEvent e) {
		this.v.updaters.add(this);

	}

	public void insertUpdate(DocumentEvent e) {
		this.v.updaters.add(this);

	}

	public void removeUpdate(DocumentEvent e) {
		this.v.updaters.add(this);

	}
}
