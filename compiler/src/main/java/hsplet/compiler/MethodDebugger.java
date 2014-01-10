/*
 * $Id: MethodDebugger.java,v 1.3 2006/01/13 20:32:11 Yuki Exp $
 */
package hsplet.compiler;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.Opcodes;

/**
 * メソッド生成のデバッグ時に使用。
 * <p>生成した内容の一部をコンソールに出力する。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.3 $, $Date: 2006/01/13 20:32:11 $
 */
public class MethodDebugger extends MethodAdapter implements Serializable,
		Opcodes {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: MethodDebugger.java,v 1.3 2006/01/13 20:32:11 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = -8947987460775942821L;

	/**
	 * オブジェクトを構築する。
	 * @param cv クラスビジタ。
	 * @param access アクセス方法。
	 * @param name メソッド名。
	 * @param desc メソッドのシグネチャ。
	 * @param signature メソッドのシグネチャもしくは null。
	 * @param exceptions このメソッドが投げる例外。
	 */
	public MethodDebugger(final ClassVisitor cv, final int access,
			final String name, final String desc, final String signature,
			final String[] exceptions) {

		super(cv.visitMethod(access, name, desc, signature, exceptions));

		System.out.println("METHOD " + name + desc + " START >>>>");
	}

	//@Override
	public void visitEnd() {

		System.out.println("<<<< METHOD END");
		System.out.println();
	}

	//@Override
	public void visitLabel(Label label) {

		super.visitLabel(label);

		System.out.println("\t" + "LABEL:" + label.hashCode());
	}

	//@Override
	public void visitJumpInsn(int opcode, Label label) {

		super.visitJumpInsn(opcode, label);

		System.out.println("\t" + opcodeName(opcode) + " -> LABEL:"
				+ label.hashCode());
	}

	//@Override
	public void visitLookupSwitchInsn(Label def, int[] values, Label[] labels) {
		super.visitLookupSwitchInsn(def, values, labels);

		System.out.println("\tSWITCH -> LABEL:" + def);
		for( int i = 0; i<labels.length; ++i ){
			System.out.println("\tSWITCH -> LABEL:" + labels[i].hashCode());
		}
	}

	//@Override
	public void visitTableSwitchInsn(int min, int max, Label def, Label[] labels) {
		super.visitTableSwitchInsn(min, max, def, labels);

		System.out.println("\tSWITCH -> LABEL:" + def);
		for( int i = 0; i<labels.length; ++i ){
			System.out.println("\tSWITCH -> LABEL:" + labels[i].hashCode());
		}
	}

	private static String opcodeName(final int opcode) {

		final Field[] fields = Opcodes.class.getFields();
		for( int i = 0; i<fields.length; ++i ){
			final Field field = fields[i];
			if (!field.getName().startsWith("V1_")) {

				try {
					final Object o = field.get(null);
					if (o instanceof Integer
							&& ((Integer) o).intValue() == opcode) {
						return field.getName();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return "";
	}
}
