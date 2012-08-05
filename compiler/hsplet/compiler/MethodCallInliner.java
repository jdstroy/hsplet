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

package hsplet.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.MethodNode;

/**
 *
 * @author jdstroy
 */
public class MethodCallInliner extends LocalVariablesSorter {

    private final String oldClass;
    private final String newClass;
    private final MethodNode mn;
    private List<CatchBlock> blocks = new ArrayList<CatchBlock>();
    private boolean inlining;

    public MethodCallInliner(int access, String desc, MethodVisitor mv, MethodNode mn, String oldClass, String newClass) {
        super(access, desc, mv);
        this.oldClass = oldClass;
        this.newClass = newClass;
        this.mn = mn;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        if (!canBeInlined(owner, name, desc)) {
            mv.visitMethodInsn(opcode, owner, name, desc);
            return;
        }
        final Map<String, String> map = Collections.singletonMap(oldClass, newClass);
        Remapper remapper = new Remapper() {

            @Override
            public String mapType(String type) {
                if (map.containsKey(type)) {
                    return map.get(type);
                } else {
                    return super.mapType(type);
                }
            }
        };
        Label end = new Label();
        inlining = true;
        mn.instructions.resetLabels();
        mn.accept(new InliningAdapter(this, end, opcode == Opcodes.INVOKESTATIC ? Opcodes.ACC_STATIC : 0, desc, remapper));
        inlining = false;
        super.visitLabel(end);
    }

    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        if (!inlining) {
            blocks.add(new CatchBlock(start, end, handler, type));
        } else {
            super.visitTryCatchBlock(start, end, handler, type);
        }
    }

    public void visitMaxs(int stack, int locals) {
        Iterator<CatchBlock> it = blocks.iterator();
        while (it.hasNext()) {
            CatchBlock b = it.next();
            super.visitTryCatchBlock(b.start, b.end, b.handler, b.type);
        }
        super.visitMaxs(stack, locals);
    }

    private boolean canBeInlined(String owner, String name, String desc) {
        Pattern p = Pattern.compile("^m\\d+$");
        return (owner.equals("start") && p.matcher(name).matches());
    }
}
