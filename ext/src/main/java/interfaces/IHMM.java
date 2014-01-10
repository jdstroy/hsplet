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
public interface IHMM {

public int DSINIT ( Operand bmscr, int a, int b, int c);
public int DSEND ( int a, int b, int c, int d);
public int DSRELEASE ( int a, int b, int c, int d);
public int DSLOADFNAME ( Operand pexinfo, Object n, Object o, Object p);
public int DSPLAY ( int a, int b, int c, int d);
public int DSSTOP ( int a, int b, int c, int d);
public int DSSETVOLUME ( int a, int b, int c, int d);
public int DSGETMASTERVOLUME ( int a, int b, int c, int d);
public int CHECKPLAY ( int a, int b, int c, int d);
public int DMINIT ( Operand bmscr, int a, int b, int c);
public int DMEND ( int a, int b, int c, int d);
public int DMLOADFNAME ( Operand bmscr, String a, int b, int c);
public int DMPLAY ( int a, int b, int c, int d);
public int DMSTOP ( Operand bmscr, int a, int b, int c);
public int DIINIT ( Operand bmscr, int a, int b, int c);
public int DIGETJOYNUM ( int a, int b, int c, int d);
public int DIGETJOYSTATE ( Operand a, int b, int c, int d);
public int HMMBITON ( Operand a, int b, int c, int d);
public int HMMBITOFF ( Operand a, int b, int c, int d);
public int HMMBITCHECK ( int a, int b, int c, int d);
}
