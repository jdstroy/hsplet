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
public interface IWater {

    public int _water_getimage(int bmscr, int a, int b, int c);

    public int _water_setripple(int a, int b, int c, int d);

    public int _water_calc(int a, int b, int c, int d);

    public int _water_draw(int bmscr, int a, int b, int c);
}
