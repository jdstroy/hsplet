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

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 *
 * @author jdstroy
 */
public class OpcodeIndexAsLineMethodAdapter extends CodeOpcodeCounter {

    public OpcodeIndexAsLineMethodAdapter(MethodVisitor mv) {
        super(mv);
    }
    
    private void insertDebugLine() {
        Label label = new Label();
        visitLabel(label);
        visitLineNumber(getCount(), label);
    }

    @Override
    public void visitFieldInsn(int var, String owner, String name, String desc) {
        insertDebugLine();
        super.visitFieldInsn(var, owner, name, desc);
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        insertDebugLine();
        super.visitIincInsn(var, increment);
    }

    @Override
    public void visitInsn(int opcode) {
        insertDebugLine();
        super.visitInsn(opcode);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        insertDebugLine();
        super.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        insertDebugLine();
        super.visitJumpInsn(opcode, label);
    }

    @Override
    public void visitLdcInsn(Object cst) {
        insertDebugLine();
        super.visitLdcInsn(cst);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        insertDebugLine();
        super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        insertDebugLine();
        super.visitMethodInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        insertDebugLine();
        super.visitMultiANewArrayInsn(desc, dims);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
        insertDebugLine();
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        insertDebugLine();
        super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        insertDebugLine();
        super.visitVarInsn(opcode, var);
    }
    
}
