/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.compiler.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.objectweb.asm.*;

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

    public static void main(String[] args) {
        try {
            FileOutputStream fos = null;
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            new ClassReader(args[0]).accept(new OpcodeIndexAsLineClassAdapter(writer), 0);
            try {
                fos = new FileOutputStream(args[1]);
                fos.write(writer.toByteArray());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(OpcodeIndexAsLineClassAdapter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(OpcodeIndexAsLineClassAdapter.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fos.close();
                } catch (IOException ex) {
                    Logger.getLogger(OpcodeIndexAsLineClassAdapter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(OpcodeIndexAsLineClassAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
