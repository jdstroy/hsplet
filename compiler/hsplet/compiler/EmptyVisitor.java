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
public class EmptyVisitor implements MethodVisitor, Serializable {

  /** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
  private static final long serialVersionUID = -3781394417631917269L;

  /** このクラスを含むソースファイルのバージョン文字列。 */
  private static final String fileVersionID = "$Id: EmptyVisitor.java,v 1.1 2006/01/09 12:07:05 Yuki Exp $";

  public AnnotationVisitor visitAnnotationDefault() {

    return null;
  }

  public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

    return null;
  }

  public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {

    return null;
  }

  public void visitAttribute(Attribute attr) {

  }

  public void visitCode() {

  }

  public void visitInsn(int opcode) {

  }

  public void visitIntInsn(int opcode, int operand) {

  }

  public void visitVarInsn(int opcode, int var) {

  }

  public void visitTypeInsn(int opcode, String desc) {

  }

  public void visitFieldInsn(int opcode, String owner, String name, String desc) {

  }

  public void visitMethodInsn(int opcode, String owner, String name, String desc) {

  }

  public void visitJumpInsn(int opcode, Label label) {

  }

  public void visitLabel(Label label) {

  }

  public void visitLdcInsn(Object cst) {

  }

  public void visitIincInsn(int var, int increment) {

  }

  public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {

  }

  public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {

  }

  public void visitMultiANewArrayInsn(String desc, int dims) {

  }

  public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {

  }

  public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {

  }

  public void visitLineNumber(int line, Label start) {

  }

  public void visitMaxs(int maxStack, int maxLocals) {

  }

  public void visitEnd() {

  }

}
