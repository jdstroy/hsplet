/*
 * $Id: BasicCommand.java,v 1.8 2006/03/26 14:35:37 Yuki Exp $
 */
package hsplet.function;

import hsplet.Context;
import hsplet.HSPError;
import hsplet.variable.ByteString;
import hsplet.variable.IExpandable;
import hsplet.variable.Operand;
import hsplet.variable.OperandInputStream;
import hsplet.variable.Scalar;
import hsplet.variable.StringArray;
import hsplet.variable.Variable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * HSP の基本コマンド群。
 *
 * @author Yuki
 * @version $Revision: 1.8 $, $Date: 2006/03/26 14:35:37 $
 */
public class BasicCommand extends FunctionBase {

    /**
     * このクラスを含むソースファイルのバージョン文字列。
     */
    private static final String fileVersionID = "$Id: BasicCommand.java,v 1.8 2006/03/26 14:35:37 Yuki Exp $";

    public static void onexit(final Context context, final JumpStatement jump, final Operand v, final int vi) {

        if (v != null && v.getType() == Operand.Type.LABEL) {

            context.onexit.label = toInt(v, vi, 0);
            context.onexit.jump = jump == null ? JumpStatement.Goto : jump;
            context.onexit.enabled = true;

        } else {

            context.onexit.enabled = toInt(v, vi, 0) != 0;
        }
    }

    public static void onerror(final Context context, final JumpStatement jump, final Operand v, final int vi) {

        if (v != null && v.getType() == Operand.Type.LABEL) {

            context.onerror.label = toInt(v, vi, 0);
            context.onerror.jump = jump == null ? JumpStatement.Goto : jump;
            context.onerror.enabled = true;

        } else {

            context.onerror.enabled = toInt(v, vi, 0) != 0;
        }
    }

    public static void onkey(final Context context, final JumpStatement jump, final Operand v, final int vi) {

        if (v != null && v.getType() == Operand.Type.LABEL) {

            context.onkey.label = toInt(v, vi, 0);
            context.onkey.jump = jump == null ? JumpStatement.Goto : jump;
            context.onkey.enabled = true;

        } else {

            context.onkey.enabled = toInt(v, vi, 0) != 0;
        }
    }

    public static void onclick(final Context context, final JumpStatement jump, final Operand v, final int vi) {

        if (v != null && v.getType() == Operand.Type.LABEL) {

            context.onclick.label = toInt(v, vi, 0);
            context.onclick.jump = jump == null ? JumpStatement.Goto : jump;
            context.onclick.enabled = true;

        } else {

            context.onclick.enabled = toInt(v, vi, 0) != 0;
        }
    }

    public static void oncmd(final Context context, final JumpStatement jump, final Operand v, final int vi,
            final int message) {

        if (v != null && v.getType() == Operand.Type.LABEL) {

            context.oncmd(context.targetWindow, message).label = toInt(v, vi, 0);
            context.oncmd(context.targetWindow, message).jump = jump == null ? JumpStatement.Goto : jump;
            context.oncmd(context.targetWindow, message).enabled = true;

        } else {

            context.oncmd(context.targetWindow, message).enabled = toInt(v, vi, 0) != 0;
        }
    }

    public static void exist(final Context context, final String fileName) {

        if (fileName == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "exist", "fileName");
            return;
        }

        try {

            if (fileName.startsWith("MEM:")) {
                context.strsize.value = context.memfile.getSize();
            } else {
                final URL url = context.resolve(fileName).toURL();

                final URLConnection con = url.openConnection();

                try {
                    final InputStream in = con.getInputStream();
                    try {
                        if (in == null) {
                            context.strsize.value = -1;
                        } else if (con.getContentLength() >= 0) {
                            context.strsize.value = con.getContentLength();
                        } else {

                            final byte[] buf = new byte[1024];

                            context.strsize.value = 0;
                            for (;;) {
                                int length = in.read(buf);
                                if (length < 0) {
                                    break;
                                }

                                context.strsize.value += length;
                            }
                        }

                    } finally {
                        in.close();
                    }
                } catch (IOException e) {
                    context.strsize.value = -1;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            context.strsize.value = -1;
        }

    }

    public static void delete(final Context context, final String fileName) {
        try {
            boolean retval = new File(context.resolve(fileName)).delete();
            context.stat.value = retval ? 1 : 0;
        } catch (URISyntaxException ex) {
            Logger.getLogger(BasicCommand.class.getName()).log(Level.SEVERE, null, ex);
            context.stat.value = 2;
        }
    }

    public static void mkdir(final Context context, final String fileName) {
        try {
            boolean retval = new File(context.resolve(fileName)).mkdir();
            context.stat.value = retval ? 0 : 1;
        } catch (URISyntaxException ex) {
            Logger.getLogger(BasicCommand.class.getName()).log(Level.SEVERE, null, ex);
            context.stat.value = 2;
        }
    }

    public static void chdir(final Context context, final String dirName) {
        try {
            if (dirName == null) {
                context.error(HSPError.ParameterCannotBeOmitted, "chdir", "dirName");
                return;
            }

            final String rel = dirName.replace('\\', '/');
            context.curdir = new URL(context.curdir, rel + (rel.endsWith("/") ? "" : "/"));
            Logger.getLogger(BasicCommand.class.getName()).log(Level.INFO, "chdir: Requested {0}, gave {1}", new Object[]{dirName, context.curdir});
        } catch (MalformedURLException ex) {
            Logger.getLogger(BasicCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static File cwd(final Context context) throws URISyntaxException {
        return new File(context.curdir.toURI());
    }

    private static int firstWildcard(final CharSequence s1) {
        for (int i = 0; i < s1.length(); i++) {
            switch (s1.charAt(i)) {
                case '?':
                case '*':
                    return i;
            }
        }
        return -1;
    }

    private static int findLastNonWildcardSegment(String globberPattern) {
        // Find the first * or ?
        // Grab the substring from 0 to index of last / before the first * or ?
        // If * or ? do not exist, then grab up to the the last /; otherwise, 
        // grab nothing
        int firstWildcardIndex = firstWildcard(globberPattern);
        if (firstWildcardIndex == -1) {
            // no wildcard
            return globberPattern.lastIndexOf('/');
        } else {
            int slash = globberPattern.lastIndexOf('/', firstWildcardIndex);
            return slash;
        }
    }

    public static void dirlist(final Context context, final ByteString result, final String mask, int mode) {
        try {
            // We might get an IllegalArgumentException.  We shall see.
            /*
             * Is it using /? Probably was coded with HSPlet/Java/*nix in mind.
             * If it isn't, we'll assume we're on Windows.
             */
            final String new_mask = winPathToNetPath(mask);
            // Need to parse new_mask - split between the dirname and mask
            int index = findLastNonWildcardSegment(new_mask);

            URI subdir;
            String file_mask;
            if (index == -1) {
                subdir = cwd(context).toURI();
                file_mask = new_mask;
            } else {
                subdir = cwd(context).toURI().resolve(new_mask.substring(0, index));
                file_mask = new_mask.substring(index + 1, new_mask.length());
            }
            File[] dirlist = new File(subdir).listFiles(
                    new Globber(
                    file_mask,
                    mode));
            
            StringBuilder sb = new StringBuilder();
            List<File> fList = Arrays.asList(dirlist);
            for (Iterator<File> it = fList.iterator(); it.hasNext();) {
                File f = it.next();
                sb.append(f.getName());
                if (it.hasNext()) {
                    sb.append('\n');
                }
            }

            result.assign(new ByteString(sb.toString()));
            context.stat.value = dirlist.length; // OK; if no entries, return 0
        } catch (URISyntaxException ex) {
            context.stat.value = 0; // Fail
            Logger.getLogger(BasicCommand.class.getName()).log(Level.SEVERE, "Tried to convert to File URI but failed", ex);
        } catch (Exception ex) {
            context.stat.value = 0; // Fail
            Logger.getLogger(BasicCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void bload(final Context context, final String fileName, final Operand v, final int vi,
            final Operand sizev, final int sizevi, final int offset) {

        if (fileName == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "bload", "fileName");
            return;
        }

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "bload", "v");
            return;
        }

        final int size = toInt(sizev, sizevi, -1);

        try {

            InputStream in = context.getResource(fileName);

            if (in == null) {
                try {
                    Logger.getLogger(BasicCommand.class.getName()).log(Level.INFO,
                            "noteload {0} => {1}",
                            new Object[]{
                                fileName,
                                context.resolve(fileName)
                            });
                    in = new FileInputStream(new File(context.resolve(fileName)));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(BasicCommand.class.getName()).log(Level.SEVERE, null, ex);
                    context.error(HSPError.FileNotFound, "noteload", fileName);
                    return;
                }
            }

            try {

                final byte[] tmp = new byte[256];

                int index = 0;

                while (index < offset) {

                    int l = in.read(tmp, 0, Math.min(256, offset - index));
                    if (l < 0) {
                        index = offset;
                        break;
                    } else {
                        index += l;
                    }
                }

                int readedDataSize = 0;
                try {
                    while (size < 0 || index < offset + size) {

                        int l = in.read(tmp, 0, Math.min(256, size < 0 ? 256 : offset + size - index));
                        if (l < 0) {
                            break;
                        } else {

                            for (int i = 0; i < l; ++i) {
                                v.poke(vi, readedDataSize, tmp[i]);
                                ++readedDataSize;
                            }

                            index += l;
                        }

                    }

                } catch (ArrayIndexOutOfBoundsException e) {
                    // 変数バッファオーバー
                }

                context.strsize.value = readedDataSize;

            } finally {
                in.close();
            }

        } catch (URISyntaxException ex) {
            context.error(HSPError.FileNotFound, "bload", fileName);
            Logger.getLogger(BasicCommand.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            context.error(HSPError.FileNotFound, "bload", fileName);
            Logger.getLogger(BasicCommand.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void bsave(final Context context, final String fileName, final Operand v, final int vi,
            final Operand sizev, final int sizevi, final int offset) {
        FileOutputStream out = null;

        try {

            if (fileName == null) {
                context.error(HSPError.ParameterCannotBeOmitted, "bload", "fileName");
                return;
            }

            if (v == null) {
                context.error(HSPError.ParameterCannotBeOmitted, "bload", "v");
                return;
            }

            final int size = toInt(sizev, sizevi, -1);

            File outputFile = new File(context.resolve(fileName));
            out = new FileOutputStream(outputFile);

            out.getChannel().position(offset);

            try {

                try {
                    for (int i = 0; i < size; i++) {
                        out.write(v.peek(vi, i));
                    }

                } catch (ArrayIndexOutOfBoundsException e) {
                    // 変数バッファオーバー
                }

                //context.strsize.value = readedDataSize;

            } finally {
                out.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(BasicCommand.class.getName()).log(Level.SEVERE, null, ex);
            context.error(HSPError.FileNotFound, "bload", fileName);
        } catch (URISyntaxException ex) {
            Logger.getLogger(BasicCommand.class.getName()).log(Level.SEVERE, null, ex);
            context.error(HSPError.FileNotFound, "bload", fileName);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(BasicCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void bcopy(final Context context, String fileName, String target) {

        //context.error(HSPError.UnsupportedOperation, "bcopy "+fileName+" "+target);
        if (fileName == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "bcopy", "fileName");
            return;
        }
        if (target == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "bcopy", "target");
            return;
        }
        try {
            File sourceFile = new File(context.resolve(fileName));

            if (!sourceFile.exists()) {
                context.error(HSPError.FileNotFound, fileName);
                return;
            }
            File targetFile = new File(context.resolve(target));
            File dir = targetFile.getParentFile();
            if ((dir != null) && (!dir.exists())) {
                dir.mkdirs();
            }
            Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (URISyntaxException ex) {
            Logger.getLogger(BasicCommand.class.getName()).log(Level.SEVERE, "Tried to convert to File URI but failed", ex);
        } catch (IOException ex) {
            context.error(HSPError.ErrorOnExecution, "bcopy", "Failed to copy " + fileName + " to " + target);
        }

    }

    public static void memfile(final Context context, final Operand v, final int vi, final int base, final int size) {

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "memfile", "v");
            return;
        }

        context.memfile = new OperandInputStream(v, vi, base, size);

    }

    public static void poke(final Context context, final Operand v, final int vi, final int index, final Operand sv,
            final int svi) {

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "poke", "v");
            return;
        }

        if (sv != null && sv.getType() == Operand.Type.STRING) {

            final ByteString s = sv.toByteString(svi);

            for (int i = 0; i < s.length(); ++i) {

                v.poke(vi, index + i, s.get(i));
            }

        } else {
            v.poke(vi, index, (byte) toInt(sv, svi, 0));
        }
    }

    public static void wpoke(final Context context, final Operand v, final int vi, final int index, final int word) {

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "wpoke", "v");
            return;
        }

        v.poke(vi, index, (byte) (word & 0xFF));
        v.poke(vi, index + 1, (byte) ((word >> 8) & 0xFF));
    }

    public static void lpoke(final Context context, final Operand v, final int vi, final int index, final int dword) {

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "lpoke", "v");
            return;
        }

        v.poke(vi, index, (byte) (dword & 0xFF));
        v.poke(vi, index + 1, (byte) ((dword >> 8) & 0xFF));
        v.poke(vi, index + 2, (byte) ((dword >> 16) & 0xFF));
        v.poke(vi, index + 3, (byte) ((dword >> 24) & 0xFF));
    }

    public static void getstr(final Context context, final Operand v, final int vi, final ByteString str,
            final int offset, final int separator) {

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "getstr", "v");
            return;
        }

        if (str == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "getstr", "str");
            return;
        }

        int length;
        for (length = 0; offset + length < str.length(); ++length) {
            final int ch = str.get(offset + length) & 0xFF;
            if (ch == 0 || ch == '\r' || ch == separator) {
                if (ch == 0) {
                    context.strsize.value = length;
                } else if (ch == '\r' && (str.get(offset + length + 1) & 0xFF) == '\n') {
                    context.strsize.value = length + 2;
                } else {
                    context.strsize.value = length + 1;
                }
                break;
            }
        }
        v.assign(vi, Scalar.fromValue(str.substring(offset, length)), 0);
    }

    public static void chdpm(final Context context, final Operand v, final int vi) {

        context.error(HSPError.UnsupportedOperation, "chdpm");
    }

    public static void memexpand(final Context context, final Operand v, final int vi, final int newSize) {

        if (v instanceof IExpandable) {
            ((IExpandable) v).expand(newSize);
        } else {
            context.error(HSPError.UnsupportedOperation, "memexpand");
        }
    }

    public static void memcpy(final Context context, final Operand dv, final int dvi, final Operand sv, final int svi,
            final int size, final int doff, final int soff) {

        if (dv == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "memcpy", "dv");
            return;
        }

        if (sv == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "memcpy", "sv");
            return;
        }

        if (dv == sv && doff > soff) {
            for (int i = size - 1; i >= 0; --i) {
                dv.poke(dvi, doff + i, sv.peek(svi, soff + i));
            }
        } else {

            for (int i = 0; i < size; ++i) {
                dv.poke(dvi, doff + i, sv.peek(svi, soff + i));
            }
        }

    }

    public static void memset(final Context context, final Operand v, final int vi, final int s, final int size,
            final int offset) {

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "memset", "v");
            return;
        }
        if (offset < 0) {
            context.error(HSPError.InvalidParameterValue, "memset", "offset==" + offset);
            return;
        }

        for (int i = 0; i < size; ++i) {
            v.poke(vi, offset + i, (byte) s);
        }

    }

    public static void notesel(final Context context, final Operand v, final int vi) {

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "notesel", "v");
            return;
        }

        if (v.getType() != Operand.Type.STRING) {
            if (v instanceof Variable) {
                //System.out.println(((Variable)v).errorIndex()+" Assign change(note): "+v.getType()+" "+Operand.Type.STRING);
                ((Variable) v).value = new StringArray();
            } else {
                context.error(HSPError.ParameterTypeMismatch, "notesel", "vartype( v )==" + v.getType());
            }
        }

        if (context.oldNotes.size() > 512) {
            context.oldNotes.remove(0);
        }
        context.oldNotes.add(context.note);
        context.note = v.ref(vi);
    }

    public static void noteadd(final Context context, final ByteString str, final Operand linev, final int linei,
            final int overwrite) {

        if (str == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "noteadd", "str");
            return;
        }

        final int line = toInt(linev, linei, -1);

        // 文字列型なら使用中のバッファが返ってくるはず。
        final ByteString note = context.note.toByteString(0);

        int lineIndex = note.lineIndex(line);
        int lineLength = note.nextLineIndex(lineIndex) - lineIndex;

        if (lineIndex == note.length()) {
            if (note.length() >= 1 && note.get(note.length() - 1) != '\n') {
                note.append(new ByteString("\r\n"));
                lineIndex += 2;
            }
        }

        if (overwrite == 0) {
            lineLength = 0;
        }

        final ByteString lineStr = new ByteString(str, true);
        lineStr.append(new ByteString("\r\n"));
        note.replace(lineIndex, lineLength, lineStr);

    }

    private static String winPathToNetPath(String winpath) {
        String output = winpath.replace('\\', '/');
        //String output = winpath.contains("/") ? winpath : winpath.replace('\\', '/');
        return output.startsWith("/") ? output.substring(1) : output;
    }

    private static String safeWinPath2NetPath(String winpath) {
        String output = winpath.contains("/") ? winpath : winpath.replace('\\', '/');
        return output.startsWith("/") ? output.substring(1) : output;
    }

    public static void notedel(final Context context, final int line) {

        // 文字列型なら使用中のバッファが返ってくるはず。
        final ByteString note = context.note.toByteString(0);

        int lineIndex = note.lineIndex(line);
        int lineLength = note.nextLineIndex(lineIndex) - lineIndex;

        note.replace(lineIndex, lineLength, new ByteString(""));
    }

    public static void noteload(final Context context, final String fileName, final Operand sizev, final int sizevi) {

        if (fileName == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "noteload", "fileName");
            return;
        }

        final int size = toInt(sizev, sizevi, -1);
        final int offset = 0;

        // 文字列型なら使用中のバッファが返ってくるはず。
        final ByteString note = context.note.toByteString(0);
        note.set(0, (byte) 0);

        try {
            InputStream in = context.getResource(fileName);

            if (in == null) {
                // One more thing to try
                try {
                    Logger.getLogger(BasicCommand.class.getName()).log(Level.INFO,
                            "noteload {0} => {1}",
                            new Object[]{
                                fileName,
                                context.resolve(fileName)
                            });
                    in = new FileInputStream(new File(context.resolve(fileName)));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(BasicCommand.class.getName()).log(Level.SEVERE, null, ex);
                    context.error(HSPError.FileNotFound, "noteload", fileName);
                    return;
                }
            }

            try {

                final byte[] tmp = new byte[256];

                int index = 0;

                while (index < offset) {

                    int l = in.read(tmp, 0, Math.min(256, offset - index));
                    if (l < 0) {
                        index = offset;
                        break;
                    } else {
                        index += l;
                    }
                }

                while (size < 0 || index < offset + size) {

                    int l = in.read(tmp, 0, Math.min(256, size < 0 ? 256 : offset + size - index));
                    if (l < 0) {
                        break;
                    } else {
                        note.append(new ByteString(tmp, 0, l, false));

                        index += l;
                    }

                }

            } finally {
                in.close();
            }

        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(BasicCommand.class.getName()).log(Level.SEVERE, null, ex);
            context.error(HSPError.FileNotFound, "noteload", fileName);
        }

    }

    public static void notesave(final Context context, String filename) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(context.resolve(filename)));
            context.note.toByteString(0).dump(out);
        } catch (URISyntaxException ex) {
            Logger.getLogger(BasicCommand.class.getName()).log(Level.SEVERE, null, ex);
            context.error(HSPError.FileNotFound, "notesave");
        } catch (IOException ex) {
            Logger.getLogger(BasicCommand.class.getName()).log(Level.SEVERE, null, ex);
            context.error(HSPError.FileNotFound, "notesave");
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(BasicCommand.class.getName()).log(Level.FINER, null, ex);
                }
            }
        }
    }

    public static void randomize(final Context context, final Operand v, final int vi) {

        //		context.random = new Random(toInt(v, vi, (int) System.currentTimeMillis()));
        context.random.srand(toInt(v, vi, (int) System.currentTimeMillis()));

    }

    public static void noteunsel(final Context context) {

        if (context.oldNotes.size() != 0) {
            context.note = (Operand) context.oldNotes.pop();
        }
    }

    public static void noteget(final Context context, final Operand v, final int vi, final int line) {

        if (v == null) {
            context.error(HSPError.ParameterCannotBeOmitted, "noteget", "v");
            return;
        }

        // 文字列型なら使用中のバッファが返ってくるはず。
        final ByteString note = context.note.toByteString(0);

        int lineIndex = note.lineIndex(line);
        int lineLength = note.nextLineIndex(lineIndex) - lineIndex;

        final ByteString string = note.substring(lineIndex, lineLength);

        if (string.length() >= 1 && string.get(string.length() - 1) == '\n') {

            string.set(string.length() - 1, (byte) 0);
            if (string.length() >= 1 && string.get(string.length() - 1) == '\r') {
                string.set(string.length() - 1, (byte) 0);
            }
        }
        v.assign(vi, Scalar.fromValue(string), 0);
    }
}
