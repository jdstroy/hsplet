/*
 * Copyright 2012 John Stroy
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
