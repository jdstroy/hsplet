/*
 * $Id: Task.java,v 1.1 2006/01/09 12:07:13 Yuki Exp $
 */
package hsplet;

import java.io.Serializable;

/**
 * button 割り込みなどのタスクのインターフェイス。
 * <p>
 * HSPLet 3.0 は GUI とスクリプト処理が別スレッドで行われるため、button などのイベントを即座に実行することが出来ない。 割り込みが発生したときは、コンテキストにこのタスクを追加しておき、その後の
 * await/wait/stop 時に実行する。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.1 $, $Date: 2006/01/09 12:07:13 $
 */
public interface Task extends Serializable {

  /** このクラスを含むソースファイルのバージョン文字列。 */
  static final String fileVersionID = "$Id: Task.java,v 1.1 2006/01/09 12:07:13 Yuki Exp $";

  /**
   * タスクを実行する。
   * 
   * @param context 実行コンテキスト。
   */
  public void run(final Context context);
}
