/*
 * Copyright 2012 John Stroy
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import hsplet.Context;
import hsplet.function.FunctionBase;
import hsplet.variable.Operand;
import java.io.File;
import java.net.URISyntaxException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yi.jdstroy.commons.winapi.*;

/**
 * Win32 API - kernel32 API for HSPlet
 *
 * @author jdstroy
 */
public class kernel32 extends FunctionBase {

    public static final int NT_STATUS_SUCCESS = 0, WIN32_CONSTANT_FALSE = 0;
    private Context context;
    private AtomicInteger lastHandle = new AtomicInteger();
    private int WIN32_CONSTANT_TRUE = 1;
    private ObjectManager objectManager = new ObjectManager();
    private ProcessContext processContext = new ProcessContext();
    private int lastError = 0;

    public kernel32(final Context context) {
        this.context = context;
    }

    public int GetLastError() {
        return lastError;
    }

    public int CreateMutex(Operand securityPointer, int index, int inherit, @LPCSTR String name) {
        //Logger.getLogger(getClass().getName()).log(Level.FINE, "CreateMutex() called but not implemented - requesting \"{0}\"", name);
        Logger.getLogger(getClass().getName()).log(Level.FINE, "CreateMutex() requesting \"{0}\"", name);

        Semaphore semaphore = new Semaphore(1);
        Semaphore currentObject = objectManager.putIfAbsent(name, semaphore, Semaphore.class);

        if (currentObject == null) {
            // Did not exist, has been put
            int handle = processContext.add(semaphore);
            return handle;
        } else {
            // Existed, open currentObject instead
            if (Semaphore.class.isInstance(currentObject)) {
                lastError = WinError.ERROR_ALREADY_EXISTS.value();
                int handle = processContext.find(semaphore);
                return handle;
            } else {
                // Not a semaphore
                return Winbase.INVALID_HANDLE;
            }
        }

    }

    public int CreateMutexA(Operand securityPointer, int index, int inherit, String lpcstr) {
        return CreateMutex(securityPointer, index, inherit, lpcstr);
    }

    public int CloseHandle(int handle) {
        processContext.remove(handle);
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
