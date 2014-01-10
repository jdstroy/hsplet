/*
 * $Id: EventListener.java,v 1.8 2006/05/20 06:12:06 Yuki Exp $
 */
package hsplet.gui;

import hsplet.Context;
import hsplet.JumpTask;

import java.awt.Component;
import java.awt.IllegalComponentStateException;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

/**
 * マウスやキーボードのイベントを監視して、context に登録するオブジェクト。 <p> Screen や Bgscr を登録する。 </p>
 *
 * @author Yuki
 * @version $Revision: 1.8 $, $Date: 2006/05/20 06:12:06 $
 */
public class EventListener implements MouseListener, MouseMotionListener, KeyListener, FocusListener, Serializable {

    /**
     * このクラスを含むソースファイルのバージョン文字列。
     */
    private static final String fileVersionID = "$Id: EventListener.java,v 1.8 2006/05/20 06:12:06 Yuki Exp $";
    /**
     * 直列化復元時に、データの互換性を確認するためのバージョン番号。
     */
    private static final long serialVersionUID = -7715847659889033067L;
    private final Context context;
    /**
     * Javaキーコード -> Windowsキーコード
     */
    private static final int[] vkJtoW = new int[1024];
    /**
     * Javaキーコード -> 文字コード
     */
    private static final int[] vkJtoC = new int[1024];

    static {

        // 基本は同じ
        for (int i = 0; i < vkJtoW.length; ++i) {
            vkJtoW[i] = i;
        }

        vkJtoW[155] = 45;//ins
        vkJtoW[127] = 46;//del
        vkJtoW[45] = 189;//-
        vkJtoW[514] = 222;//^
        vkJtoW[92] = 220;//\
        vkJtoW[512] = 192;//@
        vkJtoW[91] = 219;//[
        vkJtoW[59] = 187;//;
        vkJtoW[513] = 186;//:
        vkJtoW[93] = 221;//]
        vkJtoW[44] = 188;//,
        vkJtoW[46] = 190;//.
        vkJtoW[47] = 191;///
        vkJtoW[524] = 91;//田
        vkJtoW[525] = 93;//メニュー
        vkJtoW[10] = 13;//エンター

        // 基本は同じ
        for (int i = '0'; i <= '9'; ++i) {
            vkJtoC[i] = i;
        }
        for (int i = 'A'; i <= 'Z'; ++i) {
            vkJtoC[i] = i;
        }

        vkJtoC[45] = '-';//-
        vkJtoC[514] = '^';//^
        vkJtoC[92] = '\\';//\
        vkJtoC[512] = '@';//@
        vkJtoC[91] = '[';//[
        vkJtoC[59] = ';';//;
        vkJtoC[513] = ':';//:
        vkJtoC[93] = ']';//]
        vkJtoC[44] = ',';//,
        vkJtoC[46] = '.';//.
        vkJtoC[47] = '/';///
        vkJtoC[10] = 13;//エンター
    }

    /**
     * オブジェクトを作成する。
     *
     * @param context 実行しているコンテキスト。
     */
    public EventListener(final Context context) {

        this.context = context;
    }

    /**
     * 画面を監視する。
     *
     * @param info 監視される画面。
     */
    public void listen(final Bmscr info) {

        final Component component = info.component;
        if (component != null) {

            component.addKeyListener(this);
            component.addMouseListener(this);
            component.addMouseMotionListener(this);
            component.addFocusListener(this);
        }
    }

    /**
     * オブジェクトを監視する。
     *
     * @param control 監視されるオブジェクト。
     */
    public void listen(final HSPControl control) {

        final Component component = control.asComponent();
        if (component != null) {

            component.addKeyListener(this);
            component.addMouseListener(this);
            component.addMouseMotionListener(this);
        }
    }

    private static int getStickCode(final KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                return 1;
            case KeyEvent.VK_UP:
                return 2;
            case KeyEvent.VK_RIGHT:
                return 4;
            case KeyEvent.VK_DOWN:
                return 8;
            case KeyEvent.VK_SPACE:
                return 16;
            case KeyEvent.VK_ENTER:
                return 32;
            case KeyEvent.VK_CONTROL:
                return 64;
            case KeyEvent.VK_ESCAPE:
                return 128;
            case KeyEvent.VK_TAB:
                return 1024;
            default:
                return -1;
        }
    }

    private static int getStickCode(final MouseEvent e) {

        if (SwingUtilities.isLeftMouseButton(e)) {
            return 256;
        } else if (SwingUtilities.isRightMouseButton(e)) {
            return 512;
        } else {
            return -1;
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {

        final int stickCode = getStickCode(e);

        if (stickCode >= 0) {

            if ((context.stickState & stickCode) == 0) {
                if (context.stickTriggerTime < System.currentTimeMillis() - 500) {
                    context.stickTrigger = 0;
                }

                context.stickTrigger |= stickCode;
                context.stickState |= stickCode;
                context.stickTriggerTime = System.currentTimeMillis();
            }
        }

        if (SwingUtilities.isLeftMouseButton(e)) {
            context.keyPressed[1] = true;
        } else if (SwingUtilities.isRightMouseButton(e)) {
            context.keyPressed[2] = true;
        } else if (SwingUtilities.isMiddleMouseButton(e)) {
            context.keyPressed[4] = true;
        }

        if (context.onclick.enabled && context.onclick.jump != null) {
            context.tasks.add(new JumpTask(context.onclick.jump, context.onclick.label, new Integer(SwingUtilities.isLeftMouseButton(e) ? 0 : SwingUtilities.isRightMouseButton(e) ? 3 : 6), new Integer(((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0 ? 8 : 0)
                    | ((e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0 ? 4 : 0)
                    | (SwingUtilities.isLeftMouseButton(e) ? 1 : 0)
                    | (SwingUtilities.isRightMouseButton(e) ? 2 : 0)
                    | (SwingUtilities.isMiddleMouseButton(e) ? 16 : 0)), new Integer(e.getX() | (e.getY() << 16))));
        }
    }

    public void mouseReleased(MouseEvent e) {

        final int stickCode = getStickCode(e);

        if (stickCode >= 0) {
            context.stickState &= ~stickCode;
        }

        if (SwingUtilities.isLeftMouseButton(e)) {
            context.keyPressed[1] = false;
        } else if (SwingUtilities.isRightMouseButton(e)) {
            context.keyPressed[2] = false;
        } else if (SwingUtilities.isMiddleMouseButton(e)) {
            context.keyPressed[4] = false;
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {

        try {
            context.mouseX = e.getComponent().getLocationOnScreen().x + e.getX();
            context.mouseY = e.getComponent().getLocationOnScreen().y + e.getY();
        } catch (IllegalComponentStateException ex) {
        }

    }

    public void mouseMoved(MouseEvent e) {

        try {
            context.mouseX = e.getComponent().getLocationOnScreen().x + e.getX();
            context.mouseY = e.getComponent().getLocationOnScreen().y + e.getY();
        } catch (IllegalComponentStateException ex) {
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {

        Logger.getLogger(getClass().getName()).log(Level.FINEST, "keyPressed: {0}", KeyEvent.getKeyText(e.getKeyCode()));
        context.keyPressed[vkJtoW[e.getKeyCode()]] = true;

        final int stickCode = getStickCode(e);

        if (stickCode >= 0) {

            if ((context.stickState & stickCode) == 0) {
                if (context.stickTriggerTime < System.currentTimeMillis() - 500) {
                    context.stickTrigger = 0;
                }

                context.stickTrigger |= stickCode;
                context.stickState |= stickCode;
                context.stickTriggerTime = System.currentTimeMillis();
            }
        }

        if (context.onkey.enabled && context.onkey.jump != null) {
            context.tasks.add(new JumpTask(context.onkey.jump, context.onkey.label,
                    new Integer(vkJtoC[e.getKeyCode()]), new Integer(vkJtoW[e.getKeyCode()]), new Integer(0)));
        }

    }

    public void keyReleased(KeyEvent e) {

        Logger.getLogger(getClass().getName()).log(Level.FINEST, "keyReleased: {0}", KeyEvent.getKeyText(e.getKeyCode()));
        context.keyPressed[vkJtoW[e.getKeyCode()]] = false;

        final int stickCode = getStickCode(e);

        if (stickCode >= 0) {
            context.stickState &= ~stickCode;
        }

    }

    public void focusGained(final FocusEvent e) {

        context.activeWindow = -1;

        for (int i = 0; i < context.windows.size(); ++i) {
            final Bmscr info = (Bmscr) context.windows.get(i);
            if (info != null && info.component == e.getComponent()) {
                context.activeWindow = i;
            }
        }

    }

    public void focusLost(final FocusEvent e) {
        // Don't do anything with focusLost - issue #12
        //context.activeWindow = -1;
    }
}
