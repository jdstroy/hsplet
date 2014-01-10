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
public interface IUser32 {

    public int CheckMenuRadioItem(Operand sptr_a, Operand sptr_b, Operand sptr_c, Operand sptr_d, Operand sptr_e);

    public int keybd_event(int a, int b, int c);

    public int GetKeyboardState(Operand a);
}
