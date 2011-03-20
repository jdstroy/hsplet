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
public interface IExRand {

    public int _exrand_randomize(int a, int b, int c, int d);

    public int _exrand_rnd(Operand a, int b, int c, int d);
}
