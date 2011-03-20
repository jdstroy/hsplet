/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.compiler;

/**
 *
 * @author jdstroy
 */
public class DllCtrlClassStub {

    public static final int OP_DLLCTRL = 17;

    public static String getMethodNameBySubTypeId(int subType) {
        if (subType < 0x100) {
            return LOW_METHOD_NAMES[subType];
        } else {
            return HIGH_METHOD_NAMES[subType - 0x100];
        }
    }
    /** Starts at 100 */
    private static final String[] HIGH_METHOD_NAMES = new String[]{
        "callfunc",
        "cnvwtos",
        "comevdisp",
        "libptr",};
    /** Starts at 0 */
    private static final String[] LOW_METHOD_NAMES = new String[]{
        "newcom",
        "querycom",
        "delcom",
        "cnvstow",
        "comres",
        "axobj",
        "winobj",
        "sendmsg",
        "comevent",
        "comevarg",
        "sarrayconv",};
}
