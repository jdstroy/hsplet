/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import hsplet.variable.Operand;

/**
 *
 * @author jdstroy
 */
public interface IHSPDA {

    public int _sortnote(Operand pexinfo, Object n, Object m, Object o);

    public int _xnotesel(Operand pval, int a, int b, int c);

    public int _xnoteadd(Operand bmscr, String str, int a, int b);
}
