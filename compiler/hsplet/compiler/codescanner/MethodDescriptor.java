/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.compiler.codescanner;

/**
 *
 * @author jdstroy
 */
public class MethodDescriptor {

    public String name, desc, signature;

    public MethodDescriptor(String name, String desc, String signature) {
        this.name = name;
        this.desc = desc;
        this.signature = signature;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public String getSignature() {
        return signature;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
    
}
