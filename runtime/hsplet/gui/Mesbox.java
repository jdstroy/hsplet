/*
 * $Id: Mesbox.java,v 1.4 2006/02/27 15:52:05 Yuki Exp $
 */
package hsplet.gui;

import hsplet.variable.*;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Keymap;

/**
 * mesbox コントロール。 <p> 内容が変更されたら変数を更新する。 </p>
 *
 * @author Yuki
 * @version $Revision: 1.4 $, $Date: 2006/02/27 15:52:05 $
 */
public class Mesbox extends JScrollPane implements VolatileValueUpdater, HSPControl, DocumentListener {

    /**
     * このクラスを含むソースファイルのバージョン文字列。
     */
    private static final String fileVersionID = "$Id: Mesbox.java,v 1.4 2006/02/27 15:52:05 Yuki Exp $";
    /**
     * 直列化復元時に、データの互換性を確認するためのバージョン番号。
     */
    private static final long serialVersionUID = 2562206890409286100L;
    /**
     * 変数
     */
    private VolatileValue v;
    /**
     * 変数インデックス
     */
    private int vi;
    private final JTextArea text;
    private static final Action NULL_ACTION = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {
        }
    };

    /**
     * オブジェクトを構築する。
     *
     * @param v 表示する変数。
     * @param vi 表示する変数のインデックス。
     * @param editable 編集可能かどうか。
     * @param hscroll 横スクロール可能かどうか。
     */
    public Mesbox(final Variable v, final int vi, boolean editable, boolean hscroll) {
        super(VERTICAL_SCROLLBAR_ALWAYS, hscroll ? HORIZONTAL_SCROLLBAR_ALWAYS : HORIZONTAL_SCROLLBAR_NEVER);
        text = new JTextArea(v.toString(vi));
        text.setNavigationFilter(new NonNegativeNavigationFilter());

        text.setEditable(editable);

        this.v = v.makeVolatile();
        this.vi = vi;

        text.getDocument().addDocumentListener(this);

        setViewportView(text);
    }

    public void update(final Operand value) {

        value.assign(vi, Scalar.fromValue(text.getText().replace("\n", "\r\n")), 0);
    }

    public Component asComponent() {
        return this;
    }

    public void setValue(Operand v, int vi) {

        final JTextArea component = text;
        final String value = v.toString(vi);
        try {
            EventQueue.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    if (!value.equals(component.getText())) {
                        component.setText(value);
                    }
                }
            });
            this.v.updaters.add(this);
        } catch (InterruptedException ex) {
            Logger.getLogger(Mesbox.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Mesbox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void requestFocus() {
        try {
            EventQueue.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    text.requestFocus();
                }
            });
        } catch (InterruptedException ex) {
            Logger.getLogger(Mesbox.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Mesbox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean requestFocusInWindow() {
        final boolean[] out = new boolean[1];
        try {
            EventQueue.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    out[0] = text.requestFocusInWindow();
                }
            });
        } catch (InterruptedException ex) {
            Logger.getLogger(Mesbox.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Mesbox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return out[0];
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        this.v.updaters.add(this);

    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        this.v.updaters.add(this);

    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        this.v.updaters.add(this);
    }

    @Override
    public synchronized void addKeyListener(KeyListener l) {
        super.addKeyListener(l);
        text.addKeyListener(l);
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        super.addMouseListener(l);
        text.addMouseListener(l);
    }

    @Override
    public synchronized void addMouseMotionListener(MouseMotionListener l) {
        super.addMouseMotionListener(l);
        text.addMouseMotionListener(l);
    }

    public JTextArea getTextArea() {
        return text;
    }
}
