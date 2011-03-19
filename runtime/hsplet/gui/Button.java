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
 * button コントロール。
 * <p>
 * クリックされたらジャンプタスクを登録する。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.3 $, $Date: 2006/02/11 17:19:32 $
 */
public class Button extends javax.swing.JButton implements ActionListener, HSPControl {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: Button.java,v 1.3 2006/02/11 17:19:32 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = 7638106082988894456L;

	/**
	 * オブジェクトを構築する。
	 * @param text ボタンに表示するテキスト。
	 * @param context 実行されているコンテキスト。
	 * @param jump ジャンプの種類。
	 * @param label ジャンプ先ラベル。
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
