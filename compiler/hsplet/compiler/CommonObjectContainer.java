/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.compiler;

/**
 * @author jdstroy
 */
class CommonObjectContainer {

    public Object o;
    public int localIndex;

    public CommonObjectContainer(Object o, int localIndex) {
        this.o = o;
        this.localIndex = localIndex;
    }
}
