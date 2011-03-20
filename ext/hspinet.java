
import hsplet.Context;
import hsplet.function.FunctionBase;
import hsplet.variable.ByteString;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jdstroy
 */
public class hspinet extends FunctionBase {

    private Context context;

    public hspinet(final Context context) {
        this.context = context;
    }

    public int netinit(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int netexec(String a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int neterror(ByteString pexinfo, int n, int m, int o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int neturl(String str, int z, int a, int b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int netdlname(String str, int z, int a, int b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int netrequest(String str, int z, int a, int b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
