
import hsplet.Context;
import hsplet.function.FunctionBase;
import hsplet.variable.ByteString;
import hsplet.variable.Operand;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jdstroy
 */
public class kernel32 extends FunctionBase {

    public static final int NT_STATUS_SUCCESS = 0, W32_STATUS_ZERO_FAIL = 0;
    private Context context;
    private int STATUS_DELETED = 1;

    public kernel32(final Context context) {
        this.context = context;
    }

    public int GetLastError() {

        return 0;
    }

    public int CreateMutex(Operand securityPointer, int index, int inherit, String lpcstr) {
        return NT_STATUS_SUCCESS;
    }

    public int CreateMutexA(Operand securityPointer, int index, int inherit, String lpcstr) {
        return CreateMutex(securityPointer, index, inherit, lpcstr);
    }
    private Map<Integer, Object> handleMap = new TreeMap<Integer, Object>();

    public int CloseHandle(int handle) {
        handleMap.remove(handle);
        return 1;
    }

    public int LCMapStringA(
            int Locale,
            int dwMapFlags,
            String lpSrcStr,
            int cchSrc,
            String lpDestStr,
            int cchDest) {

        return 0;
    }

    public int RemoveDirectoryA(String str) {
        File f = new File(str);
        if (!f.isDirectory()) {
            return W32_STATUS_ZERO_FAIL;
        }
        return f.delete() ? STATUS_DELETED : W32_STATUS_ZERO_FAIL;
    }

    private boolean removeRecursive(File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (File g : files) {
                removeRecursive(g);
            }
        }
        return f.delete();
    }

    public int GetUserDefaultLCID() {
        return 1033;
    }
}
