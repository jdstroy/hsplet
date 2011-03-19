/*
 * $Id: JumpTask.java,v 1.3 2006/01/13 20:32:10 Yuki Exp $
 */
package hsplet;

import hsplet.function.JumpStatement;
import hsplet.function.ProgramCommand;

/**
 * ボタンやキー入力などによって発生する割り込みジャンプタスク。
 * 
 * @author Yuki
 * @version $Revision: 1.3 $, $Date: 2006/01/13 20:32:10 $
 */
public class JumpTask implements Task {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: JumpTask.java,v 1.3 2006/01/13 20:32:10 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = -6548767826568493395L;

	private final JumpStatement jump;

	private final int label;

	private final Integer iparam;

	private final Integer wparam;

	private final Integer lparam;

	/**
	 * オブジェクトを構築する。
	 * @param jump ジャンプの種類。
	 * @param label ジャンプ先ラベル。
	 * @param iparam システム変数 iparam にセットする値。
	 * セットする必要が無いときは null。
	 * @param wparam システム変数 wparam にセットする値。
	 * セットする必要が無いときは null。
	 * @param lparam システム変数 lparam にセットする値。
	 * セットする必要が無いときは null。
	 */
	public JumpTask(final JumpStatement jump, final int label,
			final Integer iparam, final Integer wparam, final Integer lparam) {

		this.jump = jump;
		this.label = label;
		this.iparam = iparam;
		this.wparam = wparam;
		this.lparam = lparam;
	}

	public void run(Context context) {

		if (iparam != null) {
			context.iparam.value = iparam.intValue();
		}
		if (lparam != null) {
			context.lparam.value = lparam.intValue();
		}
		if (wparam != null) {
			context.wparam.value = wparam.intValue();
		}

		if ( jump==JumpStatement.Goto ){
			ProgramCommand.goto_(context, label, true);
		}else {
			ProgramCommand.gosub(context, label);
		}
	}

}
