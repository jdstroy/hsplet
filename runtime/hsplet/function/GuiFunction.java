/*
 * $Id: GuiFunction.java,v 1.7 2006/02/11 17:19:34 Yuki Exp $
 */
package hsplet.function;

import hsplet.Context;
import hsplet.HSPError;
import hsplet.gui.Bmscr;
import hsplet.variable.Operand;
import hsplet.variable.Scalar;

import java.awt.IllegalComponentStateException;
import java.awt.Toolkit;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * HSP の拡張GUI関数群。
 * 
 * @author Yuki
 * @version $Revision: 1.7 $, $Date: 2006/02/11 17:19:34 $
 */
public class GuiFunction extends FunctionBase {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: GuiFunction.java,v 1.7 2006/02/11 17:19:34 Yuki Exp $";

	public static int mousex(final Context context) {

		final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

		if (win.contents == null) {
			return context.mouseX;
		} else {
			try {
				return context.mouseX - win.contents.getLocationOnScreen().x;
			} catch (IllegalComponentStateException e) {
				return context.mouseX;
			}
		}

	}

	public static int mousey(final Context context) {

		final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

		if (win.contents == null) {
			return context.mouseY;
		} else {
			try {
				return context.mouseY - win.contents.getLocationOnScreen().y;
			} catch (IllegalComponentStateException e) {
				return context.mouseY;
			}
		}

	}

	public static int mousew(final Context context) {

		context.error(HSPError.UnsupportedOperation, "mousew");
		return 0;
	}

	public static int hwnd(final Context context) {

		return context.targetWindow;

	}

	public static int hinstance(final Context context) {

		context.error(HSPError.UnsupportedOperation, "hinstance");
		return 0;

	}

	public static int hdc(final Context context) {

		return context.targetWindow;

	}

	public static int ginfo(final Context context, final int type) {

		final Bmscr win = (Bmscr) context.windows.get(context.targetWindow);

		switch (type) {
		case 0:
			return context.mouseX;
		case 1:
			return context.mouseY;
		case 2:
                    /*Logger.getLogger(GuiFunction.class.getName()).log(
                            Level.WARNING, 
                            "ginfo(2) = {0}; this is being forced to 0", 
                            context.activeWindow);*/
                    // TODO: Fix me!
			//return context.activeWindow;
                    return 0;
		case 3:
			return context.targetWindow;
		case 4:
			if (win.window != null) {
				return win.window.getX();
			} else {
				return 0;
			}
		case 5:
			if (win.window != null) {
				return win.window.getY();
			} else {
				return 0;
			}
		case 6:
			if (win.window != null) {
				return win.window.getX() + win.window.getWidth();
			} else if (win.contents != null) {
				return win.contents.getWidth();
			} else {
				return win.backImage.getWidth();
			}
		case 7:
			if (win.window != null) {
				return win.window.getY() + win.window.getHeight();
			} else if (win.contents != null) {
				return win.contents.getHeight();
			} else {
				return win.backImage.getHeight();
			}
		case 8:
			return win.originx;
		case 9:
			return win.originy;
		case 10:
			if (win.window != null) {
				return win.window.getWidth();
			} else if (win.contents != null) {
				return win.contents.getWidth();
			} else {
				return win.backImage.getWidth();
			}
		case 11:
			if (win.window != null) {
				return win.window.getHeight();
			} else if (win.contents != null) {
				return win.contents.getHeight();
			} else {
				return win.backImage.getHeight();
			}
		case 12:
			return win.backImage.getWidth();
		case 13:
			return win.backImage.getHeight();
		case 14:
			return win.mesw;
		case 15:
			return win.mesh;
		case 16:
			return win.color.getRed();
		case 17:
			return win.color.getGreen();
		case 18:
			return win.color.getBlue();
		case 19: // 現在のデスクトップカラーモード(色モード)が返されます。 フルカラーモードの場合は0が、パレットモードの場合は1が返されます。
			return 0;
		case 20:
			return Toolkit.getDefaultToolkit().getScreenSize().width;
		case 21:
			return Toolkit.getDefaultToolkit().getScreenSize().height;
		case 22:
			return win.cx;
		case 23:
			return win.cy;
		case 24:
			return context.messageWindow;
		default:
			context.error(HSPError.InvalidParameterValue, "ginfo", "type==" + type);
			return 0;
		}
	}

	public static int objinfo(final Context context, final int type) {

		context.error(HSPError.UnsupportedOperation, "objinfo");
		return 0;
	}

	public static String dirinfo(final Context context, final int type) {

		switch (type) {
		case 0:
			return context.curdir.toString();
		case 1:
			return context.exedir.toString();
		case 4:
			return context.cmdline;
		default:
			context.error(HSPError.InvalidParameterValue, "dirinfo", "type==" + type);
			return "";
		}

	}

	public static Operand sysinfo(final Context context, final int type) {

		switch (type) {
		case 0:
			return Scalar.fromValue(System.getProperty("os.name"));
		case 1:
			return Scalar.fromValue(System.getProperty("user.name"));
		case 2:
			try {
				return Scalar.fromValue(new InetSocketAddress(InetAddress.getLocalHost(), 80).getHostName());
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return Scalar.fromValue("localhost");
			}
		case 16:
			return Scalar.fromValue(System.getProperty("os.arch"));
		case 33:
			return Scalar
					.fromValue((int) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) * 100 / Runtime
							.getRuntime().totalMemory()));
		case 34:
			return Scalar.fromValue((int) Runtime.getRuntime().totalMemory());
		case 35:
			return Scalar.fromValue((int) Runtime.getRuntime().freeMemory());
		case 36:
			return Scalar.fromValue(0);
		case 37:
			return Scalar.fromValue(0);
		case 38:
			return Scalar.fromValue((int) Runtime.getRuntime().totalMemory());
		case 39:
			return Scalar.fromValue((int) Runtime.getRuntime().freeMemory());
		default:
			context.error(HSPError.InvalidParameterValue, "sysinfo", "type==" + type);
			return Scalar.fromValue(0);
		}
	}
}
