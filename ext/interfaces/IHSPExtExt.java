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
public interface IHSPExtExt {

    public int gfini(Operand bmscr, int a, int b, int c);

    public int gfdec(int a, int b, int c, int d);

    public int gfdec2(int a, int b, int c, int d);

    public int gfinc(int a, int b, int c, int d);

    public int ematan(Operand a, int b, int c, int d);

    public int aplsel(Operand bmscr, String str, int a, Operand prefstr);

    public int aplobj(Operand bmscr, String str, int a, Operand prefstr);

    public int apledit(Operand a, int b, int c, Operand prefstr);
}
