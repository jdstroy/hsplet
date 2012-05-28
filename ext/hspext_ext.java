
import hsplet.Context;
import hsplet.function.FunctionBase;
import hsplet.gui.Bmscr;
import hsplet.variable.Operand;
import hsplet.variable.Scalar;
import interfaces.IHSPExtExt;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author jdstroy
 */
public class hspext_ext extends FunctionBase {

    private Context context;

    public hspext_ext(final Context context) {
        this.context = context;
    }

    /**
     * Gets information about an edit control selected through a call to aplobj
     * Writes information to numericTarget at index.
     *
     * mode: 0 - The current cursor position. rowIndex is ignored. 1 - The
     * number of lines. rowIndex is ignored. 2 - The number of characters in
     * rowIndex.
     *
     * @param numericTarget
     * @param index
     * @param mode
     * @param rowIndex
     */
    public void apledit(Operand numericTarget, int index, int mode, int rowIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void aplobj(String objname, int a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void aplsel(Operand destination, int index, String name, int startId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void ematan(Operand destination, int index, double x, double y) {
        destination.add(index, Scalar.fromValue(Math.atan2(y, x)), 0);
    }

    /**
     * Subtracts the specified colors to the pixels in the bounding box given by
     * gfini
     *
     * @param red The amount of red to subtract from all the pixels
     * @param green The amount of green to subtract from all the pixels
     * @param blue The amount of blue to subtract from all the pixels
     */
    public void gfdec(int red, int green, int blue) {
        final Bmscr win = context.windows.get(context.targetWindow);

        final Graphics2D g = win.backGraphics;

        final int x1 = win.cx;
        final int x2 = win.cx + x_width;
        final int y1 = win.cy;
        final int y2 = win.cy + y_height;

        // 終点は書いてはダメ

        if (x2 >= 0 && y2 >= 0 && x2 < win.backImage.getWidth() && y2 < win.backImage.getHeight()) {
            final int last = win.backImage.getRGB(x2, y2);
            
            __gfadd(win.backImage, x1, y1, x2, y2, -red, -green, -blue);
            
            win.backImage.setRGB(x2, y2, last);
        } else {

            g.drawLine(x1, y1, x2, y2);
        }

        final int l = Math.min(x1, x2);
        final int t = Math.min(y1, y2);
        final int r = Math.max(x1, x2);
        final int b = Math.max(y1, y2);

        win.redraw(l, t, r - l, b - t);
    }

    public void gfdec2(int red, int green, int blue) {
        final Bmscr win = context.windows.get(context.targetWindow);

        final Graphics2D g = win.backGraphics;

        final int x1 = win.cx;
        final int x2 = win.cx + x_width;
        final int y1 = win.cy;
        final int y2 = win.cy + y_height;

        // 終点は書いてはダメ

        if (x2 >= 0 && y2 >= 0 && x2 < win.backImage.getWidth() && y2 < win.backImage.getHeight()) {
            final int last = win.backImage.getRGB(x2, y2);
            
            __gfadd2(win.backImage, x1, y1, x2, y2, -red, -green, -blue);
            
            win.backImage.setRGB(x2, y2, last);
        } else {
            g.drawLine(x1, y1, x2, y2);
        }

        final int l = Math.min(x1, x2);
        final int t = Math.min(y1, y2);
        final int r = Math.max(x1, x2);
        final int b = Math.max(y1, y2);

        win.redraw(l, t, r - l, b - t);
    }
    
    /** Adds red, green, and blue to all pixels in the bounding box specified by
     * the coordinates (startx, starty) and (endx, endy).
     * @param target BufferedImage on which to draw
     * @param startx Starting x coordinate
     * @param starty Starting y coordinate
     * @param endx Ending x coordinate
     * @param endy Ending y coordinate
     * @param add_red Amount to add (subtract if negative) to red 
     * @param add_green Amount to add (subtract if negative) to green
     * @param add_blue Amount to add (subtract if negative) to blue
     */
    private void __gfadd(BufferedImage target, int startx, int starty, 
            int endx, int endy, 
            int add_red, int add_green, int add_blue) {
        
        // We're doing this because we aren't guaranteed that they will be in 
        // order.
        int x0, y0, x1, y1;
        
        if (startx < endx) {
            x0 = startx;
            x1 = endx;
        } else { // Equality? Do nothing?
            x1 = startx;
            x0 = endx;
        }
        
        if (starty < endy) {
            y0 = starty;
            y1 = endy;
        } else { // Equality? Do nothing?
            y1 = starty;
            y0 = endy;
        }
        
        for(int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                int argb = target.getRGB(x, y);
                int alpha = argb >> 24;
                int new_red = Math.min(Math.max(((argb >> 16) % 0xff) + add_red, 0), 255);
                int new_green = Math.min(Math.max(((argb >> 8) % 0xff) + add_green, 0), 255);
                int new_blue = Math.min(Math.max((argb % 0xff) + add_blue, 0), 255);
                target.setRGB(x, y, ((alpha << 24) | (new_red << 16) | (new_green << 8) | new_blue));
            }
        }
    }

    /**
     * Bounded version of __gfadd at argb=(0,0,0)
     * @param target BufferedImage on which to draw
     * @param startx Starting x coordinate
     * @param starty Starting y coordinate
     * @param endx Ending x coordinate
     * @param endy Ending y coordinate
     * @param add_red Amount to add (subtract if negative) to red 
     * @param add_green Amount to add (subtract if negative) to green
     * @param add_blue Amount to add (subtract if negative) to blue
     */
    private void __gfadd2(BufferedImage target, 
            int startx, int starty, 
            int endx, int endy, 
            int add_red, int add_green, int add_blue) {
        
        
        // We're doing this because we aren't guaranteed that they will be in 
        // order.
        int x0, y0, x1, y1;
        
        if (startx < endx) {
            x0 = startx;
            x1 = endx;
        } else { // Equality? Do nothing?
            x1 = startx;
            x0 = endx;
        }
        
        if (starty < endy) {
            y0 = starty;
            y1 = endy;
        } else { // Equality? Do nothing?
            y1 = starty;
            y0 = endy;
        }
        
        for(int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                int argb = target.getRGB(x, y);
                int alpha = argb >> 24;
                int old_red = ((argb >> 16) % 0xff);
                int old_green = ((argb >> 8) % 0xff);
                int old_blue = (argb % 0xff);
                
                int new_red = old_red < 1 ? old_red : Math.min(Math.max(old_red + add_red, 1), 255);
                int new_green = old_green < 1 ? old_green : Math.min(Math.max(old_green + add_green, 1), 255);
                int new_blue = old_blue < 1 ? old_blue : Math.min(Math.max(old_blue + add_blue, 1), 255);
                target.setRGB(x, y, ((alpha << 24) | (new_red << 16) | (new_green << 8) | new_blue));
            }
        }
    }
    /**
     * Adds the specified colors to the pixels in the bounding box given by
     * gfini
     *
     * @param red The amount of red to add to all the pixels
     * @param green The amount of green to add to all the pixels
     * @param blue The amount of blue to add to all the pixels
     */
    public void gfinc(int red, int green, int blue) {
        final Bmscr win = context.windows.get(context.targetWindow);

        final Graphics2D g = win.backGraphics;

        final int x1 = win.cx;
        final int x2 = win.cx + x_width;
        final int y1 = win.cy;
        final int y2 = win.cy + y_height;

        // 終点は書いてはダメ

        if (x2 >= 0 && y2 >= 0 && x2 < win.backImage.getWidth() && y2 < win.backImage.getHeight()) {
            final int last = win.backImage.getRGB(x2, y2);
            
            __gfadd(win.backImage, x1, y1, x2, y2, red, green, blue);
            
            win.backImage.setRGB(x2, y2, last);
        } else {

            g.drawLine(x1, y1, x2, y2);
        }

        final int l = Math.min(x1, x2);
        final int t = Math.min(y1, y2);
        final int r = Math.max(x1, x2);
        final int b = Math.max(y1, y2);

        win.redraw(l, t, r - l, b - t);
    }
    private int x_width, y_height;

    /**
     * Selects the dimensions for graphics operations
     *
     * @param x_width width of operation
     * @param y_height height of operation
     */
    public void gfini(final Context context, final Operand xv, final int xvi, final Operand yv, final int yvi) {

        final Bmscr win = context.windows.get(context.targetWindow);

        x_width = toInt(xv, xvi, win.cx);
        y_height = toInt(yv, yvi, win.cy);
    }
}
