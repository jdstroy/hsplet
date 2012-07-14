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
import hsplet.variable.Scalar;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author jdstroy
 */
public class z extends FunctionBase {

    private Context context;

    public z(Context context) {
        this.context = context;
    }
    private TreeMap<Integer, InputStream> inHandles = new TreeMap<Integer, InputStream>();
    private TreeMap<Integer, OutputStream> outHandles = new TreeMap<Integer, OutputStream>();
    private int serial = 0;

    public int zOpen(Operand handle, int index, String path, int fileMode, int modeB) {
        Logger.getLogger(z.class.getName()).log(Level.FINE,
                "zOpen(\"{0}\", {1}, {2})", new Object[]{
                    path, fileMode, modeB
                });
        try {
            URI new_path = context.resolve(path);

            Logger.getLogger(z.class.getName()).log(Level.FINER, 
                    "zOpen(\"{0}\") converted to zOpen(\"{1}\")", 
                    new Object[]{path, new_path});
            try {
                handle.assign(index, Scalar.fromValue(serial), 0);
                if (fileMode == 0) {
                    GZIPInputStream in = new GZIPInputStream(new FileInputStream(new File(new_path)));
                    inHandles.put(new Integer(serial), in);
                } else {
                    GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(new File(new_path)));
                    outHandles.put(new Integer(serial), out);
                }
                serial++;
            } catch (IOException ex) {
                Logger.getLogger(z.class.getName()).log(Level.SEVERE, null, ex);
                handle.assign(0, Scalar.fromValue(-1), 0);
                return -1;
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(z.class.getName()).log(Level.SEVERE, null, ex);
            handle.assign(0, Scalar.fromValue(-1), 0);
            return -1;
        }
        return 0;
    }

    public int zRead(Operand a, int index, int handle, int size) {
        try {
            for (int i = 0; i < size; i++) {
                int value = inHandles.get(handle).read();
                if (value != -1) {
                    a.poke(index, i, (byte) value);
                } else {
                    a.poke(index, i, (byte) 0);
                }
            }
            return 1;
        } catch (IOException ex) {
            Logger.getLogger(z.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public int zWrite(Operand a, int index, int handle, int size) {
        try {
            for (int i = 0; i < size; i++) {
                outHandles.get(handle).write(a.peek(index, i));
            }
            return 1;
        } catch (IOException ex) {
            Logger.getLogger(z.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public int zClose(int handleId) {
        try {
            if (inHandles.containsKey(new Integer(handleId))) {
                inHandles.remove(new Integer(handleId)).close();
            }
            if (outHandles.containsKey(handleId)) {
                outHandles.remove(new Integer(handleId)).close();
            }

        } catch (IOException ex) {
            Logger.getLogger(z.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
