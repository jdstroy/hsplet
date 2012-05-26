/*
 * $Id: Array.java,v 1.2 2006/01/13 20:32:12 Yuki Exp $
 */
package hsplet.variable;

/**
 * 配列オペランドをあらわす基本クラス。
 * <p>
 * このクラスは配列のインデックスとオフセットの関係を定義し、 配列の要素の型と演算の実装はサブクラスに任せる。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/01/13 20:32:12 $
 */
public abstract class Array extends Operand {

  /** このクラスを含むソースファイルのバージョン文字列。 */
  private static final String fileVersionID = "$Id: Array.java,v 1.2 2006/01/13 20:32:12 Yuki Exp $";

  /** 一次元目の要素数。 */
  protected int l0;

  /** 二次元目の要素数。 */
  protected int l1;

  /** 三次元目の要素数。 */
  protected int l2;

  /** 四次元目の要素数。 */
  protected int l3;

  /**
   * 要素数を指定してオブジェクトを構築する。
   * <p>
   * オリジナル HSP と違い、要素数に 0 を指定することは出来ない。 つまり、配列は必ず四次元あることになる。 そのほうが要素数などの計算が簡単。
   * </p>
   * 
   * @param l0 一次元目の要素数。
   * @param l1 二次元目の要素数。
   * @param l2 三次元目の要素数。
   * @param l3 四次元目の要素数。
   */
  public Array(final int l0, final int l1, final int l2, final int l3) {

    this.l0 = l0;
    this.l1 = l1;
    this.l2 = l2;
    this.l3 = l3;

  }

  //@Override
  public int l0() {

    return l0;
  }

  //@Override
  public int l1() {

    return l1;
  }

  //@Override
  public int l2() {

    return l2;
  }

  //@Override
  public int l3() {

    return l3;
  }
  
  //@Override
  public int getIndex(final int i0, final int i1) {

    if(i1>l1){
	    if(Variable.class.isAssignableFrom(this.getClass())) System.out.print("v");
	    System.out.println(errorIndex()+" l1 exceeded: "+i1+" "+l1);
    }
    return i1 * l0 + i0;
  }

  //@Override
  public int getIndex(final int i0, final int i1, final int i2) {

    if(i2>l2){
	    if(Variable.class.isAssignableFrom(this.getClass())) System.out.print("v");
	    System.out.println(errorIndex()+" l2 exceeded: "+i2+" "+l2);
    }
    return (i2 * l1 + i1) * l0 + i0;
  }

  //@Override
  public int getIndex(final int i0, final int i1, final int i2, final int i3) {

    if(i3>l3){
	    if(Variable.class.isAssignableFrom(this.getClass())) System.out.print("v");
	    System.out.println(errorIndex()+" l3 exceeded: "+i3+" "+l3);
    }
    return ((i3 * l2 + i2) * l1 + i1) * l0 + i0;
  }



  /**
   * 指定された要素にアクセスできるように配列を自動拡張する。
   * <p>
   * このメソッドは要素数を適切に変更するだけなので。 サブクラスでオーバーライドする必要がある。
   * </p>
   * 
   * @param index 最低限確保したい要素番号。
   */
  public void expand(final int index) {

    // 要素数が 1 じゃない最後の次元を拡張するのが妥当と思われる。 要素の再配置が不要なのがうれしい。
    // 10, 2, 1, 1 -> 10, 3, 1, 1

    int expandSize = (index + 1) - l0 * l1 * l2 * l3;

    if (l3 != 1) {
      l3 += (expandSize + 1) / (l0 * l1 * l2);
    } else if (l2 != 1) {
      l2 += (expandSize + 1) / (l0 * l1);
    } else if (l1 != 1) {
      l1 += (expandSize + 1) / (l0);
    } else {
      l0 += expandSize + 1;
    }

  }
}
