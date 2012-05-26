
import hsplet.Context;
import hsplet.function.FunctionBase;
import hsplet.gui.Bmscr;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jdstroy
 */
public class water extends FunctionBase {

    private Context context;

    public water(final Context context) {
        this.context = context;
    }
    
    public int water_getimage(int z, int a, int b, int c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int water_setripple(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int water_calc(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public int water_draw(int bmscr_index, int a, int b, int c) {
        Bmscr el = context.windows.get(bmscr_index); // Just guessing here
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
