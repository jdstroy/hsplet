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
public interface IZ {

    public int _zOpen(Operand a, String b, int c, int d);

    public int _zRead(Operand a, int b, int c, int d);

    public int _zWrite(Operand a, int b, int c, int d);

    public int _zClose(int a, int b, int c, int d);
}
