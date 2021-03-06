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

import org.objectweb.asm.AnnotationVisitor;

/**
 *
 * @author jdstroy
 */
class NullAnnotationVisitor implements AnnotationVisitor {

    public NullAnnotationVisitor() {
    }

    @Override
    public void visit(String name, Object value) {
        
    }

    @Override
    public void visitEnum(String name, String desc, String value) {
        
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String desc) {
        return this;
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        return this;
    }

    @Override
    public void visitEnd() {
        
    }
    
}
