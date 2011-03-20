
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
public class hmm extends FunctionBase  {

    private Context context;

    public hmm(final Context context) {
        this.context = context;
    }
    
    public int DSINIT(int bmscr, int a, int b, int c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int DSEND(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int DSRELEASE(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int DSLOADFNAME(ByteString pexinfo, int n, int o, int p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int DSPLAY(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int DSSTOP(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int DSSETVOLUME(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int DSGETMASTERVOLUME(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int CHECKPLAY(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int DMINIT(int bmscr, int a, int b, int c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int DMEND(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int DMLOADFNAME(ByteString a, int z, int b, int c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int DMPLAY(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int DMSTOP(int bmscr, int a, int b, int c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int DIINIT(int bmscr, int a, int b, int c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int DIGETJOYNUM(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int DIGETJOYSTATE(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int HMMBITON(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int HMMBITOFF(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int HMMBITCHECK(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
