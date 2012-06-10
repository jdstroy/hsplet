/*
 * $Id: GuiCommand.java,v 1.14 2006/05/20 06:12:07 Yuki Exp $
 */
package hsplet.function;

import hsplet.Context;
import hsplet.HSPError;
import hsplet.gui.Bgscr;
import hsplet.gui.Bmscr;
import hsplet.gui.Buffer;
import hsplet.gui.Button;
import hsplet.gui.Chkbox;
import hsplet.gui.Combox;
import hsplet.gui.GraphicsRenderer;
import hsplet.gui.HSPControl;
import hsplet.gui.Input;
import hsplet.gui.Listbox;
import hsplet.gui.Mesbox;
import hsplet.gui.Screen;
import hsplet.media.HSPMedia;
import hsplet.variable.Operand;
import hsplet.variable.Scalar;
import hsplet.variable.Variable;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * HSP の拡張GUIコマンド群。
 *
 * @author Yuki
 * @version $Revision: 1.14 $, $Date: 2006/05/20 06:12:07 $
 */
public class GuiCommand extends FunctionBase {

    /**
     * このクラスを含むソースファイルのバージョン文字列。
     */
    private static final String fileVersionID = "$Id: GuiCommand.java,v 1.14 2006/05/20 06:12:07 Yuki Exp $";

    private static void addObject(final Context context, Bmscr win, HSPControl obj, Dimension size) {

        if (win.contents != null) {
            final Component co = obj.asComponent();

            co.setLocation(win.cx, win.cy);
            co.setSize(size);
            win.cy += Math.max(size.height, win.mindy);

            win.contents.add(co);

            context.listener.listen(obj);

            if (win.objfont != null) {
                co.setFont(win.objfont);
            }

            final int nullIndex = win.controls.indexOf(null);
            if (nullIndex < 0) {
                win.controls.add(obj);
                context.stat.value = win.controls.size() - 1;
            } else {
                win.controls.set(nullIndex, obj);
                context.stat.value = nullIndex;
            }

            obj.asComponent().setVisible(true);
            obj.asComponent().repaint();

        } else {
            context.stat.value = 0;
        }

    }

    public static void button(final Context context, final JumpStatement jump, final String text, final int label) {

        if (text == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "button", "text");
            return;
        }

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        if (win.contents != null) {

            final Button obj = new Button(text, context, jump == null ? JumpStatement.Goto : jump, label);

            addObject(context, win, obj, new Dimension(win.owidth, win.oheight));
        }
    }

    public static void chgdips(final Context context, final Operand v, final int vi) {

        context.error(HSPError.UnsupportedOperation, "chgdisp");

        context.stat.value = 2;

    }

    public static void exec(final Context context, final String fileName, final int mode, final String command) {

        if (fileName == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "exec", "fileName");
            return;
        }

        try {
            final URL url = new URL(context.curdir, fileName);
            context.showPage(url, command == null || command.length() == 0 ? "_blank" : command);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            context.error(HSPError.ErrorOnExecution, "exec", fileName);
        }

    }

    public static void dialog(final Context context, final String text, final int type, final String title) {

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        final Component parent = win.component == null ? ((Bmscr) context.windows.get(0)).component : win.component;

        switch (type) {
            case 0:
                JOptionPane.showMessageDialog(parent, text, title, JOptionPane.INFORMATION_MESSAGE);
                context.stat.value = 1;
                break;
            case 1:
                JOptionPane.showMessageDialog(parent, text, title, JOptionPane.WARNING_MESSAGE);
                context.stat.value = 1;
                break;
            case 2:
                switch (JOptionPane.showConfirmDialog(parent, text, title, JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE)) {
                    case JOptionPane.YES_OPTION:
                        context.stat.value = 6;
                        break;
                    case JOptionPane.NO_OPTION:
                        context.stat.value = 7;
                        break;
                }
                break;
            case 3:
                switch (JOptionPane.showConfirmDialog(parent, text, title, JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE)) {
                    case JOptionPane.YES_OPTION:
                        context.stat.value = 6;
                        break;
                    case JOptionPane.NO_OPTION:
                        context.stat.value = 7;
                        break;
                }
                break;
            case 16:
            case 17: {

                final JFileChooser chooser = new JFileChooser(context.curdir.getFile());

                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                if (text != null && !text.equals("*")) {
                    chooser.setFileFilter(new FileFilter() {

                        //@Override
                        public boolean accept(final File f) {

                            return f.isDirectory() || f.getName().toLowerCase().endsWith("." + text.toLowerCase());
                        }

                        //@Override
                        public String getDescription() {

                            return title + " (*." + text + ")";
                        }
                    });
                }

                final int result;
                if (type == 16) {
                    result = chooser.showOpenDialog(parent);
                } else {
                    result = chooser.showSaveDialog(parent);
                }

                switch (result) {
                    case JFileChooser.APPROVE_OPTION:
                        context.stat.value = 1;
                        try {
                            context.refstr.value.assign(chooser.getSelectedFile().toURL().toString());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        context.stat.value = 0;
                        break;
                }
            }
            break;
            case 32:
            case 33: {

                context.stat.value = 0;

                final JColorChooser chooser = new JColorChooser(win.color);

                JDialog dialog = JColorChooser.createDialog(parent, title, true, chooser, new ActionListener() {

                    public void actionPerformed(ActionEvent e) {

                        win.color = chooser.getColor();
                        win.backGraphics.setColor(win.color);
                        context.stat.value = 1;
                    }
                }, null);

                dialog.setVisible(true);

                // ダイアログは別スレッドなので、閉じた後若干待ち時間が必要。
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            break;
            default:
                context.error(HSPError.InvalidParameterValue, "dialog", "type==" + type);
                return;
        }
    }

    public static void mmload(final Context context, final String fileName, final int id, final int mode) {

        if (fileName == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "mmload", "fileName");
            return;
        }

        try {
            while (context.medias.size() <= id) {
                context.medias.add(null);
            }
            context.medias.set(id, context.mci.open(fileName, mode));
        } catch (Exception e) {
            e.printStackTrace();
            context.error(HSPError.FileNotFound, "mmload", fileName);
        }
    }

    public static void mmplay(final Context context, final int id) {

        if (id < 0 || id >= context.medias.size() || context.medias.get(id) == null) {
            context.error(HSPError.InvalidParameterValue, "mmplay", "id==" + id);
            return;
        }

        ((HSPMedia) context.medias.get(id)).setPosition(0);
        ((HSPMedia) context.medias.get(id)).play();
    }

    public static void mmstop(final Context context) {

        for (int i = 0; i < context.medias.size(); ++i) {
            if (context.medias.get(i) != null) {
                ((HSPMedia) context.medias.get(i)).stop();
            }
        }
    }

    public static void mci(final Context context, final String command) {

        if (command == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "mci", "command");
            return;
        }

        context.mci.exec(command);

    }

    public static void pset(final Context context, final int x, final int y) {

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        if (x >= 0 && y >= 0 && x < win.backImage.getWidth() && y < win.backImage.getHeight()) {
            win.backImage.setRGB(x, y, win.color.getRGB());
        }

        win.redraw(x, y, 1, 1);
    }

    public static void pget(final Context context, final int x, final int y) {

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        if (x >= 0 && y >= 0 && x < win.backImage.getWidth() && y < win.backImage.getHeight()) {
            win.color = new Color(win.backImage.getRGB(x, y));
        } else {
            win.color = Color.white;
        }

        final Graphics2D g = win.backGraphics;

        g.setColor(win.color);
    }

    public static void syscolor(final Context context, final int type) {

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        final Color color;
        switch (type) {
            case 0:
                color = SystemColor.scrollbar;
                break;
            case 1:
                color = SystemColor.desktop;
                break;
            case 2:
                color = SystemColor.activeCaption;
                break;
            case 3:
                color = SystemColor.inactiveCaption;
                break;
            case 4:
                color = SystemColor.menu;
                break;
            case 5:
                color = SystemColor.window;
                break;
            case 6:
                color = SystemColor.windowBorder;
                break;
            case 7:
                color = SystemColor.menuText;
                break;
            case 8:
                color = SystemColor.windowText;
                break;
            case 9:
                color = SystemColor.activeCaptionText;
                break;
            case 10:
                color = SystemColor.activeCaptionBorder;
                break;
            case 11:
                color = SystemColor.inactiveCaptionBorder;
                break;
            case 12: // MDI の背景未対応
                color = SystemColor.desktop;
                break;
            case 13:
                color = SystemColor.textHighlight;
                break;
            case 14:
                color = SystemColor.textHighlightText;
                break;
            case 15:
                color = SystemColor.control;
                break;
            case 16:
                color = SystemColor.controlShadow;
                break;
            case 17:
                color = SystemColor.textInactiveText;
                break;
            case 18:
                color = SystemColor.controlText;
                break;
            case 19:
                color = SystemColor.inactiveCaptionText;
                break;
            case 20:
                color = SystemColor.controlLtHighlight;
                break;
            case 21:
                color = SystemColor.controlDkShadow;
                break;
            case 22:
                color = SystemColor.controlHighlight;
                break;
            case 23:
                color = SystemColor.infoText;
                break;
            case 24:
                color = SystemColor.info;
                break;
            case 26: // ホットトラックアイテムの色 未対応
                color = SystemColor.controlText;
                break;
            case 27: // グラデーションの場合はアクティブウィンドウのタイトルバーの右側の色 未対応
                color = SystemColor.activeCaption;
                break;
            case 28: // グラデーションの場合は非アクティブウィンドウのタイトルバーの右側の色 未対応
                color = SystemColor.inactiveCaption;
                break;
            case 29: // XP メニューアイテムをハイライト表示するのに使用される色 未対応
                color = SystemColor.controlHighlight;
                break;
            case 30: // XP メニューバーの背景色 未対応
                color = SystemColor.menu;
                break;
            default:
                context.error(HSPError.InvalidParameterValue, "syscolor", "type==" + type);
                color = win.color;
                break;
        }

        win.color = color;
        win.backGraphics.setColor(color);

    }

    public static void mes(final Context context, final String text) {

        if (text == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "mes", "text");
            return;
        }

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);
        final Graphics2D g = win.backGraphics;

        final FontMetrics metrics = win.backGraphics.getFontMetrics();

        win.mesw = 0;
        win.mesh = metrics.getFont().getSize();

        final int ascent = metrics.getAscent() - (metrics.getHeight() - win.mesh) / 2;

        final BufferedReader r = new BufferedReader(new StringReader(text + "\r\n"));

        final Object oldAntiAliasing = win.backGraphics.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
        final Object oldRenering = win.backGraphics.getRenderingHint(RenderingHints.KEY_RENDERING);

        if ((win.fontStyle & 16) != 0) {
            win.backGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            win.backGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }

        for (;;) {
            try {
                final String line = r.readLine();
                if (line == null) {
                    break;
                }

                g.drawString(line, win.cx, win.cy + ascent);

                win.mesw = g.getFontMetrics().stringWidth(line);

                if ((win.fontStyle & 4) != 0) {
                    g.drawLine(win.cx, win.cy + win.mesh - 1, win.cx + win.mesw - 1, win.cy + win.mesh - 1);
                }
                if ((win.fontStyle & 8) != 0) {
                    g.drawLine(win.cx, win.cy + win.mesh / 2, win.cx + win.mesw - 1, win.cy + win.mesh / 2);
                }

                win.redraw(win.cx, win.cy, win.mesw, win.mesh);

                win.cy += Math.max(win.mindy, win.mesh);
            } catch (IOException e) {
            }
        }

        win.backGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, oldAntiAliasing);
        win.backGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, oldRenering);
    }

    public static void title(final Context context, final String text) {

        if (text == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "title", "text");
            return;
        }

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        win.screen.setTitle(text);
    }

    public static void pos(final Context context, final Operand xv, final int xvi, final Operand yv, final int yvi) {

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        win.cx = toInt(xv, xvi, win.cx);
        win.cy = toInt(yv, yvi, win.cy);
    }

    public static void circle(final Context context, final int x1, final int y1, final Operand x2v, final int x2vi,
            final Operand y2v, final int y2vi, final Operand typev, final int typevi) {

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        final Graphics2D g = win.backGraphics;

        final int x2 = toInt(x2v, x2vi, win.backImage.getWidth() - 1);
        final int y2 = toInt(y2v, y2vi, win.backImage.getHeight() - 1);

        final int l = Math.min(x1, x2);
        final int t = Math.min(y1, y2);
        final int r = Math.max(x1, x2) + 1;
        final int b = Math.max(y1, y2) + 1;

        if (toInt(typev, typevi, 1) == 0) {
            g.drawOval(l, t, r - l, b - t);
        } else {
            g.fillOval(l, t, r - l, b - t);
        }

        win.redraw(l, t, r - l, b - t);
    }

    public static void cls(final Context context, final int mode) {

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        final Color color;
        switch (mode) {
            default:
                color = Color.white;
                break;
            case 1:
                color = Color.lightGray;
                break;
            case 2:
                color = Color.gray;
                break;
            case 3:
                color = Color.darkGray;
                break;
            case 4:
                color = Color.black;
                break;
        }

        win.init(color);
        win.redraw(0, 0, win.backImage.getWidth(), win.backImage.getHeight());
    }

    public static void font(final Context context, final String name, final Operand sizev, final int sizevi,
            final int style) {

        if (name == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "font", "name");
            return;
        }

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        win.font = new Font(name, style & 3, toInt(sizev, sizevi, 18));
        win.fontStyle = style;

        final Graphics2D g = win.backGraphics;

        g.setFont(win.font);
    }

    public static void sysfont(final Context context, final int type) {

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        switch (type) {
            case 0:
                win.font = new Font("Monospaced", 0, 18);
                win.fontStyle = win.font.getStyle();
                break;
            case 10:
                win.font = new Font("Monospaced", 0, 18);
                win.fontStyle = win.font.getStyle();
                break;
            case 11:
                win.font = new Font("DialogInput", 0, 13);
                win.fontStyle = win.font.getStyle();
                break;
            case 12:
                win.font = new Font("Dialog", 0, 13);
                win.fontStyle = win.font.getStyle();
                break;
            case 13:
                win.font = new Font("Monospaced", 0, 18);
                win.fontStyle = win.font.getStyle();
                break;
            case 17: // デフォルト GUI フォント
                win.font = ((Bmscr) context.windows.get(0)).component.getFont();
                win.fontStyle = win.font.getStyle();
                break;
        }

        final Graphics2D g = win.backGraphics;

        g.setFont(win.font);
    }

    public static void objsize(final Context context, final Operand wv, final int wvi, final Operand hv, final int hvi,
            final int dy) {

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        win.owidth = toInt(wv, wvi, 64);
        win.oheight = toInt(hv, hvi, 24);
        win.mindy = dy;

    }

    public static void picload(final Context context, final String fileName, final int mode) {

        if (fileName == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "picload", "fileName");
            return;
        }

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        try {

            final InputStream in = context.getResource(fileName);

            if (in == null) {
                context.error(HSPError.ImageNotFound, "picload", fileName);
                return;
            }

            try {

                final BufferedImage image = ImageIO.read(in);

                if (mode == 0) {
                    win.init(Color.white, new Dimension(image.getWidth(), image.getHeight()));
                }

                win.backGraphics.drawImage(image, win.cx, win.cy, null);

                win.redraw(win.cx, win.cy, image.getWidth(), image.getHeight());

                image.flush();

            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            context.error(HSPError.ImageNotFound, "picload", fileName);
        }
    }

    public static void color(final Context context, final int red, final int green, final int blue) {

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        win.color = new Color(red % 256, green % 256, blue % 256, 255);

        final Graphics2D g = win.backGraphics;

        g.setColor(win.color);
    }

    public static void palcolor(final Context context, final Operand v, final int vi) {

        context.error(HSPError.UnsupportedOperation, "palcolor");
    }

    public static void palette(final Context context, final Operand v, final int vi) {

        context.error(HSPError.UnsupportedOperation, "palette");

    }

    public static void redraw(final Context context, final Operand modev, final int modevi, final int x, final int y,
            final Operand wv, final int wvi, final Operand hv, final int hvi) {

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        if (win.contents == null) {
            return;
        }

        final int mode = toInt(modev, modevi, 1);

        final int w = toInt(wv, wvi, win.contents.getWidth());
        final int h = toInt(hv, hvi, win.contents.getHeight());

        if (w < 0) {
            context.error(HSPError.InvalidParameterValue, "width", "w==" + w);
            return;
        }

        if (h < 0) {
            context.error(HSPError.InvalidParameterValue, "width", "h==" + h);
            return;
        }

        win.redraw = mode & 1;

        if ((mode & 2) == 0) {
            win.redraw(x, y, w, h);
        }

    }

    public static void width(final Context context, final Operand wv, final int wvi, final Operand hv, final int hvi,
            final Operand lv, final int lvi, final Operand tv, final int tvi) {

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        if (win.contents == null) {
            return;
        }

        final int w = toInt(wv, wvi, win.contents.getWidth());
        final int h = toInt(hv, hvi, win.contents.getHeight());

        if (w < 0) {
            context.error(HSPError.InvalidParameterValue, "width", "w==" + w);
            return;
        }

        if (h < 0) {
            context.error(HSPError.InvalidParameterValue, "width", "h==" + h);
            return;
        }
        try {
            EventQueue.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    if (win.contents != null) {
                        win.contents.setPreferredSize(new Dimension(w, h));
                        win.contents.setSize(w, h);
                    }
                    if (win.window != null) {

                        final int l = toInt(hv, hvi, win.window.getX());
                        final int t = toInt(tv, tvi, win.window.getY());

                        win.window.setLocation(l, t);
                    }

                }
            });
        } catch (InterruptedException ex) {
            Logger.getLogger(GuiCommand.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(GuiCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void gsel(final Context context, final int id, final int mode) {

        if (id < 0 || id >= context.windows.size() || context.windows.get(id) == null) {
            context.error(HSPError.InvalidParameterValue, "gsel", "id==" + id);
        }

        context.targetWindow = id;
        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        switch (mode) {
            case -1:
                if (win.component != null) {
                    win.component.setVisible(false);
                }
                break;
            case 0:
                break;
            case 1:
                try {
                    EventQueue.invokeAndWait(new Runnable() {

                        @Override
                        public void run() {

                            if (win.component != null) {
                                win.component.setVisible(true);
                                win.component.requestFocus();
                            }
                        }
                    });
                } catch (InterruptedException ex) {
                    Logger.getLogger(GuiCommand.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(GuiCommand.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case 2:
                try {
                    EventQueue.invokeAndWait(new Runnable() {

                        @Override
                        public void run() {
                            if (win.component != null) {
                                win.component.setVisible(true);
                                win.component.requestFocus();
                            }
                        }
                    });
                } catch (InterruptedException ex) {
                    Logger.getLogger(GuiCommand.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(GuiCommand.class.getName()).log(Level.SEVERE, null, ex);
                }

                context.error(HSPError.UnsupportedOperation, "gsel", "mode==" + mode);
                break;
            default:
                context.error(HSPError.InvalidParameterValue, "gsel", "mode==" + mode);
                return;
        }

    }

    public static void gcopy(final Context context, final int srcId, final int sx, final int sy, final Operand wv,
            final int wvi, final Operand hv, final int hvi) {

        if (srcId < 0 || srcId >= context.windows.size() || context.windows.get(srcId) == null) {
            context.error(HSPError.InvalidParameterValue, "gcopy", "srcId==" + srcId);
        }

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);
        final Bmscr src = (Bmscr) context.windows.get(srcId);

        final int w = toInt(wv, wvi, win.gwidth);
        final int h = toInt(hv, hvi, win.gheight);

        GraphicsRenderer.gcopy(win, win.cx, win.cy, src.backImage, sx, sy, w, h);

        win.redraw(win.cx, win.cy, w, h);

    }

    public static void gzoom(final Context context, final int dw, final int dh, final int srcId, final int sx,
            final int sy, final Operand wv, final int wvi, final Operand hv, final int hvi, final int mode) {

        if (srcId < 0 || srcId >= context.windows.size() || context.windows.get(srcId) == null) {
            context.error(HSPError.InvalidParameterValue, "gzoom", "srcId==" + srcId);
        }

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);
        final Bmscr src = (Bmscr) context.windows.get(srcId);

        int dx = win.cx;
        int dy = win.cy;

        int sw = toInt(wv, wvi, win.gwidth);
        int sh = toInt(hv, hvi, win.gheight);

        final Object oldAntiAliasing = win.backGraphics.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        final Object oldRenering = win.backGraphics.getRenderingHint(RenderingHints.KEY_RENDERING);

        final boolean runningOnMac = System.getProperty("os.name").toLowerCase().startsWith("mac os x");

        if ((dw < 0 || dh < 0 || sw < 0 || sh < 0)) {

            final int[] dxs = {dx, dx + dw, dx + dw, dx};
            final int[] dys = {dy, dy, dy + dh, dy + dh};
            final int[] sxs = {sx, sx + sw, sx + sw, sx};
            final int[] sys = {sy, sy, sy + sh, sy + sh};

            final int oldMode = win.gmode;
            win.gmode = 0;
            try {

                GraphicsRenderer.gsquare(win, dxs, dys, src.backImage, sxs, sys);

            } finally {
                win.gmode = oldMode;
            }

        } else {

            if (mode == 1) {
                win.backGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                win.backGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            }

            win.backGraphics.drawImage(src.backImage, dx, dy, dx + dw, dy + dh, sx, sy, sx + sw, sy + sh, null);

            win.backGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntiAliasing);
            win.backGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, oldRenering);

        }

        win.redraw(win.cx, win.cy, Math.abs(dw), Math.abs(dh));

    }

    public static void gmode(final Context context, final int mode, final int w, final int h, final int alpha) {

        if (mode < 0 || mode >= 8) {
            context.error(HSPError.InvalidParameterValue, "gmode", "mode==" + mode);
            return;
        }

        if (mode == 7) {
            context.error(HSPError.UnsupportedOperation, "gmode", "mode==" + mode);
            return;
        }

        if (w < 0) {
            context.error(HSPError.InvalidParameterValue, "gmode", "w==" + w);
            return;
        }

        if (h < 0) {
            context.error(HSPError.InvalidParameterValue, "gmode", "h==" + h);
            return;
        }

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        win.gmode = mode;
        win.gwidth = w;
        win.gheight = h;
        win.galpha = alpha >= 256 ? 256 : alpha < 0 ? 0 : alpha;
        win.transColor = win.color;

    }

    public static void bmpsave(final Context context, final Operand v, final int vi) {

        context.error(HSPError.UnsupportedOperation, "bmpsave");

    }

    public static void hsvcolor(final Context context, final int h, final int s, final int v) {

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        win.color = Color.getHSBColor(h % 192 / 192.0F, s > 256 ? 1.0F : s / 256.0F, v > 256.0F ? 1.0F : v / 256.0F);

        final Graphics2D g = win.backGraphics;

        g.setColor(win.color);
    }

    public static void getkey(final Context context, final Operand v, final int vi, final int key) {
        /*
         * Logger.getLogger(GuiCommand.class.getName()).log(Level.FINEST,
         * "getKey: Request = {0}, Response = {1}", new Object[] {
         * KeyEvent.getKeyText(key), Boolean.toString(context.keyPressed[key]) }
         * );
         */
        v.assign(vi, Scalar.fromValue(context.keyPressed[key] ? 1 : 0), 0);

    }

    public static void chkbox(final Context context, final String text, final Operand v, final int vi) {

        if (text == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "chkbox", "text");
            return;
        }

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "chkbox", "v");
            return;
        }

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        if (win.contents != null) {

            final Chkbox obj = new Chkbox(text, (Variable) v, vi);

            addObject(context, win, obj, new Dimension(win.owidth, win.oheight));
        }
    }

    public static void listbox(final Context context, final Operand v, final int vi, final int height,
            final String items) {

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "listbox", "v");
            return;
        }

        if (items == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "listbox", "items");
            return;
        }

        if (height < 0) {
            context.error(HSPError.InvalidParameterValue, "combox", "height==" + height);
            return;
        }

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        if (win.contents != null) {

            final Listbox obj = new Listbox((Variable) v, vi, items.split("\\r?\\n"));

            addObject(context, win, obj, new Dimension(win.owidth, win.oheight + height));
        }
    }

    public static void combox(final Context context, final Operand v, final int vi, final int height, final String items) {

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "combox", "v");
            return;
        }

        if (items == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "combox", "items");
            return;
        }

        if (height < 0) {
            context.error(HSPError.InvalidParameterValue, "combox", "height==" + height);
            return;
        }

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        if (win.contents != null) {

            final Combox obj = new Combox((Variable) v, vi, items.split("\\r?\\n"));

            addObject(context, win, obj, new Dimension(win.owidth, win.oheight));

            obj.setMaximumRowCount((win.oheight + height) / obj.getFont().getSize());
        }
    }

    public static void input(final Context context, final Operand v, final int vi, final Operand wv, final int wvi,
            final Operand hv, final int hvi, final int length) {

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "input", "v");
            return;
        }

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        final int w = toInt(wv, wvi, win.owidth);
        final int h = toInt(hv, hvi, win.oheight);

        if (w < 0) {
            context.error(HSPError.InvalidParameterValue, "mesbox", "w==" + w);
            return;
        }
        if (h < 0) {
            context.error(HSPError.InvalidParameterValue, "mesbox", "h==" + h);
            return;
        }

        if (win.contents != null) {

            final Input obj = new Input((Variable) v, vi, length);

            addObject(context, win, obj, new Dimension(w, h));
        }
    }

    public static void mesbox(final Context context, final Operand v, final int vi, final Operand wv, final int wvi,
            final Operand hv, final int hvi, final Operand typev, final int typevi) {

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "mesbox", "v");
            return;
        }

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        final int w = toInt(wv, wvi, win.owidth);
        final int h = toInt(hv, hvi, win.oheight);
        final int type = toInt(typev, typevi, 1);

        if (w < 0) {
            context.error(HSPError.InvalidParameterValue, "mesbox", "w==" + w);
            return;
        }
        if (h < 0) {
            context.error(HSPError.InvalidParameterValue, "mesbox", "h==" + h);
            return;
        }
        if (win.contents != null) {

            final Mesbox obj = new Mesbox((Variable) v, vi, (type & 1) != 0, (type & 4) != 0);

            addObject(context, win, obj, new Dimension(w, h));

        }
    }

    public static void buffer(final Context context, final int id, final Operand wv, final int wvi, final Operand hv,
            final int hvi) {

        if (id < 0) {
            context.error(HSPError.InvalidParameterValue, "buffer", "id==" + id);
            return;
        }

        final int w = toInt(wv, wvi, 640);
        final int h = toInt(hv, hvi, 480);

        if (w < 0) {
            context.error(HSPError.InvalidParameterValue, "screen", "w==" + w);
            return;
        }
        if (h < 0) {
            context.error(HSPError.InvalidParameterValue, "screen", "h==" + h);
            return;
        }

        context.targetWindow = id;

        if (id == 0) {
            final Bmscr win = (Bmscr) context.windows.get(0);

            win.init(Color.white, new Dimension(w, h));
            win.contents.setPreferredSize(new Dimension(w, h));
            win.contents.setSize(new Dimension(w, h));

            if (win.component != null) {
                win.component.setVisible(true);
            }
        } else {
            setScreen(context, id, new Buffer(new Dimension(w, h)).getBmscr());
        }
    }

    public static void screen(final Context context, final int id, final Operand wv, final int wvi, final Operand hv,
            final int hvi, final int mode, final Operand lv, final int lvi, final Operand tv, final int tvi,
            final Operand vwv, final int vwvi, final Operand vhv, final int vhvi) {

        if (id < 0) {
            context.error(HSPError.InvalidParameterValue, "screen", "id==" + id);
            return;
        }

        final int w = toInt(wv, wvi, 640);
        final int h = toInt(hv, hvi, 480);
        final int l = toInt(lv, lvi, Integer.MIN_VALUE);
        final int t = toInt(tv, tvi, Integer.MIN_VALUE);
        final int vw = toInt(vwv, vwvi, w);
        final int vh = toInt(vhv, vhvi, h);

        if (w < 0) {
            context.error(HSPError.InvalidParameterValue, "screen", "w==" + w);
            return;
        }
        if (h < 0) {
            context.error(HSPError.InvalidParameterValue, "screen", "h==" + h);
            return;
        }
        if (vw < 0) {
            context.error(HSPError.InvalidParameterValue, "screen", "vw==" + vw);
            return;
        }

        if (vh < 0) {
            context.error(HSPError.InvalidParameterValue, "screen", "vh==" + vh);
            return;
        }

        context.targetWindow = id;

        if (id == 0) {
            final Bmscr win = (Bmscr) context.windows.get(0);

            win.init(Color.white, new Dimension(w, h));
            win.contents.setPreferredSize(new Dimension(vw, vh));
            win.contents.setSize(new Dimension(vw, vh));

            if (win.component != null) {
                win.component.setVisible(true);
            }
        } else {
            final Screen s = new Screen(new Dimension(w, h), mode, new Point(l, t), new Dimension(vw, vh));
            setScreen(context, id, s.getBmscr());

            if ((mode & 2) == 0) {
                s.setVisible(true);
            }
        }
    }

    public static void bgscr(final Context context, final int id, final Operand wv, final int wvi, final Operand hv,
            final int hvi, final int mode, final Operand lv, final int lvi, final Operand tv, final int tvi,
            final Operand vwv, final int vwvi, final Operand vhv, final int vhvi) {

        if (id < 0) {
            context.error(HSPError.InvalidParameterValue, "bgscr", "id==" + id);
            return;
        }

        final int w = toInt(wv, wvi, 640);
        final int h = toInt(hv, hvi, 480);
        final int l = toInt(lv, lvi, Integer.MIN_VALUE);
        final int t = toInt(tv, tvi, Integer.MIN_VALUE);
        final int vw = toInt(vwv, vwvi, w);
        final int vh = toInt(vhv, vhvi, h);

        if (w < 0) {
            context.error(HSPError.InvalidParameterValue, "bgscr", "w==" + w);
            return;
        }
        if (h < 0) {
            context.error(HSPError.InvalidParameterValue, "bgscr", "h==" + h);
            return;
        }
        if (vw < 0) {
            context.error(HSPError.InvalidParameterValue, "bgscr", "vw==" + vw);
            return;
        }

        if (vh < 0) {
            context.error(HSPError.InvalidParameterValue, "bgscr", "vh==" + vh);
            return;
        }

        context.targetWindow = id;

        if (id == 0) {
            final Bmscr win = (Bmscr) context.windows.get(0);

            win.init(Color.white, new Dimension(w, h));
            win.contents.setPreferredSize(new Dimension(vw, vh));
            win.contents.setSize(new Dimension(vw, vh));

            if (win.component != null) {
                win.component.setVisible(true);
            }
        } else {
            final Bgscr s = new Bgscr(new Dimension(w, h), mode, new Point(l, t), new Dimension(vw, vh));
            setScreen(context, id, s.getBmscr());

            if ((mode & 2) == 0) {
                s.setVisible(true);
            }
        }
    }

    private static void setScreen(final Context context, final int id, final Bmscr screen) {

        while (context.windows.size() <= id) {
            context.windows.add(null);
        }

        // すでにあるスクリーンを破棄しなければならない
        if (context.windows.get(id) != null) {

            final Bmscr existing = (Bmscr) context.windows.get(id);

            if (existing.window != null) {
                // スクリーンはウィンドウ
                existing.window.dispose();
            } else if (existing.component != null) {
                // それ以外のコンポーネント（ありえないが・・・）

                if (existing.component.getParent() != null) {
                    existing.component.getParent().remove(existing.component);
                } else {

                    existing.component.setVisible(false);
                }
            }
        }

        context.windows.set(id, screen);
        context.listener.listen(screen);
    }

    public static void mouse(final Context context, final Operand v, final int vi) {

        context.error(HSPError.UnsupportedOperation, "mouse");

    }

    public static void objsel(final Context context, final int id) {

        final Bmscr win = context.windows.get(context.targetWindow);

        if (id < 0) {
            context.stat.value = 0;
            for (int i = 0; i < win.controls.size(); ++i) {
                if (win.controls.get(i) != null && (win.controls.get(i)).asComponent().hasFocus()) {
                    context.stat.value = i;
                    return;
                }
            }

        } else {
            if (id < win.controls.size() && win.controls.get(id) != null) {
                (win.controls.get(id)).asComponent().requestFocusInWindow();
            }
        }

    }

    public static void groll(final Context context, final int x, final int y) {

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        win.originx = x;
        win.originy = y;

        win.redraw(0, 0, win.backImage.getWidth(), win.backImage.getHeight());

    }

    public static void line(final Context context, final int x2, final int y2, final Operand x1v, final int x1vi,
            final Operand y1v, final int y1vi) {

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        final Graphics2D g = win.backGraphics;

        final int x1 = toInt(x1v, x1vi, win.cx);
        final int y1 = toInt(y1v, y1vi, win.cy);

        // 終点は書いてはダメ

        if (x2 >= 0 && y2 >= 0 && x2 < win.backImage.getWidth() && y2 < win.backImage.getHeight()) {
            final int last = win.backImage.getRGB(x2, y2);
            g.drawLine(x1, y1, x2, y2);
            win.backImage.setRGB(x2, y2, last);
        } else {

            g.drawLine(x1, y1, x2, y2);
        }

        win.cx = x2;
        win.cy = y2;

        final int l = Math.min(x1, x2);
        final int t = Math.min(y1, y2);
        final int r = Math.max(x1, x2);
        final int b = Math.max(y1, y2);

        win.redraw(l, t, r - l, b - t);

    }

    public static void clrobj(final Context context, final int start, final Operand ev, final int evi) {

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);
        int end = Math.min(win.controls.size(), (toInt(ev, evi, -1) & 0xFFFFFF) + 1);

        if (win.contents != null) {
            for (int i = start; i < end; ++i) {
                if (win.controls.get(i) != null) {
                    Component c = (win.controls.get(i)).asComponent();
                    c.setVisible(false);
                    win.contents.remove(c);
                    win.controls.set(i, null);
                }
            }

        }

    }

    public static void boxf(final Context context, final int x1, final int y1, final Operand x2v, final int x2vi,
            final Operand y2v, final int y2vi) {

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        final Graphics2D g = win.backGraphics;

        final int x2 = toInt(x2v, x2vi, win.backImage.getWidth() - 1);
        final int y2 = toInt(y2v, y2vi, win.backImage.getHeight() - 1);

        final int l = Math.min(x1, x2);
        final int t = Math.min(y1, y2);
        final int r = Math.max(x1, x2) + 1;
        final int b = Math.max(y1, y2) + 1;

        g.fillRect(l, t, r - l, b - t);

        win.redraw(l, t, r - l, b - t);
    }

    public static void objprm(final Context context, final int id, final Operand v, final int vi) {

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "objprm", "v");
            return;
        }

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);
        if (win.controls.get(id) != null) {
            win.controls.get(id).setValue(v, vi);
        }

    }

    public static void objmode(final Context context, final int fontMode, final int tabMode) {

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        switch (fontMode) {
            case 0:
                win.objfont = new Font("Monospaced", 0, 16);
                break;
            case 1:
                win.objfont = null;
                break;
            case 2:
                win.objfont = win.font;
                break;
        }

    }

    public static void stick(final Context context, final Operand v, final int vi, final int notrigerMask) {

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "stick", "v");
            return;
        }

        if (context.stickTriggerTime < System.currentTimeMillis() - 500) {
            context.stickTrigger = 0;
        }

        v.assign(vi, Scalar.fromValue((context.stickState & notrigerMask) | (context.stickTrigger & ~notrigerMask)), 0);

        context.stickTrigger = 0;
    }

    public static void grect(final Context context, final int cx, final int cy, final double angle, final Operand wv,
            final int wvi, final Operand hv, final int hvi) {

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        final int sw = toInt(wv, wvi, win.gwidth);
        final int sh = toInt(hv, hvi, win.gheight);
        final int w = sw / 2 - 1;
        final int h = sh / 2 - 1;

        final double si = Math.sin(angle);
        final double co = Math.cos(angle);

        int[] dx = new int[]{(int) (-w * co + h * si) + cx - 1, (int) (w * co + h * si) + cx - 1,
            (int) (w * co - h * si) + cx - 1, (int) (-w * co - h * si) + cx - 1,};
        int[] dy = new int[]{(int) (-w * si - h * co) + cy - 1, (int) (w * si - h * co) + cy - 1,
            (int) (w * si + h * co) + cy - 1, (int) (-w * si + h * co) + cy - 1,};

        GraphicsRenderer.gsquare(win, dx, dy);

        final int l = Math.min(Math.min(Math.min(dx[0], dx[1]), dx[2]), dx[3]);
        final int t = Math.min(Math.min(Math.min(dy[0], dy[1]), dy[2]), dy[3]);
        final int r = Math.max(Math.max(Math.max(dx[0], dx[1]), dx[2]), dx[3]);
        final int b = Math.max(Math.max(Math.max(dy[0], dy[1]), dy[2]), dy[3]);

        win.redraw(l, t, r - l, b - t);
    }

    public static void grotate(final Context context, final int id, final int sx, final int sy, final double angle,
            final Operand dwv, final int dwi, final Operand dhv, final int dhi) {

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        final int dw = toInt(dwv, dwi, win.gwidth);
        final int dh = toInt(dhv, dhi, win.gheight);

        final double w = dw / 2 - 0.5;
        final double h = dh / 2 - 0.5;

        final int cx = win.cx;
        final int cy = win.cy;

        final double si = Math.sin(angle);
        final double co = Math.cos(angle);

        int[] dx = new int[]{(int) Math.round(-w * co + h * si) + cx, (int) Math.round(w * co + h * si) + cx,
            (int) Math.round(w * co - h * si) + cx, (int) Math.round(-w * co - h * si) + cx,};
        int[] dy = new int[]{(int) Math.round(-w * si - h * co) + cy, (int) Math.round(w * si - h * co) + cy,
            (int) Math.round(w * si + h * co) + cy, (int) Math.round(-w * si + h * co) + cy,};
        int[] sxs = new int[]{sx, sx + win.gwidth - 1, sx + win.gwidth - 1, sx};
        int[] sys = new int[]{sy, sy, sy + win.gheight - 1, sy + win.gheight - 1};

        try {
            GraphicsRenderer.gsquare(win, dx, dy, ((Bmscr) context.windows.get(id)).backImage, sxs, sys);
        } catch (ArrayIndexOutOfBoundsException ex) {
            Logger.getLogger(GuiCommand.class.getName()).log(Level.SEVERE, "", ex);
        }

        final int l = Math.min(Math.min(Math.min(dx[0], dx[1]), dx[2]), dx[3]);
        final int t = Math.min(Math.min(Math.min(dy[0], dy[1]), dy[2]), dy[3]);
        final int r = Math.max(Math.max(Math.max(dx[0], dx[1]), dx[2]), dx[3]);
        final int b = Math.max(Math.max(Math.max(dy[0], dy[1]), dy[2]), dy[3]);

        win.redraw(l, t, r - l, b - t);
    }

    public static void gsquare(final Context context, final int id, final Operand dxs, final int dxi,
            final Operand dys, final int dyi, final Operand sxs, final int sxi, final Operand sys, final int syi) {

        if (dxs == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "gsquare", "dxs");
            return;
        }
        if (dys == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "gsquare", "dys");
            return;
        }

        final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

        final int[] dx = new int[]{dxs.toInt(dxi), dxs.toInt(dxi + 1), dxs.toInt(dxi + 2), dxs.toInt(dxi + 3)};

        final int[] dy = new int[]{dys.toInt(dyi), dys.toInt(dyi + 1), dys.toInt(dyi + 2), dys.toInt(dyi + 3)};

        if (id >= 0) {

            if (id >= context.windows.size() || context.windows.get(id) == null) {
                context.error(HSPError.InvalidParameterValue, "gsquare", "id==" + id);
                return;
            }

            if (sxs == null) {
                context.error(HSPError.ParameterCannotBeOmitted, "gsquare", "sxs");
                return;
            }
            if (sys == null) {
                context.error(HSPError.ParameterCannotBeOmitted, "gsquare", "sys");
                return;
            }

            final int[] sx = new int[]{sxs.toInt(sxi), sxs.toInt(sxi + 1), sxs.toInt(sxi + 2), sxs.toInt(sxi + 3)};

            final int[] sy = new int[]{sys.toInt(syi), sys.toInt(syi + 1), sys.toInt(syi + 2), sys.toInt(syi + 3)};

            GraphicsRenderer.gsquare(win, dx, dy, ((Bmscr) context.windows.get(id)).backImage, sx, sy);
        } else {
            GraphicsRenderer.gsquare(win, dx, dy);

        }

        final int l = Math.min(Math.min(Math.min(dx[0], dx[1]), dx[2]), dx[3]);
        final int t = Math.min(Math.min(Math.min(dy[0], dy[1]), dy[2]), dy[3]);
        final int r = Math.max(Math.max(Math.max(dx[0], dx[1]), dx[2]), dx[3]);
        final int b = Math.max(Math.max(Math.max(dy[0], dy[1]), dy[2]), dy[3]);

        win.redraw(l, t, r - l, b - t);
    }
}
