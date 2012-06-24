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
 * コンボボックス。
 * <p>
 * 選択されたら変数を更新する。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.4 $, $Date: 2006/02/11 17:19:31 $
 */
public class Combox extends JComboBox<String> implements VolatileValueUpdater, HSPControl, ItemListener {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: Combox.java,v 1.4 2006/02/11 17:19:31 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = -8777787617022303507L;

	/** 変数 */
	private VolatileValue v;

	/** 変数インデックス */
	private int vi;

	/**
	 * オブジェクトを構築する。
	 * @param v 選択を保持する変数。
	 * @param vi 選択を保持する変数のインデックス。
	 * @param items 選択肢の配列。
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
