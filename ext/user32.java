
import hsplet.Context;
import hsplet.function.FunctionBase;
import hsplet.gui.Bmscr;
import hsplet.gui.HSPScreen;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;

/**
 * user32.dll を実装するクラス.
 * 
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/05/20 06:12:07 $
 */
public class user32 extends FunctionBase implements Serializable {

    private static final int MF_STRING = 0x00000000;
    private static final int MF_DISABLED = 0x00000001;
    private static final int MF_GLAYED = 0x00000002;
    private static final int MF_BITMAP = 0x00000004;
    private static final int MF_CHECKED = 0x00000008;
    private static final int MF_POPUP = 0x00000010;
    private static final int MF_MENUBARBREAK = 0x00000020;
    private static final int MF_OWNERDRAW = 0x00000100;
    private static final int MF_SEPARATOR = 0x00000800;
    public static final int WM_COMMAND = 0x0111;
    private static final int MF_BYCOMMAND = 0;
    private static final int MF_BYPOSITION = 0x400;
    private Context context;
    private List<Object> objects = new ArrayList<Object>();

    public user32(final Context context) {
        this.context = context;

        // ID = 0 は使用しない
        objects.add(new Object());
    }

    public int keybd_event(int a, int b, int c) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int GetKeyboardState(hsplet.variable.ByteString str) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private int addObject(final Object o) {

        final int emptyIndex = objects.indexOf(null);
        if (emptyIndex >= 0) {
            objects.set(emptyIndex, o);
            return emptyIndex;
        }

        objects.add(o);
        return objects.size() - 1;
    }

    public int CreateMenu() {

        return addObject(new JHierarchicalMenuBar());
    }

    public int CreatePopupMenu() {

        return addObject(new JHierarchicalMenu(""));
    }

    public int AppendMenuA(final int menuId, final int flags, final int itemId, final String text) {

        final JComponent menu = (JComponent) objects.get(menuId);
        final JComponent item;

        final boolean enabled = (flags & (MF_GLAYED | MF_DISABLED)) == 0;

        final String label = text.replaceAll("\\&(.)", "$1");
        final int mnemonicIndex = text.indexOf('&') + 1;

        char mnemonic = 0;
        if (mnemonicIndex >= 1) {
            mnemonic = text.charAt(mnemonicIndex);
        }

        if ((flags & MF_POPUP) != 0) {

            final JHierarchicalMenu submenu = (JHierarchicalMenu) objects.get(itemId);

            submenu.setText(label);

            item = submenu;

        } else if ((flags & MF_SEPARATOR) != 0) {

            item = new JSeparator();

        } else if (menu instanceof JHierarchicalMenuBar) {
            JHierarchicalMenu subitem = new JHierarchicalMenu(label);
            subitem.addActionListener(new MenuAction(itemId));
            if (mnemonic != 0) {
                subitem.setMnemonic(mnemonic);
            }
            item = subitem;

        } else if ((flags & MF_CHECKED) != 0) {
            JHierarchicalCheckBoxMenuItem subitem = new JHierarchicalCheckBoxMenuItem(label);
            subitem.addActionListener(new MenuAction(itemId));
            if (mnemonic != 0) {
                subitem.setMnemonic(mnemonic);
            }
            item = subitem;

        } else {
            JHierarchicalMenuItem subitem = new JHierarchicalMenuItem(label);
            subitem.addActionListener(new MenuAction(itemId));
            if (mnemonic != 0) {
                subitem.setMnemonic(mnemonic);
            }
            item = subitem;
        }

        if (item instanceof HierarchicalMenu) {
            ((HierarchicalMenu) item).setParentMenu((HierarchicalMenu) menu);
        }

        item.setEnabled(enabled);
        menu.add(item);

        return 1;
    }

    public int SetMenu(final int windowId, final int menuId) {

        final JMenuBar menu = (JMenuBar) objects.get(menuId);

        final Bmscr win = context.windows.get(windowId);

        if (win.component instanceof JFrame) {
            ((JFrame) win.component).setJMenuBar(menu);
            ((JFrame) win.component).pack();
        } else if (win.component instanceof JApplet) {
            ((JApplet) win.component).setJMenuBar(menu);
            win.contents.setSize(win.contents.getWidth(), win.contents.getHeight() - 1);
            win.contents.setSize(win.contents.getWidth(), win.contents.getHeight() + 1);
        }

        return 1;
    }

    public int DrawMenuBar(final int windowId) {

        return 1;
    }

    public int DestroyMenu(final int menuId) {

        objects.set(menuId, null);

        return 1;
    }

    public int CheckMenuRadioItem(int hmenu,
            int idFirst,
            int idLast,
            int idCheck,
            int uFlags) {
        Object item = objects.get(hmenu);

        if (item instanceof JHierarchicalMenu) {
            JHierarchicalMenu menu = (JHierarchicalMenu) item;
            List<JHierarchicalCheckBoxMenuItem> itemsToDeselect = new ArrayList<JHierarchicalCheckBoxMenuItem>(idLast - idFirst);

            if ((uFlags & user32.MF_BYPOSITION) != 0) {
                item = menu.getMenuComponent(idFirst + idCheck);

                for (int i = idFirst; i < idLast; i++) {
                    Component com = menu.getMenuComponent(i);
                    if (com instanceof JHierarchicalCheckBoxMenuItem) {
                        itemsToDeselect.add((JHierarchicalCheckBoxMenuItem) com);
                    }
                }

            } else {
                item = objects.get(idCheck);
                for (int i = idFirst; i < idLast; i++) {
                    Object com = objects.get(i);
                    if (com instanceof JHierarchicalCheckBoxMenuItem) {
                        itemsToDeselect.add((JHierarchicalCheckBoxMenuItem) com);
                    }
                }
            }

            if (item instanceof JHierarchicalCheckBoxMenuItem) {
                JHierarchicalCheckBoxMenuItem menuItem = (JHierarchicalCheckBoxMenuItem) item;

                for (JHierarchicalCheckBoxMenuItem i : itemsToDeselect) {
                    if (i.getState() && i != menuItem) {
                        i.setState(false);
                    }
                }

                menuItem.setState(true);
                return 1;
            }
        }
        return 0;
    }

    private static interface HierarchicalMenu {

        void setParentMenu(HierarchicalMenu value);

        HierarchicalMenu getParentMenu();
    }

    private final class JHierarchicalMenuBar extends JMenuBar implements HierarchicalMenu {

        private HierarchicalMenu parentMenu;

        public HierarchicalMenu getParentMenu() {
            return parentMenu;
        }

        public void setParentMenu(HierarchicalMenu parentMenu) {
            this.parentMenu = parentMenu;
        }
    }

    private final class JHierarchicalMenu extends JMenu implements HierarchicalMenu {

        private HierarchicalMenu parentMenu;

        public JHierarchicalMenu(String label) {
            super(label);
        }

        public HierarchicalMenu getParentMenu() {
            return parentMenu;
        }

        public void setParentMenu(HierarchicalMenu parentMenu) {
            this.parentMenu = parentMenu;
        }
    }

    private final class JHierarchicalMenuItem extends JMenuItem implements HierarchicalMenu {

        private HierarchicalMenu parentMenu;

        public JHierarchicalMenuItem(String label) {
            super(label);
        }

        public HierarchicalMenu getParentMenu() {
            return parentMenu;
        }

        public void setParentMenu(HierarchicalMenu parentMenu) {
            this.parentMenu = parentMenu;
        }
    }

    private final class JHierarchicalRadioButtonMenuItem extends JRadioButton implements HierarchicalMenu {

        private HierarchicalMenu parentMenu;

        public JHierarchicalRadioButtonMenuItem(String label) {
            super(label);
        }

        public HierarchicalMenu getParentMenu() {
            return parentMenu;
        }

        public void setParentMenu(HierarchicalMenu parentMenu) {
            this.parentMenu = parentMenu;
        }
    }

    private final class JHierarchicalCheckBoxMenuItem extends JCheckBoxMenuItem implements HierarchicalMenu {

        private HierarchicalMenu parentMenu;

        public JHierarchicalCheckBoxMenuItem(String label) {
            super(label);
        }

        public HierarchicalMenu getParentMenu() {
            return parentMenu;
        }

        public void setParentMenu(HierarchicalMenu parentMenu) {
            this.parentMenu = parentMenu;
        }
    }

    private final class MenuAction implements ActionListener {

        private int id;

        public MenuAction(final int id) {
            this.id = id;
        }

        public void actionPerformed(ActionEvent e) {

            HierarchicalMenu m = (HierarchicalMenu) e.getSource();
            while (m != null) {
                if (m.getParentMenu() == null) {

                    Component c = (Component) m;

                    while (c != null) {

                        if (c instanceof HSPScreen) {

                            final int windowId = context.windows.indexOf(((HSPScreen) c).getBmscr());

                            context.postMessage(windowId, WM_COMMAND, id, 0);
                        }

                        c = c.getParent();
                    }

                }
                m = m.getParentMenu();
            }

        }
    }
}
