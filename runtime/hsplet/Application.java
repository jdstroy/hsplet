/*
 * $Id: Application.java,v 1.7 2006/02/11 18:00:38 Yuki Exp $
 */
package hsplet;

import hsplet.gui.Browser;
import hsplet.gui.DebugWindow;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * HSPLet �̃��C���A�v���P�[�V�����N���X�B
 *
 * @author Yuki
 * @version $Revision: 1.7 $, $Date: 2006/02/11 18:00:38 $
 */
public final class Application extends JFrame implements AppletStub,
        AppletContext {

    /**
     * ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B
     */
    private static final String fileVersionID = "$Id: Application.java,v 1.7 2006/02/11 18:00:38 Yuki Exp $";
    /**
     * ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B
     */
    private static final long serialVersionUID = 3051070041292384848L;
    /**
     * �A�v���b�g�ɗ^���閼�O�B
     */
    public static final String APPLET_NAME = "HSPLet";

    /**
     * �A�v���P�[�V�����̃G���g���[�|�C���g�B
     *
     * @param args ���s���̈����B <ol> <li>--width=����</li> <li>--height=����</li>
     * <li>--debug=�f�o�b�O�E�B���h�E��\�����邩�ǂ���</li> <li>--startClass=�J�n�N���X��</li>
     * <li>--cmdline=�R�}���h���C������</li> </ol>
     */
    //@SuppressWarnings("unchecked")
    public static void main(final String[] args) {

        int width = 640;
        int height = 480;
        boolean debug = false;
        String startClass = hsplet.Applet.DEFAULT_START_CLASSNAME;
        String cmdline = "";

        for (int i = 0; i < args.length; ++i) {

            final String arg = args[i];

            if (arg.startsWith("--width=")) {
                width = Integer.parseInt(arg.substring("--width=".length()));

            } else if (arg.startsWith("--height=")) {
                height = Integer.parseInt(arg.substring("--height=".length()));

            } else if (arg.startsWith("--debug=")) {
                debug = Boolean.valueOf(arg.substring("--debug=".length())).booleanValue();

            } else if (arg.startsWith("--startClass=")) {
                startClass = arg.substring("--startClass=".length());

            } else if (arg.startsWith("--cmdline=")) {
                cmdline = arg.substring("--cmdline=".length());

            }
        }

        try {
            run(Class.forName(startClass), width, height, debug, cmdline);
        } catch (Throwable e) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * �A�v���P�[�V�������J�n����B
     *
     * @param startClass ���s�����N���X�B
     * @param width ��ʂ̉����B
     * @param height ��ʂ̍����B
     * @param debug �f�o�b�O�E�B���h�E��\�����邩�ǂ����B
     * @param cmdline �R�}���h���C�������B
     * @return �A�v���P�[�V�����C���X�^���X�B
     */
    public static Application run(final Class startClass, final int width,
            final int height, final boolean debug, final String cmdline) {

        return new Application(startClass, width, height, debug, cmdline);
    }
    private final Map<Object, Object> parameters = new HashMap();
    private final Map<String, InputStream> streams = Collections.synchronizedMap(new HashMap<String, InputStream>());
    private final hsplet.Applet applet;

    //@SuppressWarnings("unchecked")
    private Application(final Class startClass, final int width,
            final int height, final boolean debug, final String cmdline) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // �f�o�b�O�E�B���h�E��\���B
        if (debug) {
            final DebugWindow debugWindow = new DebugWindow();
            addWindowListener(new WindowAdapter() {
                //@Override

                public void windowClosed(WindowEvent e) {
                    debugWindow.dispose();
                }
            });
        }

        parameters.put(hsplet.Applet.START_CLASSNAME_PARAM, startClass.getName());
        parameters.put("cmdline", cmdline);
        //parameters.putAll(System.getenv());
        parameters.putAll(System.getProperties());

        this.applet = new hsplet.Applet();

        applet.setStub(this);

        setContentPane(applet);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        applet.getBmscr().contents.setPreferredSize(new Dimension(width, height));
        applet.getBmscr().contents.setSize(new Dimension(width, height));

        pack();

        //setLocationByPlatform(true);
        setLocation(
                (Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2);

        getContentPane().addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                pack();

            }
        });

        applet.init(startClass);
        applet.start();

        setVisible(true);

    }

    //@Override
    public void dispose() {

        applet.stop();
        applet.destroy();

        super.dispose();

        System.exit(0);
    }

    public Applet getApplet(final String name) {

        return name != null && name.equals(APPLET_NAME) ? applet : null;
    }

    public void showDocument(final URL url) {

        showDocument(url, "_self");

    }

    public void showDocument(final URL url, final String target) {

        Browser.showPage(url, target);
    }

    public AudioClip getAudioClip(final URL url) {

        return Applet.newAudioClip(url);
    }

    public Enumeration<Applet> getApplets() {

        final Vector<Applet> v = new Vector<Applet>();
        v.add(applet);

        return v.elements();
    }

    public void showStatus(final String status) {

        setTitle(status);

    }

    public Image getImage(final URL url) {

        return Toolkit.getDefaultToolkit().createImage(url);
    }

    public AppletContext getAppletContext() {

        return this;
    }

    public void appletResize(int width, int height) {

        final Dimension prevSize = applet.getPreferredSize();
        if (prevSize.width != width || prevSize.height != height) {
            applet.setSize(new Dimension(width, height));
        }
    }

    public URL getCodeBase() {

        return getDocumentBase();
    }

    public URL getDocumentBase() {

        final String classPath = System.getProperty("user.dir");

        try {
            return new File(classPath + "/").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getParameter(final String name) {

        return (String) parameters.get(name);
    }

    public void setStream(final String key, final InputStream stream) {

        streams.put(key, stream);

    }

    public InputStream getStream(final String key) {

        return streams.get(key);
    }

    public Iterator<String> getStreamKeys() {

        return streams.keySet().iterator();
    }
}
