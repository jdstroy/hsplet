import hsplet.Context;
import hsplet.function.FunctionBase;
import hsplet.util.Conversion;
import hsplet.variable.Operand;
import hsplet.variable.Scalar;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import netscape.javascript.JSObject;

/**
 * hspini.dll を実装するクラス.
 * <p>{@link netscape.javascript.JSObject} を使用して JavaScript 経由でクッキーを操作する. 
 * </p>
 * @author Yuki
 * @version $Revision: 1.4 $, $Date: 2006/05/20 06:12:07 $
 */
public class hspini extends FunctionBase implements Serializable {

	private Context context;

	private final SimpleDateFormat format = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss 'GMT'", new Locale("en"));

	private static final String ENCODING = "MS932";

	private static String namePrefix = "";

	public hspini(final Context context) {
		this.context = context;
	}

	public int iniset(final String file, final String section) {

		namePrefix = file + "-[" + section + "]-";

		try {
			JSObject.getWindow(context.applet);
		} catch (RuntimeException e) {
			return 1;
		}

		return 0;
	}

	public int iniget(final Operand v, final int vi, final String name, final int defaultValue) {

		final String value = getCookie(name);

		if (value == null || value.length() == 0) {

			v.assignRaw(vi, Scalar.fromValue(defaultValue), 0);
			return 0;

		} else {

			v.assignRaw(vi, Scalar.fromValue(Conversion.strtoi(value)), 0);
			return 1;
		}

	}

	public int inigets(final Operand v, final int vi, final String name) {

		final String value = getCookie(name);

		if (value == null || value.length() == 0) {
			return 0;
		} else {

			v.assignRaw(vi, Scalar.fromValue(value), 0);
			return 1;
		}
	}

	public int iniput(final String value, final String name) {

		Calendar expires = Calendar.getInstance();
		expires.add(Calendar.YEAR, 1);

		try {
			JSObject.getWindow(context.applet).eval(
					"document.cookie=\"" + URLEncoder.encode(namePrefix + name, ENCODING) + "="
							+ URLEncoder.encode(value, ENCODING) + "; expires=" + format.format(expires.getTime())
							+ "\"");

		} catch (UnsupportedEncodingException e) {
			return 0;
		}
		return 1;
	}

	public int inidel(final String name) {

		return iniput(name, "");
	}

	private String getCookie(final String name) {

		try {

			final String starts = URLEncoder.encode(namePrefix + name, ENCODING) + "=";

			final String rawCookie = JSObject.getWindow(context.applet).eval("document.cookie").toString();

			final String[] cookies = rawCookie.split(";");
			for (int i = 0; i < cookies.length; ++i) {

				final String cookie = cookies[i].trim();
				if (cookie.startsWith(starts)) {
					return URLDecoder.decode(cookie.substring(starts.length()).trim(), ENCODING);
				}
			}

		} catch (UnsupportedEncodingException e) {
		} catch (RuntimeException e) {
		}
		return null;
	}

}
