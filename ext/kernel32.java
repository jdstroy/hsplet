
import hsplet.Context;
import hsplet.function.FunctionBase;
import hsplet.variable.ByteString;
import hsplet.variable.Operand;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yi.jdstroy.commons.winapi.NTSTATUS;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jdstroy
 */
public class kernel32 extends FunctionBase {

    public static final int NT_STATUS_SUCCESS = 0, WIN32_CONSTANT_FALSE = 0;
    private Context context;
    private int WIN32_CONSTANT_TRUE = 1;

    public kernel32(final Context context) {
        this.context = context;
    }

    public int GetLastError() {

        return 0;
    }

    public int CreateMutex(Operand securityPointer, int index, int inherit, String lpcstr) {
        Logger.getLogger(getClass().getName()).log(Level.FINE, "CreateMutex() called but not implemented - requesting \"{0}\"", lpcstr);
        return NTSTATUS.STATUS_SUCCESS.value();
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

    public int RemoveDirectoryA(String fileName) {
        try {
            File f = new File(context.resolve(fileName));
            if (!f.isDirectory()) {
                return WIN32_CONSTANT_FALSE;
            }
            return f.delete() ? WIN32_CONSTANT_TRUE : WIN32_CONSTANT_FALSE;
        } catch (URISyntaxException ex) {
            Logger.getLogger(kernel32.class.getName()).log(Level.SEVERE, null, ex);
            return WIN32_CONSTANT_FALSE;
        }
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
