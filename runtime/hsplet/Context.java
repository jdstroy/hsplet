/*
 * $Id: Context.java,v 1.14 2006/05/20 06:12:07 Yuki Exp $
 */
package hsplet;

import hsplet.function.EndException;
import hsplet.function.JumpStatement;
import hsplet.function.ProgramCommand;
import hsplet.function.RunException;
import hsplet.gui.Bmscr;
import hsplet.gui.EventListener;
import hsplet.media.HSPMedia;
import hsplet.media.Mci;
import hsplet.util.VCRandom;
import hsplet.variable.DoubleScalar;
import hsplet.variable.IntScalar;
import hsplet.variable.Operand;
import hsplet.variable.OperandInputStream;
import hsplet.variable.StringArray;
import hsplet.variable.StringScalar;
import java.awt.im.InputContext;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.ArrayDeque;

import javax.swing.JOptionPane;

/**
 * HSPLet の実行されているコンテキストを表すクラス。 <p> システム変数などを含む。 </p>
 *
 * @author Yuki
 * @version $Revision: 1.14 $, $Date: 2006/05/20 06:12:07 $
 */
public class Context implements Serializable {

    /**
     * このクラスを含むソースファイルのバージョン文字列。
     */
    private static final String fileVersionID = "$Id: Context.java,v 1.14 2006/05/20 06:12:07 Yuki Exp $";
    /**
     * 直列化復元時に、データの互換性を確認するためのバージョン番号。
     */
    private static final long serialVersionUID = 4644182106732036912L;
    public Applet applet;
    private Class runClass;
    public final EventListener listener = new EventListener(this);
    public final boolean[] keyPressed = new boolean[1024];
    public int stickState = 0;
    public int stickTrigger = 0;
    public long stickTriggerTime = System.currentTimeMillis();
    public int mouseX = 0;
    public int mouseY = 0;
    private final List<InputContext> inputContexts = new ArrayList<InputContext>();

    public List<InputContext> getInputContexts() {
        return inputContexts;
    }

    public void init(final Applet applet, final Class startClass) {

        this.applet = applet;
        this.runClass = startClass;
        windows.add(applet.getBmscr());
        listener.listen(applet.getBmscr());

        cmdline = applet.getParameter("cmdline");
        if (cmdline == null) {
            cmdline = "";
        }

        try {
            exedir = new URL(applet.getCodeBase(), "./");
            curdir = new URL(applet.getCodeBase(), "./");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public void start() {

        final Thread thread = new Thread() {

            public void run() {

                try {

                    boolean runmore;
                    do {
                        try {

                            runmore = false;

                            try {
                                runnableCode = (RunnableCode) runClass.getConstructor(new Class[]{Context.class}).newInstance(new Object[]{Context.this});
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }

                            runnableCode.run();

                        } catch (RunException e) {

                            runClass = e.getRunClass();
                            cmdline = e.getCmdline();
                            if (cmdline == null) {
                                cmdline = "";
                            }

                            runmore = true;

                        }

                    } while (runmore);

                } catch (RuntimeException e) {
                    e.printStackTrace();

                    java.lang.StackTraceElement[] s = (java.lang.StackTraceElement[]) e.getStackTrace();
                    java.lang.StringBuilder str = new java.lang.StringBuilder();
                    for (int i = 0; i < s.length; i++) {
                        str.append("\n   ").append(s[i].getClassName()).append(": ").append(s[i].getMethodName()).append("(").append(s[i].getFileName()).append(": ").append(s[i].getLineNumber()).append(")");
                    }
                    JOptionPane.showMessageDialog(applet, "Error:" + e + "\r\n" + str.toString(), "HSPLet", JOptionPane.ERROR_MESSAGE);

                    try {
                        System.exit(1);
                    } catch (Exception e2) {
                    }
                }

            }
        };

        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    public void stop() {
        tasks.add(new Task() {

            @Override
            public void run(Context context) {
                throw new EndException();
            }
        });

        for (int i = 1; i < windows.size(); ++i) {
            try {
                if (windows.get(i) != null && ((Bmscr) windows.get(i)).window != null) {
                    ((Bmscr) windows.get(i)).window.dispose();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < medias.size(); ++i) {
            try {
                if (medias.get(i) != null) {
                    ((HSPMedia) medias.get(i)).dispose();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mci.dispose();

    }
    private RunnableCode runnableCode;
    public final Mci mci = new Mci(this);
    public final List<Task> tasks = Collections.synchronizedList(new LinkedList<Task>());
    public final List<Bmscr> windows = new ArrayList<Bmscr>();
    public final List<HSPMedia> medias = new ArrayList<HSPMedia>();
    public int targetWindow = 0;
    public int activeWindow = -1;
    public int messageWindow = -1;
    public int lastWaitDuration = 5;
    public long lastAwaitTime = System.currentTimeMillis();
    public VCRandom random = new VCRandom();
    //public Random random = new Random(0);
    public URL exedir;
    public URL curdir;
    public String cmdline;
    public final Stack<Operand> oldNotes = new Stack<Operand>();
    public Operand note = new StringArray();
    public final OnEvent onclick = new OnEvent();
    public final OnEvent onkey = new OnEvent();
    public final OnEvent onerror = new OnEvent();
    public final OnEvent onexit = new OnEvent();
    private final List<Map<Integer, OnEvent>> winoncmds = new ArrayList<Map<Integer, OnEvent>>();

    public OnEvent oncmd(int window, int message) {

        while (window >= winoncmds.size()) {
            winoncmds.add(new HashMap<Integer, OnEvent>());
        }

        final Map<Integer, OnEvent> oncmds = (HashMap<Integer, OnEvent>) winoncmds.get(window);

        final OnEvent event = (OnEvent) oncmds.get(new Integer(message));
        if (event != null) {
            return event;
        }

        final OnEvent newEvent = new OnEvent();
        oncmds.put(new Integer(message), newEvent);
        return newEvent;
    }
    private final IntScalar[] cnts = new IntScalar[64];
    private final int[] lastCnts = new int[64];
    private int lastCnt;

    public boolean startLoop(final int count, final int initial) {

        if (count != 0) {

            int index = looplev.value;

            ++looplev.value;

            cnt = cnts[index] = new IntScalar(initial);
            lastCnt = lastCnts[index] = count < 0 ? -1 : count + initial;

            return true;
        } else {
            return false;
        }
    }

    public boolean nextLoop(final int next) {

        cnt.value = next;

        if (lastCnt < 0 || cnt.value < lastCnt) {
            return true;
        } else {
            endLoop();
            return false;
        }
    }

    public boolean nextLoop() {

        cnt.inc(0);

        if (lastCnt < 0 || cnt.value < lastCnt) {
            return true;
        } else {
            endLoop();
            return false;
        }
    }

    public boolean checkForeach(final Operand array) {

        if (cnt.value < array.l0()) {
            return true;
        } else {
            endLoop();
            return false;
        }
    }

    public void endLoop() {

        --looplev.value;

        int index = looplev.value - 1;
        if (index >= 0) {
            cnt = cnts[index];
            lastCnt = lastCnts[index];
        }
    }
    public final IntScalar system = new IntScalar(0);
    public final IntScalar hspstat = new IntScalar(0x80000000);
    public final IntScalar hspver = new IntScalar(0x3000);
    public final IntScalar stat = new IntScalar(0);
    public IntScalar cnt;
    public final IntScalar err = new IntScalar(0);
    public final IntScalar strsize = new IntScalar(0);
    public final IntScalar looplev = new IntScalar(0);
    public final IntScalar sublev = new IntScalar(0);
    public final IntScalar iparam = new IntScalar(0);
    public final IntScalar wparam = new IntScalar(0);
    public final IntScalar lparam = new IntScalar(0);
    public final StringScalar refstr = new StringScalar("");
    public final DoubleScalar refdval = new DoubleScalar(0.0);
    public OperandInputStream memfile = null;
    public final ArrayDeque<Operand[]> argumentStack = new ArrayDeque<Operand[]>();
    public final ArrayDeque<Integer> argIndexStack = new ArrayDeque<Integer>();

    public Operand getArgument(int index) {
        //For now let's throw an exception normally if something wants an illegal index.
        return argumentStack.peekLast()[index-argIndexStack.peekLast().intValue()];
    }
    public void addArguments(Operand[] args, int paramStart) {
        argumentStack.add(args);
        argIndexStack.add(paramStart);
    }
    public void popArguments() {
        argumentStack.removeLast();
        argIndexStack.removeLast();
    }
    public static final class OnEvent {

        public boolean enabled;
        public int label;
        public JumpStatement jump;
    }

    public RunnableCode getRunnableCode() {

        return runnableCode;
    }

    public InputStream getResource(final String fileName) {
        return getResource(curdir, fileName);
    }

    public InputStream getBufferedResource(final String fileName) {
        return getBufferedResource(curdir, fileName);
    }

    public InputStream getBufferedResource(final URL dir, final String fileName) {

        final InputStream raw = getResource(dir, fileName);

        if (raw == null) {
            return raw;
        }
        return new BufferedInputStream(raw);
    }

    public InputStream getResource(final URL dir, final String fileName) {

        if (fileName.startsWith("MEM:")) {

            return (InputStream) memfile.clone();
        }

        try {
            return getResourceURL(dir, fileName).openStream();
        } catch (IOException e) {
            return null;
        }
    }

    public URL getResourceURL(final String fileName) {
        return getResourceURL(curdir, fileName);
    }

    public URL getResourceURL(final URL dir, final String fileName) {

        final URL url;
        try {
            url = new URL(dir, fileName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        if (url.toString().startsWith(exedir.toString())) {

            String relativeName = url.toString().substring(exedir.toString().length()).replaceAll("\\\\", "/");

            if (relativeName.startsWith("/")) {
                relativeName = relativeName.substring(1);
            }

            {
                final URL result = getClass().getClassLoader().getResource(relativeName);

                if (result != null) {
                    return result;
                }
            }

            {
                // パックファイルはディレクトリも無視する

                final URL result = getClass().getClassLoader().getResource(fileName);

                if (result != null) {
                    return result;
                }
            }

            {
                // パックファイルはすべてのディレクトリも無視する

                final URL result = getClass().getClassLoader().getResource(new File(relativeName).getName());

                if (result != null) {
                    return result;
                }
            }

        }

        return url;
    }

    public void showPage(final URL url, final String target) {
        applet.getAppletContext().showDocument(url, target);
    }

    public void error(final int error, final String command) {
        error(error, command, null);
    }

    public void error(final int error, final String command, final String message) {

        try {

            throw new Exception("HSP error " + error + " on " + command + " "
                    + (message != null && message.length() != 0 ? "\r\n" + message : ""));

        } catch (Exception e) {
            e.printStackTrace();
        }

        err.value = error;

        if (onerror.enabled && onerror.jump != null) {

            wparam.value = error;

            if (onerror.jump == JumpStatement.Goto) {
                ProgramCommand.goto_(this, onerror.label, true);
            } else {
                ProgramCommand.gosub(this, onerror.label);
            }
        } else {
            JOptionPane.showMessageDialog(applet, "HSP error " + error + " on " + command + " "
                    + (message != null && message.length() != 0 ? "\r\n" + message : ""), "HSPLet",
                    JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException();
            //ProgramCommand.end(this, 1);
        }

    }

    public void postMessage(final int window, final int message, final int wparam, final int lparam) {

        if (window < 0 || window >= windows.size()) {
            return;
        }

        final OnEvent event = oncmd(window, message);

        if (event.enabled && event.jump != null) {

            tasks.add(new JumpTask(event.jump, event.label, new Integer(message), new Integer(wparam), new Integer(
                    lparam)) {

                public void run(Context context) {
                    context.messageWindow = window;
                    super.run(context);
                }
            });
        }
    }

    /**
     * Converts Win32 filename requests into a URI, relative to the cwd as
     * appropriate.
     *
     * @param fileName Input file name
     * @return
     */
    public URI resolve(String fileName) throws URISyntaxException {
        return curdir.toURI().resolve(((fileName.startsWith("\\")) ? fileName.substring(1) : fileName).replace('\\', '/'));
    }

    public URI resolveSafe(String fileName) throws URISyntaxException {
        return curdir.toURI().
                resolve(
                fileName.contains("/") ? fileName : ((fileName.startsWith("\\"))
                ? fileName.substring(1) : fileName).replace('\\', '/'));
    }
}
