/*
 * $Id: Chkbox.java,v 1.4 2006/02/27 15:52:05 Yuki Exp $
 */
package hsplet.gui;

import hsplet.variable.Operand;
import hsplet.variable.Scalar;
import hsplet.variable.Variable;
import hsplet.variable.VolatileValue;
import hsplet.variable.VolatileValueUpdater;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

/**
 * �`�F�b�N�{�b�N�X�B
 * <p>
 * �N���b�N���ꂽ��ϐ����X�V����B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.4 $, $Date: 2006/02/27 15:52:05 $
 */
public class Chkbox extends JCheckBox implements VolatileValueUpdater, HSPControl, ItemListener {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: Chkbox.java,v 1.4 2006/02/27 15:52:05 Yuki Exp $";

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = 6258069283313905933L;

	/** �ϐ� */
	private VolatileValue v;

	/** �ϐ��C���f�b�N�X */
	private int vi;

	/**
	 * �I�u�W�F�N�g���\�z����B
	 * @param text �`�F�b�N�{�b�N�X�̃e�L�X�g�B
	 * @param v ��Ԃ�ێ�����ϐ��B
	 * @param vi ��Ԃ�ێ�����ϐ��̃C���f�b�N�X�B
	 */
	public Chkbox(final String text, final Variable v, final int vi) {
		super(text, v.toInt(vi) != 0);

		this.v = v.makeVolatile();
		this.vi = vi;

		addItemListener(this);

	}

	public void update(final Operand value) {

		value.assign(vi, Scalar.fromValue(isSelected() ? 1 : 0), 0);
	}

	public Component asComponent() {
		return this;
	}

	public void setValue(Operand v, int vi) {

		setSelected(v.toInt(vi) != 0);

		this.v.updaters.add(this);
	}

	public void itemStateChanged(ItemEvent e) {

		this.v.updaters.add(this);
	}
}
