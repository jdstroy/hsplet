
import hsplet.Context;
import hsplet.HSPError;
import hsplet.PEXInfo;
import hsplet.function.FunctionBase;
import hsplet.function.GuiCommand;
import hsplet.gui.Bmscr;
import hsplet.gui.GraphicsRenderer;
import hsplet.variable.Operand;

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
    
    /**
     * grotate, elona style - do not let pixels reach 0,0,0
     * Not implemented correctly.
     * @param source_window_id
     * @param p2_x_coordinate
     * @param p3_y_coordinate
     * @param p4_rotation_angle_radians
     * @param dwv
     * @param dwi
     * @param dhv
     * @param dhi 
     */

    public void grotate(@PEXInfo int source_window_id, int p2_x_coordinate,
            int p3_y_coordinate, double p4_rotation_angle_radians,
            //int p5_x_size, int p6_y_size
            final Operand dwv, final int dwi, final Operand dhv, final int dhi
            ) {
        GuiCommand.grotate(context, source_window_id, p2_x_coordinate, p3_y_coordinate, p4_rotation_angle_radians, dwv, dwi, dhv, dhi);
        /*
                final Bmscr win = context.windows.get(context.targetWindow);

		final int dw = toInt(dwv, dwi, win.gwidth);
		final int dh = toInt(dhv, dhi, win.gheight);

		final double w = dw / 2 - 0.5;
		final double h = dh / 2 - 0.5;

		final int cx = win.cx;
		final int cy = win.cy;

		final double si = Math.sin(p4_rotation_angle_radians);
		final double co = Math.cos(p4_rotation_angle_radians);

		int[] dx = new int[] { (int) Math.round(-w * co + h * si) + cx, (int) Math.round(w * co + h * si) + cx,
				(int) Math.round(w * co - h * si) + cx, (int) Math.round(-w * co - h * si) + cx, };
		int[] dy = new int[] { (int) Math.round(-w * si - h * co) + cy, (int) Math.round(w * si - h * co) + cy,
				(int) Math.round(w * si + h * co) + cy, (int) Math.round(-w * si + h * co) + cy, };
		int[] sxs = new int[] { p2_x_coordinate, p2_x_coordinate + win.gwidth - 1, p2_x_coordinate + win.gwidth - 1, p2_x_coordinate };
		int[] sys = new int[] { p3_y_coordinate, p3_y_coordinate, p3_y_coordinate + win.gheight - 1, p3_y_coordinate + win.gheight - 1 };
		
		GraphicsRenderer.gsquare(win, dx, dy, context.windows.get(source_window_id).backImage, sxs, sys);

		final int l = Math.min(Math.min(Math.min(dx[0], dx[1]), dx[2]), dx[3]);
		final int t = Math.min(Math.min(Math.min(dy[0], dy[1]), dy[2]), dy[3]);
		final int r = Math.max(Math.max(Math.max(dx[0], dx[1]), dx[2]), dx[3]);
		final int b = Math.max(Math.max(Math.max(dy[0], dy[1]), dy[2]), dy[3]);

		win.redraw(l, t, r - l, b - t);
                * 
                */

    }
}
