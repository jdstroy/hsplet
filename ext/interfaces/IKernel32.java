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
public interface IKernel32 {
    /*
    #uselib "kernel32.dll" {
    public int RemoveDirectoryA ( str);
    #cfunc GetLastError "GetLastError");
    public int CreateMutexA ( sptr, int, sptr);
    public int CloseHandle ( sptr);
    public int GetLastError ();
    #cfunc LCMapStringA "LCMapStringA" int, int, sptr, int, Operand, int);
    #cfunc GetUserDefaultLCID "GetUserDefaultLCID");
    }
     */

    public int CreateHandle(Operand securityPointer, int inherit, hsplet.variable.StringScalar lpcstr);

    public int CloseHandle(int handle);

    public int GetLastError();

    public int LCMapString(
            int Locale,
            int dwMapFlags,
            hsplet.variable.StringScalar lpSrcStr,
            int cchSrc,
            hsplet.variable.Operand lpDestStr,
            int cchDest);
    public int RemoveDirectoryA (String str);
    public int GetUserDefaultLCID();

}
