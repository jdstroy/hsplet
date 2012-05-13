/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
