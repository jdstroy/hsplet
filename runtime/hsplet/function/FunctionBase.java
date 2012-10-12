/*
 * $Id: FunctionBase.java,v 1.1 2006/01/09 12:07:12 Yuki Exp $
 */
package hsplet.function;

import hsplet.variable.Operand;

/**
 * 関数群クラスの実装に便利ないくつかのスタティックメソッドを持ったクラス。
 * <p>
 * 新しい関数群クラスを作成するときは通常このクラスを拡張するが、しなくても良い。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.1 $, $Date: 2006/01/09 12:07:12 $
 */
public class FunctionBase {

  /** このクラスを含むソースファイルのバージョン文字列。 */
  private static final String fileVersionID = "$Id: FunctionBase.java,v 1.1 2006/01/09 12:07:12 Yuki Exp $";

  protected static int toInt(final Operand v, final int vi, final int ifnull) {

    if (v == null) {
      return ifnull;
    } else {
      return v.toIntRaw(vi);
    }
  }

  protected static String toString(final Operand v, final int vi, final String ifnull) {

    if (v == null) {
      return ifnull;
    } else {
      return v.toByteStringRaw(vi).toString();
    }
  }

  protected static double toDouble(final Operand v, final int vi, final double ifnull) {

    if (v == null) {
      return ifnull;
    } else {
      return v.toDoubleRaw(vi);
    }
  }

  protected static Operand ref(final Operand v, final int vi) {

    if (v == null) {
      return null;
    } else {
      return v.ref(vi);
    }
  }

}
