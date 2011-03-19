/*
 * $Id: Mesbox.java,v 1.4 2006/02/27 15:52:05 Yuki Exp $
 */
package hsplet.gui;

import hsplet.variable.Operand;
import hsplet.variable.Scalar;
import hsplet.variable.Variable;
import hsplet.variable.VolatileValue;
import hsplet.variable.VolatileValueUpdater;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * mesbox コントロール。
 * <p>
 * 内容が変更されたら変数を更新する。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.4 $, $Date: 2006/02/27 15:52:05 $
 */
public class Mesbox extends JScrollPane implements VolatileValueUpdater, HSPControl, DocumentListener {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: Mesbox.java,v 1.4 2006/02/27 15:52:05 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = 2562206890409286100L;

	/** 変数 */
	private VolatileValue v;

	/** 変数インデックス */
	private int vi;

	private final JTextArea text;

	/**
	 * オブジェクトを構築する。
	 * @param v 表示する変数。
	 * @param vi 表示する変数のインデックス。
	 * @param editable 編集可能かどうか。
	 * @param hscroll 横スクロール可能かどうか。
	 */
	public Mesbox(final Variable v, final int vi, boolean editable, boolean hscroll) {

		super(VERTICAL_SCROLLBAR_ALWAYS, hscroll ? HORIZONTAL_SCROLLBAR_ALWAYS : HORIZONTAL_SCROLLBAR_NEVER);

		text = new JTextArea(v.toString(vi));

		text.setEditable(editable);

		this.v = v.makeVolatile();
		this.vi = vi;

		text.getDocument().addDocumentListener(this);

		setViewportView(text);
	}

	public void update(final Operand value) {

		value.assign(vi, Scalar.fromValue(text.getText()), 0);
	}

	public Component asComponent() {
		return this;
	}

	public void setValue(Operand v, int vi) {

		text.setText(v.toString(vi));

		this.v.updaters.add(this);
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
