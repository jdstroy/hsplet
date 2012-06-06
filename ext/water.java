
import hsplet.Application;
import hsplet.Context;
import hsplet.function.FunctionBase;
import hsplet.gui.Bmscr;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "water_getimage() called but unimplemented");
        return 0;
    }

    
    public int water_setripple(int a, int b, int c, int d) {
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "water_setripple() called but unimplemented");
        return 0;
    }

    
    public int water_calc(int a, int b, int c, int d) {
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "water_calc() called but unimplemented");
        return 0;
    }

    
    public int water_draw(int bmscr_index, int a, int b, int c) {
        Bmscr el = context.windows.get(bmscr_index); // Just guessing here
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "water_draw() called but unimplemented");
        return 0;
    }

}
