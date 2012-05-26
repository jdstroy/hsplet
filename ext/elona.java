
import hsplet.Context;
import hsplet.HSPError;
import hsplet.function.FunctionBase;
import hsplet.gui.Bmscr;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author jdstroy
 */
public class elona extends FunctionBase {

    private Context context;

    public elona(final Context context) {
        this.context = context;
    }

    public int grotate(int pexinfo_source_window_id, int p2_x_coordinate, int p3_y_coordinate, int p4_rotation_angle_radians,
            int p5_x_size, int p6_y_size) {


        if (pexinfo_source_window_id < 0 || pexinfo_source_window_id >= context.windows.size() || context.windows.get(pexinfo_source_window_id) == null) {
            context.error(HSPError.InvalidParameterValue, "grotate", "id==" + pexinfo_source_window_id);
        }

        final Bmscr target = context.windows.get(pexinfo_source_window_id);
        
        throw new UnsupportedOperationException("Not implemented");
        //return 0;
    }
}
