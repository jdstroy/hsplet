
import hsplet.Context;
import hsplet.function.FunctionBase;
import hsplet.gui.Bmscr;
import hsplet.gui.Mesbox;
import hsplet.variable.Operand;
import hsplet.variable.Scalar;
import java.util.List;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.text.JTextComponent;

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
     * @param destination
     * @param index
     * @param mode
     * @param rowIndex
     */
    public void apledit(Operand destination, int index, int mode, int rowIndex) {

        Component component = aplObjTarget;
        _apledit(component, destination, index, mode, rowIndex);

    }

    private void _apledit(Component component, Operand destination, int index, int mode, int rowIndex) {
        String text;
        if (JTextComponent.class.isInstance(component)) {
            JTextComponent jtc = JTextComponent.class.cast(component);
            text = jtc.getText();
        } else if (TextComponent.class.isInstance(component)) {
            TextComponent tc = TextComponent.class.cast(component);
            text = tc.getText();
        } else if (Mesbox.class.isInstance(component)) {
            Mesbox inst = Mesbox.class.cast(component);
            _apledit(inst.getTextArea(), destination, index, mode, rowIndex);
            return;
        } else {
            return;
        }

        int v;
        switch (mode) {
            case 0: //get 1 byte
                // http://usk.s16.xrea.com/hsp/hsphelp_eng/hsp255_eng/hsphelp/help_a.htm#s_apledit
                byte[] textArr = text.getBytes();
                int textIndex = destination.toInt(index);
                if (textIndex < textArr.length && textIndex > 0) {
                    v = textArr[textIndex];
                    context.stat.value = 0; //?
                } else {
                    context.stat.value = 1; // fail?
                    return;
                }
                break;
            case 1: //get number of rows
                v = text.split("\n").length;
                break;
            case 2: //get the number of characters in the line at rowIndex
                v = text.split("\n")[rowIndex].getBytes().length;
                break;
            default:
                return;
        }
        destination.assign(index, Scalar.fromValue(v), 0);
    }
    private Component aplObjTarget = null;

    private static Component _safeParent(Component c) {
        return (c == null) ? c : c.getParent();
    }

    public void aplobj(String objClassName, int index) {
        Logger.getLogger(getClass().getName()).log(Level.INFO,
                "aplobj requested classname {0}", objClassName);
        hsplet.Applet applet = context.applet;
        Component hspletWindow = _safeParent(_safeParent(_safeParent(applet)));
        Window selectedWindow = aplSelTarget;
        Component privateTarget;
        if (hspletWindow != null) {
            if (selectedWindow == hspletWindow) {
                Logger.getLogger(getClass().getName()).log(Level.INFO,
                        "Bypassing typical mechanism for aplobj; using HSPlet internal knowledge");
                privateTarget = context.windows.get(context.activeWindow).controls.get(index).asComponent();
                context.refstr.value.assign(privateTarget.getClass().getName());
                aplObjTarget = privateTarget;
                context.stat.value = 0;
                return;
            }
        }
        synchronized (aplSelTarget.getTreeLock()) {
            Component[] components = aplSelTarget.getComponents();
            if (index < components.length && index >= 0) {
                privateTarget = components[index];
                aplObjTarget = privateTarget;
                context.stat.value = 0;
                context.refstr.value.assign(privateTarget.getClass().getName());
            } else {
                Logger.getLogger(getClass().getName()).log(Level.WARNING,
                        "aplobj requested invalid index {0}", index);
                aplObjTarget = null;
                context.stat.value = 1;
            }
        }
    }
    private Window aplSelTarget = null;

    public void aplsel(String windowName, int startId) {
        Window[] windows = Window.getWindows();
        List<Window> tempFrames = new ArrayList<Window>();
        for (Window w : windows) {
            if (windowName.isEmpty()) {
                tempFrames.add(w);
            } else if (Frame.class.isInstance(w)) {
                Frame f = Frame.class.cast(w);
                if (f.getTitle().contains(windowName)) {
                    tempFrames.add(f);
                }
            }
        }
        if (startId < tempFrames.size() && startId >= 0) {
            aplSelTarget = tempFrames.get(startId);
            context.stat.value = 0;
        } else {
            aplSelTarget = null;
            context.stat.value = 1;
        }
    }

    public void ematan(Operand destination, int index, double x, double y) {
        destination.assign(index, Scalar.fromValue(Math.atan2(y, x)), 0);
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

        final int x1 = ___bound(win.cx, 0, win.backImage.getWidth());
        final int x2 = ___bound(win.cx + x_width, 0, win.backImage.getWidth());
        final int y1 = ___bound(win.cy, 0, win.backImage.getHeight());
        final int y2 = ___bound(win.cy + y_height, 0, win.backImage.getHeight());

        __gfadd(win.backImage, x1, y1, x2, y2, -red, -green, -blue);

        final int l = Math.min(x1, x2);
        final int t = Math.min(y1, y2);
        final int r = Math.max(x1, x2);
        final int b = Math.max(y1, y2);

        win.redraw(l, t, r - l, b - t);
    }

    private static int ___bound(int input, int lower, int upper) {
        return Math.max(Math.min(input, upper), lower);
    }

    public void gfdec2(int red, int green, int blue) {
        final Bmscr win = context.windows.get(context.targetWindow);

        final Graphics2D g = win.backGraphics;

        final int x1 = ___bound(win.cx, 0, win.backImage.getWidth());
        final int x2 = ___bound(win.cx + x_width, 0, win.backImage.getWidth());
        final int y1 = ___bound(win.cy, 0, win.backImage.getHeight());
        final int y2 = ___bound(win.cy + y_height, 0, win.backImage.getHeight());

        __gfadd2(win.backImage, x1, y1, x2, y2, -red, -green, -blue);

        final int l = Math.min(x1, x2);
        final int t = Math.min(y1, y2);
        final int r = Math.max(x1, x2);
        final int b = Math.max(y1, y2);

        win.redraw(l, t, r - l, b - t);
    }

    /**
     * Adds red, green, and blue to all pixels in the bounding box specified by
     * the coordinates (startx, starty) and (endx, endy).
     *
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

        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                int argb = target.getRGB(x, y);
                int alpha = argb >> 24;
                int new_red = Math.min(Math.max(((argb >> 16) & 0xff) + add_red, 0), 255);
                int new_green = Math.min(Math.max(((argb >> 8) & 0xff) + add_green, 0), 255);
                int new_blue = Math.min(Math.max((argb & 0xff) + add_blue, 0), 255);
                target.setRGB(x, y, ((alpha << 24) | (new_red << 16) | (new_green << 8) | new_blue));
            }
        }
    }

    /**
     * Bounded version of __gfadd at argb=(0,0,0)
     *
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

        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                int argb = target.getRGB(x, y);
                int alpha = argb >> 24;
                int old_red = ((argb >> 16) & 0xff);
                int old_green = ((argb >> 8) & 0xff);
                int old_blue = (argb & 0xff);

                int new_red = old_red < 1 ? old_red : Math.min(Math.max(old_red + add_red, 1), 255);
                int new_green = old_green < 1 ? old_green : Math.min(Math.max(old_green + add_green, 1), 255);
                int new_blue = old_blue < 1 ? old_blue : Math.min(Math.max(old_blue + add_blue, 1), 255);

                target.setRGB(x, y, ((alpha << 24) | (new_red << 16) | (new_green << 8) | new_blue));
                //target.setRGB(x, y, argb);
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

        final int x1 = ___bound(win.cx, 0, win.backImage.getWidth());
        final int x2 = ___bound(win.cx + x_width, 0, win.backImage.getWidth());
        final int y1 = ___bound(win.cy, 0, win.backImage.getHeight());
        final int y2 = ___bound(win.cy + y_height, 0, win.backImage.getHeight());

        __gfadd(win.backImage, x1, y1, x2, y2, red, green, blue);

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
