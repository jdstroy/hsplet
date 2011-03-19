/*
 * $Id: GotoException.java,v 1.1 2006/01/09 12:07:12 Yuki Exp $
 */
package hsplet.function;

/**
 * goto を実現するための例外。
 * <p>
 * HSPLet 3 では、HSP のすべてのコードを一つのメソッド内に詰め込んでいるため、GOTO するにはローカルジャンプをする必要がある。 別関数から目的のアドレスに直に飛ぶ方法は無いため、いったん
 * 生成されたメイン関数に戻った後、目的のアドレスにジャンプする。
 * </p>
 * <p>
 * この例外を投げると、生成されたメイン関数内で適切にハンドルされ、目的のラベルにジャンプすることが出来る。
 * </p>
 * <p>
 * 通常この例外を直接使用することは無く、また使用すべきではない。 代わりに {@link hsplet.function.ProgramCommand#goto_(hsplet.Context, int, boolean) }
 * を使用する。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.1 $, $Date: 2006/01/09 12:07:12 $
 */
public class GotoException extends RuntimeException {

  /** このクラスを含むソースファイルのバージョン文字列。 */
  private static final String fileVersionID = "$Id: GotoException.java,v 1.1 2006/01/09 12:07:12 Yuki Exp $";

  /** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
  private static final long serialVersionUID = 7274255688514910944L;

  /** ジャンプ先ラベル。 */
  public final int label;

  /**
   * ジャンプ先ラベルを指定してオブジェクトを構築する。
   * 
   * @param label ジャンプ先ラベル。
   */
  public GotoException(int label) {

    this.label = label;
  }

}
