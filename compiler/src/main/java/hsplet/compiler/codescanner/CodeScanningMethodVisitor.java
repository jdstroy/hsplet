/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.compiler.codescanner;

import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;

/**
 *
 * @author jdstroy
 * 
 * This method visitor is used on a preprocessing pass of the code generator.
 * It is used to collect statistics for later optimization.
 * Currently, we can use this adapter for a first pass which examines the fields 
 * that are used in a class.
 */
public class CodeScanningMethodVisitor extends MethodAdapter {

    private int maxVars = -1;
    
    public CodeScanningMethodVisitor(MethodVisitor mv) {
        super(mv);
    }
    
    private ICodeScanningVisitorResult result;
    public void setResultReference(ICodeScanningVisitorResult result) {
        this.result = result;
    }
    

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        FieldDescriptor key = new FieldDescriptor(owner, name, desc);
        result.incrementFieldUsage(key);
        super.visitFieldInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        result.checkMaximumLocal(var);
        super.visitVarInsn(opcode, var);
    }

    public int getMaxVars() {
        return maxVars;
    }
    
}