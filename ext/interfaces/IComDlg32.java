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
public interface IComDlg32 {
public int GetOpenFileNameA ( Operand sptr);
public int GetSaveFileNameA ( Operand sptr);

}
