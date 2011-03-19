/*
 * $Id: Button.java,v 1.3 2006/02/11 17:19:32 Yuki Exp $
 */
package hsplet.gui;

import hsplet.Context;
import hsplet.JumpTask;
import hsplet.function.JumpStatement;
import hsplet.variable.Operand;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * button �R���g���[���B
 * <p>
 * �N���b�N���ꂽ��W�����v�^�X�N��o�^����B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.3 $, $Date: 2006/02/11 17:19:32 $
 */
public class Button extends javax.swing.JButton implements ActionListener, HSPControl {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: Button.java,v 1.3 2006/02/11 17:19:32 Yuki Exp $";

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = 7638106082988894456L;

	/**
	 * �I�u�W�F�N�g���\�z����B
	 * @param text �{�^���ɕ\������e�L�X�g�B
	 * @param context ���s����Ă���R���e�L�X�g�B
	 * @param jump �W�����v�̎�ށB
	 * @param label �W�����v�惉�x���B
	 */
	public Button(final String text, final Context context, final JumpStatement jump, final int label) {

		super(text);
		this.context = context;
		this.jump = jump;
		this.label = label;

		addActionListener(this);
	}

	private final Context context;

	private final JumpStatement jump;

	private final int label;

	public void actionPerformed(ActionEvent e) {

		context.tasks.add(new JumpTask(jump, label, null, null, null));
	}

	public Component asComponent() {
		return this;
	}

	public void setValue(Operand v, int vi) {

		setLabel(v.toString(vi));
	}

}
