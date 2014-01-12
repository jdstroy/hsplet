/*
 * $Id: RunnableCode.java,v 1.2 2006/05/09 11:57:31 Yuki Exp $
 */
package hsplet;

import hsplet.function.EndException;
import hsplet.variable.Operand;

import java.io.Serializable;

/**
 * コンパイラによって*.axから生成される、実行可能なコードのクラスの基本クラス。 <p>
 * 実際のクラスはコンパイラによって生成されるまでわからないので、このインターフェイスを代わりに使用する。 </p>
 *
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/05/09 11:57:31 $
 */
public abstract class RunnableCode implements Runnable, Serializable {

    public final void run() {

        try {
            run(-1);
        } catch (EndException e) {
        }
    }

    /**
     * コードを実行する。
     *
     * @param label 実行を開始するラベル。先頭から開始するときは -1。
     * @return コードの戻り値。戻り値が無いときは null。
     */
    public abstract Operand run(final int label);
}
