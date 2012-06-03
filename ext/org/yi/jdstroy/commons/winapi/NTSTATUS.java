/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yi.jdstroy.commons.winapi;

/**
 *
 * @author jdstroy
 */
public enum NTSTATUS {

    STATUS_SUCCESS(0), 
    STATUS_ACCESS_DENIED(0xc0000022);
    private int value;

    public int value() {
        return value;
    }

    private NTSTATUS(int value) {
        this.value = value;
    }
}
