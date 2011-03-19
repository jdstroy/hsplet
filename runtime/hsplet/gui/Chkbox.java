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
 * チェックボックス。
 * <p>
 * クリックされたら変数を更新する。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.4 $, $Date: 2006/02/27 15:52:05 $
 */
public class Chkbox extends JCheckBox implements VolatileValueUpdater, HSPControl, ItemListener {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: Chkbox.java,v 1.4 2006/02/27 15:52:05 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = 6258069283313905933L;

	/** 変数 */
	private VolatileValue v;

	/** 変数インデックス */
	private int vi;

	/**
	 * オブジェクトを構築する。
	 * @param text チェックボックスのテキスト。
	 * @param v 状態を保持する変数。
	 * @param vi 状態を保持する変数のインデックス。
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
