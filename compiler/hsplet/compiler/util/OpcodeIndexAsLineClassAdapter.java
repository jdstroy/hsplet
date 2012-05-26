/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.compiler.util;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 *
 * @author jdstroy
 */
public class OpcodeIndexAsLineClassAdapter extends ClassAdapter {

    public OpcodeIndexAsLineClassAdapter(ClassVisitor cv) {
        super(cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return new OpcodeIndexAsLineMethodAdapter(super.visitMethod(access, name, desc, signature, exceptions));
    }
}
