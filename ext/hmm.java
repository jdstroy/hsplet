
import hsplet.Context;
import hsplet.HSPError;
import hsplet.PEXInfo;
import hsplet.function.FunctionBase;
import hsplet.gui.Bmscr;
import hsplet.variable.Operand;
import hsplet.variable.Scalar;
import hsplet.variable.Variable;

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
    
    public void DSINIT(int bmscr_window_id, int a, int b, int c) {
        if (bmscr_window_id < 0 || bmscr_window_id >= context.windows.size() || context.windows.get(bmscr_window_id) == null) {
            context.error(HSPError.InvalidParameterValue, "grotate", "id==" + bmscr_window_id);
        }

        final Bmscr target = context.windows.get(bmscr_window_id);
        /* Looks like we need not do anything at all! */
        
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public void DSEND(int a, int b, int c, int d) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int DSRELEASE(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public int DSLOADFNAME(@PEXInfo String filename, int index, int o, int p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int DSPLAY(int sound, int lp, int c, int d) {
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

    
    public int DMLOADFNAME(@PEXInfo String filename, int z, int b, int c) {
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

    /**
     * Turns on bit position in target.
     * @param target
     * @param position
     * @return 
     */
    public int HMMBITON(Operand target, int index, int position) {
        target.or(index, Scalar.fromValue(1 << (position)), 0);
        return 1;
    }

    
    /**
     * Turns off bit position in target.
     * @param target
     * @param position
     * @return 
     */
    public int HMMBITOFF(Operand target, int index, int position) {
        target.and(0, Scalar.fromValue(~(1 << (position))), 0);
        return 1;
    }

    /**
     * Returns the value of the bit in position of target.
     * @param target
     * @param position
     * @return 
     */
    public int HMMBITCHECK(Operand target, int index, int position) {
        return ((target.toInt(index) >> position) & 1);
    }

}
