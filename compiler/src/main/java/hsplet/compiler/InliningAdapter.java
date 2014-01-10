/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.compiler;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingMethodAdapter;

/**
 *
 * @author jdstroy
 */
public class InliningAdapter extends RemappingMethodAdapter {

    private final LocalVariablesSorter lvs;
    private final Label end;

    public InliningAdapter(LocalVariablesSorter mv, Label end, int acc, String desc, Remapper remapper) {
        super(acc, desc, mv, remapper);
        this.lvs = mv;
        this.end = end;
        int offset = (acc & Opcodes.ACC_STATIC) != 0 ? 0 : 1;
        Type[] args = Type.getArgumentTypes(desc);
        for (int i = args.length - 1; i >= 0; i--) {
            super.visitVarInsn(args[i].getOpcode(Opcodes.ISTORE), i + offset);
        }
        if (offset > 0) {
            super.visitVarInsn(Opcodes.ASTORE, 0);
        }
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode == Opcodes.RETURN) {
            super.visitJumpInsn(Opcodes.GOTO, end);
            System.out.println("GOTO from K");
        } else {
            super.visitInsn(opcode);
        }
    }

    @Override
    public void visitMaxs(int stack, int locals) {
    }

    @Override
    protected int newLocalMapping(Type type) {
        return lvs.newLocal(type);
    }
}
