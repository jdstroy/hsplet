/*
 * $Id: Combox.java,v 1.4 2006/02/11 17:19:31 Yuki Exp $
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

import javax.swing.JComboBox;

/**
 * �R���{�{�b�N�X�B
 * <p>
 * �I�����ꂽ��ϐ����X�V����B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.4 $, $Date: 2006/02/11 17:19:31 $
 */
public class Combox extends JComboBox<String> implements VolatileValueUpdater, HSPControl, ItemListener {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: Combox.java,v 1.4 2006/02/11 17:19:31 Yuki Exp $";

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = -8777787617022303507L;

	/** �ϐ� */
	private VolatileValue v;

	/** �ϐ��C���f�b�N�X */
	private int vi;

	/**
	 * �I�u�W�F�N�g���\�z����B
	 * @param v �I����ێ�����ϐ��B
	 * @param vi �I����ێ�����ϐ��̃C���f�b�N�X�B
	 * @param items �I�����̔z��B
	 */
	public Combox(final Variable v, final int vi, final String[] items) {

		for (int i = 0; i < items.length; ++i) {
			addItem(items[i]);
		}

		setSelectedIndex(v.toInt(vi));

		this.v = v.makeVolatile();
		this.vi = vi;

		addItemListener(this);
	}

	public Component asComponent() {
		return this;
	}

	public void setValue(Operand v, int vi) {

		switch (v.getType()) {
		case Operand.Type.INTEGER:
			setSelectedIndex(v.toInt(vi));
			break;
		default:
			final int selection = getSelectedIndex();

			removeAll();

			final String[] items = v.toString(vi).split("\\r?\\n");
			for (int i = 0; i < items.length; ++i) {
				addItem(items[i]);
			}

			setSelectedIndex(selection < getItemCount() ? selection : -1);
		}

		this.v.updaters.add(this);
	}

	public void update(Operand value) {

		value.assign(vi, Scalar.fromValue(getSelectedIndex()), 0);
	}

	public void itemStateChanged(ItemEvent e) {

		this.v.updaters.add(this);
	}
}
