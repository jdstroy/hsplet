/*
 * $Id: Listbox.java,v 1.5 2006/02/27 15:52:04 Yuki Exp $
 */
package hsplet.gui;

import hsplet.variable.Operand;
import hsplet.variable.Scalar;
import hsplet.variable.Variable;
import hsplet.variable.VolatileValue;
import hsplet.variable.VolatileValueUpdater;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * listbox �R���g���[���B
 * <p>
 * �I�����ꂽ��ϐ����X�V����B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.5 $, $Date: 2006/02/27 15:52:04 $
 */
public class Listbox extends JScrollPane implements VolatileValueUpdater, HSPControl, ListSelectionListener {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: Listbox.java,v 1.5 2006/02/27 15:52:04 Yuki Exp $";

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = 8135061756805120960L;

	/** �ϐ� */
	private VolatileValue v;

	/** �ϐ��C���f�b�N�X */
	private int vi;

	private final JList<String> list;

	/**
	 * �I�u�W�F�N�g���\�z����B
	 * @param v �I����ێ�����ϐ��B
	 * @param vi �I����ێ�����ϐ��̃C���f�b�N�X�B
	 * @param items �I�����̔z��B
	 */
	public Listbox(final Variable v, final int vi, final String[] items) {
		super(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);

		list = new JList<String>(items);

		list.setSelectedIndex(v.toIntRaw(vi));

		this.v = v.makeVolatile();
		this.vi = vi;

		list.addListSelectionListener(this);

		setViewportView(list);
	}

	public void update(final Operand value) {

		value.assign(vi, Scalar.fromValue(list.getSelectedIndex()), 0);
	}

	public Component asComponent() {
		return this;
	}

	public void setValue(Operand v, int vi) {

		switch (v.getType()) {
		case Operand.Type.INTEGER:
			list.setSelectedIndex(v.toIntRaw(vi));
			break;
		default:
			final int selection = list.getSelectedIndex();

			removeAll();

			list.setListData(v.toStringRaw(vi).split("\\r?\\n"));

			list.setSelectedIndex(selection < list.getModel().getSize() ? selection : -1);
		}

		this.v.updaters.add(this);
	}

	public void valueChanged(ListSelectionEvent e) {
		this.v.updaters.add(this);

	}
}
