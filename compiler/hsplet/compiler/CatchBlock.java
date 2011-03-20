/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.compiler;

import org.objectweb.asm.Label;

/**
 *
 * @author jdstroy
 */
class CatchBlock {

    Label start;
    Label end;
    Label handler;
    String type;

    public CatchBlock(Label start, Label end, Label handler, String type) {
    }
}
