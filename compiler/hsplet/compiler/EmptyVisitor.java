/*
 * $Id: EmptyVisitor.java,v 1.1 2006/01/09 12:07:05 Yuki Exp $
 */
package hsplet.compiler;

import java.io.Serializable;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * 何も実行しない MethodVisitor。
 * <p>
 * ASM の追加ライブラリにも同じクラスはあるがサイズが大きいので・・・。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.1 $, $Date: 2006/01/09 12:07:05 $
 */
public class EmptyVisitor extends NullVisitor implements  Serializable {

  /** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
  private static final long serialVersionUID = -3781394417631917269L;

  /** このクラスを含むソースファイルのバージョン文字列。 */
  private static final String fileVersionID = "$Id: EmptyVisitor.java,v 1.1 2006/01/09 12:07:05 Yuki Exp $";

}
