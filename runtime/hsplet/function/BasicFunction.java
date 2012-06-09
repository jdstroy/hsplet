/*
 * $Id: BasicFunction.java,v 1.6.4.1 2006/08/02 12:13:07 Yuki Exp $
 */
package hsplet.function;

import hsplet.Context;
import hsplet.HSPError;
import hsplet.variable.ByteString;
import hsplet.variable.Operand;
import hsplet.variable.Scalar;

import java.io.File;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * HSP の基本関数群。
 *
 * @author Yuki
 * @version $Revision: 1.6.4.1 $, $Date: 2006/08/02 12:13:07 $
 */
public class BasicFunction extends FunctionBase {

    /**
     * このクラスを含むソースファイルのバージョン文字列。
     */
    private static final String fileVersionID = "$Id: BasicFunction.java,v 1.6.4.1 2006/08/02 12:13:07 Yuki Exp $";

    public static int int_(final int i) {

        return i;
    }

    public static int rnd(final Context context, final Operand v, final int vi) {

        //return context.random.nextInt(Math.abs(toInt(v, vi, 0x80000000)));
        return context.random.rand() % Math.abs(toInt(v, vi, 0x80000000));
    }

    public static int strlen(final Context context, final ByteString str) {

        if (str == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "strlen", "str");
            return 0;
        }

        return str.length();
    }

    public static int length(final Context context, final Operand v, final int vi) {

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "length", "v");
            return 0;
        }

        return v.l0();
    }

    public static int length2(final Context context, final Operand v, final int vi) {

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "length2", "v");
            return 0;
        }

        return v.l1();
    }

    public static int length3(final Context context, final Operand v, final int vi) {

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "length3", "v");
            return 0;
        }

        return v.l2();
    }

    public static int length4(final Context context, final Operand v, final int vi) {

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "length4", "v");
            return 0;
        }

        return v.l3();
    }

    public static int vartype(final Context context, final String name) {

        if (name == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "vartype", "name");
            return 0;
        }

        if (name.equals("int")) {
            return Operand.Type.INTEGER;
        } else if (name.equals("str")) {
            return Operand.Type.STRING;
        } else if (name.equals("double")) {
            return Operand.Type.DOUBLE;
        } else {
            return Operand.Type.UNKNOWN;
        }
    }
    private final static Calendar calendar = Calendar.getInstance();

    public static Operand gettime(final Context context, final int type) {

        calendar.setTimeInMillis(System.currentTimeMillis());

        int result;

        switch (type) {
            case 0:
                result = calendar.get(Calendar.YEAR);
                break;
            case 1:
                result = calendar.get(Calendar.MONTH) + 1;
                break;
            case 2:
                result = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                break;
            case 3:
                result = calendar.get(Calendar.DAY_OF_MONTH);
                break;
            case 4:
                result = calendar.get(Calendar.HOUR_OF_DAY);
                break;
            case 5:
                result = calendar.get(Calendar.MINUTE);
                break;
            case 6:
                result = calendar.get(Calendar.SECOND);
                break;
            case 7:
                result = calendar.get(Calendar.MILLISECOND);
                break;
            default:
                context.error(HSPError.InvalidParameterValue, "gettime", "type==" + type);
                result = 0;
                break;
        }

        return Scalar.fromValue(result);
    }

    public static int peek(final Context context, final Operand v, final int vi, final int index) {

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "peek", "v");
            return 0;
        }

        return v.peek(vi, index) & 0xFF;
    }

    public static int wpeek(final Context context, final Operand v, final int vi, final int index) {

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "wpeek", "v");
            return 0;
        }

        return peek(context, v, vi, index) | (peek(context, v, vi, index + 1) << 8);
    }

    public static int lpeek(final Context context, final Operand v, final int vi, final int index) {

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "lpeek", "v");
            return 0;
        }

        return wpeek(context, v, vi, index) | (wpeek(context, v, vi, index + 2) << 16);
    }

    public static int varptr(final Context context, final Operand v, final int vi) {

        context.error(HSPError.UnsupportedOperation, "varptr");
        return 0;
    }

    public static int varuse(final Context context, final Operand v, final int vi) {

        context.error(HSPError.UnsupportedOperation, "varuse");
        return 0;
    }

    public static int noteinfo(final Context context, final int type) {

        switch (type) {
            case 0: // notemax
            {
                final ByteString note = context.note.toByteString(0);
                if (note.length() == 0) {
                    return 0;
                }

                int lineCount = 1;
                // 一番最後の \n は無視される
                for (int i = 0; i < note.length() - 1; ++i) {
                    if ((note.get(i) & 0xFF) == '\n') {
                        ++lineCount;
                    }
                }
                return lineCount;
            }
            case 1: // notesize
                return context.note.toByteString(0).length();
            default:
                context.error(HSPError.InvalidParameterValue, "noteinfo", "type==" + type);
                return 0;
        }
    }

    public static int instr(final Context context, final ByteString str, final int index, final ByteString substr) {

        if (str == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "instr", "str");
            return 0;
        }

        if (substr == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "instr", "substr");
            return 0;
        }
        
        if (index < 0 || index > str.length()) {
            Logger.getLogger(BasicFunction.class.getName()).log(Level.WARNING, "instr({0}, {1}, {2}) called!", new Object[] { str, Integer.valueOf(index), substr});
        };

        return str.indexOf(substr, index);
    }

    public static int abs(final int i) {

        return Math.abs(i);
    }

    public static int limit(final int val, final int min, final int max) {

        return Math.min(Math.max(min, val), max);

    }

    public static ByteString str(final Context context, final ByteString str) {

        if (str == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "str", "str");
            return new ByteString("");
        }

        return str;
    }
    public static final ByteString EMPTY_STRING = new ByteString("");

    public static ByteString strmid(final Context context, final ByteString str, final int index, final int count) {

        if (str == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "strmid", "str");
            return new ByteString("");
        }

        if (count < 0) {
            Logger.getLogger(BasicFunction.class.getName()).log(Level.WARNING,
                    "strmid(\"{0}\", {1}, {2}) was called!  We will be returning "
                    + "the empty string, but be aware that this may not be the "
                    + "behavior that you want!",
                    new Object[]{str, index, count});
            return EMPTY_STRING;
        }

        if (index >= 0) {
            return str.substring(index, count);
        } else {

            //return str.substring(str.length() + index, count);

            // behavior of OpenHSP's implementation
            if (index != -1) {
                Logger.getLogger(BasicFunction.class.getName()).log(Level.WARNING,
                        "strmid(str: \"{0}\", index: {1}, count: {2}) was called.  Negative values of "
                        + "index other than -1 do not behave in a well-defined manner.",
                        new Object[]{str, index, count});
            }
            return str.substring(str.length() - 1 - count, count);
        }
    }

    public static String strf(final Context context, final String format, final Operand v, final int vi) {

        if (format == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "strf", "format");
            return "";
        }

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "strf", "v");
            return "";
        }

        try {
            switch (v.getType()) {
                case Operand.Type.INTEGER:
                case Operand.Type.LABEL:
                    return Formatter.format(format, new Integer(v.toInt(vi)));
                case Operand.Type.DOUBLE:
                    return Formatter.format(format, new Double(v.toDouble(vi)));
                case Operand.Type.STRING:
                    return Formatter.format(format, v.toByteString(vi));
                default:
                    return format;
            }
        } catch (Exception e) {
            context.error(HSPError.InvalidParameterValue, "strf", "format=" + format + ", v=" + v.toString(vi));
            Logger.getLogger(BasicFunction.class.getName()).log(Level.SEVERE, null, e);
            return format;
        } catch (Throwable e) {
            context.error(HSPError.UnsupportedOperation, "strf");
            Logger.getLogger(BasicFunction.class.getName()).log(Level.SEVERE, null, e);
            return format;
        }
    }

    public static String getpath(final Context context, final String str, final int type) {

        if (str == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "getpath", "str");
            return "";
        }

        File file = new File(str);

        String result = str;

        if ((type & 1) != 0) {
            result = file.getName().lastIndexOf('.') >= 0 ? str.substring(0, str.length()
                    - (file.getName().length() - file.getName().lastIndexOf('.'))) : str;
        } else if ((type & 2) != 0) {
            result = file.getName().lastIndexOf('.') >= 0 ? file.getName().substring(file.getName().lastIndexOf('.'))
                    : "";
        }

        file = new File(result);

        if ((type & 8) != 0) {
            result = file.getName();
        } else if ((type & 32) != 0) {
            result = file.getParent();
        }

        if ((type & 16) != 0) {
            result = result.toLowerCase();
        }

        return result;
    }

    public static double sin(final double a) {

        return Math.sin(a);
    }

    public static double cos(final double a) {

        return Math.cos(a);
    }

    public static double tan(final double a) {

        return Math.tan(a);
    }

    public static double atan(final double y, final Operand xv, final int xvi) {

        return Math.atan2(y, toDouble(xv, xvi, 1.0));
    }

    public static double sqrt(final double a) {

        return Math.sqrt(a);
    }

    public static double double_(final double a) {

        return a;
    }

    public static double absf(final double a) {

        return Math.abs(a);
    }

    public static double expf(final double a) {

        return Math.exp(a);
    }

    public static double logf(final double a) {

        return Math.log(a);
    }

    public static double limitf(final double val, final double min, final double max) {

        return Math.min(Math.max(min, val), max);
    }
}
