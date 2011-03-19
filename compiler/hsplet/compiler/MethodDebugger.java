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
 * ���\�b�h�����̃f�o�b�O���Ɏg�p�B
 * <p>�����������e�̈ꕔ���R���\�[���ɏo�͂���B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.3 $, $Date: 2006/01/13 20:32:11 $
 */
public class MethodDebugger extends MethodAdapter implements Serializable,
		Opcodes {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: MethodDebugger.java,v 1.3 2006/01/13 20:32:11 Yuki Exp $";

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = -8947987460775942821L;

	/**
	 * �I�u�W�F�N�g���\�z����B
	 * @param cv �N���X�r�W�^�B
	 * @param access �A�N�Z�X���@�B
	 * @param name ���\�b�h���B
	 * @param desc ���\�b�h�̃V�O�l�`���B
	 * @param signature ���\�b�h�̃V�O�l�`���������� null�B
	 * @param exceptions ���̃��\�b�h���������O�B
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
