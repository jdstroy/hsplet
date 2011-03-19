/*
 * $Id: ClassDebugger.java,v 1.3 2006/01/13 20:32:11 Yuki Exp $
 */
package hsplet.compiler;

import java.io.Serializable;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * �N���X�����̃f�o�b�O���Ɏg�p�B
 * <p>�����������e�̈ꕔ���R���\�[���ɏo�͂���B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.3 $, $Date: 2006/01/13 20:32:11 $
 */
public class ClassDebugger extends ClassAdapter implements Serializable {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: ClassDebugger.java,v 1.3 2006/01/13 20:32:11 Yuki Exp $";

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = 6559205215277401891L;

	/**
	 * �I�u�W�F�N�g���\�z����B
	 * @param cv �����N���X�r�W�^�B
	 */
	public ClassDebugger(final ClassVisitor cv) {

		super(cv);
	}

	//@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {

		super.visit(version, access, name, signature, superName, interfaces);

		System.out.println("CLASS " + name + " START >>");
		System.out.println();
	}

	//@Override
	public void visitEnd() {

		super.visitEnd();

		System.out.println("<< CLASS END");
		System.out.println();
	}

	//@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {

		return new MethodDebugger(cv, access, name, desc, signature, exceptions);
	}
}
