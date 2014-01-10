/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.compiler.codescanner;

import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;

/**
 *
 * @author jdstroy
 */
public class CodeScanningClassAdapter extends ClassAdapter {

    public CodeScanningClassAdapter(ClassVisitor cv) {
        super(cv);
    }
    
    private List<ICodeScanningVisitorResult> results = new ArrayList<ICodeScanningVisitorResult>();
    
    @Override
    public CodeScanningMethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        CodeScanningMethodVisitor mv = new CodeScanningMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
        AICodeScanningVisitorResult result = new AICodeScanningVisitorResult(new MethodDescriptor(name, desc, signature));
        mv.setResultReference(result);
        results.add(result);
        return mv;
    }

    public List<ICodeScanningVisitorResult> getResults() {
        return results;
    }
}
