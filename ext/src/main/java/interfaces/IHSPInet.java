/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import hsplet.variable.Operand;
import hsplet.variable.Reference;

/**
 *
 * @author jdstroy
 */
public interface IHSPInet {

    public int netinit(int a, int b, int c, int d);

    public int netexec(Operand a, int b, int c, int d);

    public int neterror(Reference pexinfo, Object n, Object m, Object o);

    public int neturl(Reference bmscr, String str, int a, int b);

    public int netdlname(Reference bmscr, String str, int a, int b);

    public int netrequest(Reference bmscr, String str, int a, int b);
}
