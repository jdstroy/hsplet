
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

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
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
        try {
            URI new_path = context.resolveSafe(path);
            Logger.getLogger(z.class.getName()).log(Level.INFO, "zOpen({0})", path);
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
