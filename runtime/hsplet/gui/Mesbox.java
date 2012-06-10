/*
 * $Id: Mesbox.java,v 1.4 2006/02/27 15:52:05 Yuki Exp $
 */
package hsplet.gui;

import hsplet.variable.*;
import java.awt.Component;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * mesbox �R���g���[���B <p> ���e���ύX���ꂽ��ϐ����X�V����B </p>
 *
 * @author Yuki
 * @version $Revision: 1.4 $, $Date: 2006/02/27 15:52:05 $
 */
public class Mesbox extends JScrollPane implements VolatileValueUpdater, HSPControl, DocumentListener {

    /**
     * ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B
     */
    private static final String fileVersionID = "$Id: Mesbox.java,v 1.4 2006/02/27 15:52:05 Yuki Exp $";
    /**
     * ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B
     */
    private static final long serialVersionUID = 2562206890409286100L;
    /**
     * �ϐ�
     */
    private VolatileValue v;
    /**
     * �ϐ��C���f�b�N�X
     */
    private int vi;
    private final JTextArea text;

    /**
     * �I�u�W�F�N�g���\�z����B
     *
     * @param v �\������ϐ��B
     * @param vi �\������ϐ��̃C���f�b�N�X�B
     * @param editable �ҏW�\���ǂ����B
     * @param hscroll ���X�N���[���\���ǂ����B
     */
    public Mesbox(final Variable v, final int vi, boolean editable, boolean hscroll) {

        super(VERTICAL_SCROLLBAR_ALWAYS, hscroll ? HORIZONTAL_SCROLLBAR_ALWAYS : HORIZONTAL_SCROLLBAR_NEVER);

        text = new JTextArea(v.toString(vi));

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

        text.setText(v.toString(vi));

        this.v.updaters.add(this);
    }

    @Override
    public void requestFocus() {
        text.requestFocus();
    }

    @Override
    public boolean requestFocusInWindow() {
        return text.requestFocusInWindow();
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
}
