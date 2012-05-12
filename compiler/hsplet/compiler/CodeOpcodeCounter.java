/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.compiler;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;

/**
 *
 * @author jdstroy
 * Estimates the number of opcodes in each method by intercepting all visit 
 * methods for opcode generation.
 * Adding line numbers to every opcode - Kejardon
 */
public class CodeOpcodeCounter extends MethodAdapter {
    private int count = 0;

    public CodeOpcodeCounter(MethodVisitor mv) {
        super(mv);
    }

    /* Gets the number of opcodes issued. */
    public int getCount() {
        return count;
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        count++;
        Label ln=new Label();
        visitLabel(ln);
        visitLineNumber(count, ln);
        super.visitFieldInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        count++;
        Label ln=new Label();
        visitLabel(ln);
        visitLineNumber(count, ln);
        super.visitIincInsn(var, increment);
    }

    @Override
    public void visitInsn(int opcode) {
        count++;
        Label ln=new Label();
        visitLabel(ln);
        visitLineNumber(count, ln);
        super.visitInsn(opcode);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        count++;
        Label ln=new Label();
        visitLabel(ln);
        visitLineNumber(count, ln);
        super.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        count++;
        Label ln=new Label();
        visitLabel(ln);
        visitLineNumber(count, ln);
        super.visitJumpInsn(opcode, label);
    }

    @Override
    public void visitLdcInsn(Object cst) {
        count++;
        Label ln=new Label();
        visitLabel(ln);
        visitLineNumber(count, ln);
        super.visitLdcInsn(cst);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        count++;
        Label ln=new Label();
        visitLabel(ln);
        visitLineNumber(count, ln);
        super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        count++;
        Label ln=new Label();
        visitLabel(ln);
        visitLineNumber(count, ln);
        super.visitMethodInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        count++;
        Label ln=new Label();
        visitLabel(ln);
        visitLineNumber(count, ln);
        super.visitMultiANewArrayInsn(desc, dims);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
        count++;
        Label ln=new Label();
        visitLabel(ln);
        visitLineNumber(count, ln);
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        count++;
        Label ln=new Label();
        visitLabel(ln);
        visitLineNumber(count, ln);
        super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        count++;
        Label ln=new Label();
        visitLabel(ln);
        visitLineNumber(count, ln);
        super.visitVarInsn(opcode, var);
    }
    
}
