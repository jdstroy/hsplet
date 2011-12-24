/*
 * $Id: BasicCommand.java,v 1.8 2006/03/26 14:35:37 Yuki Exp $
 */
package hsplet.function;

import hsplet.Context;
import hsplet.HSPError;
import hsplet.variable.ByteString;
import hsplet.variable.Operand;
import hsplet.variable.OperandInputStream;
import hsplet.variable.Scalar;
import hsplet.variable.StringArray;
import hsplet.variable.Variable;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * HSP �̊�{�R�}���h�Q�B
 * 
 * @author Yuki
 * @version $Revision: 1.8 $, $Date: 2006/03/26 14:35:37 $
 */
public class BasicCommand extends FunctionBase {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
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
				final URL url = context.getResourceURL(fileName);

				final URLConnection con = url.openConnection();
				
				try{
					final InputStream in = con.getInputStream();
					try{
						if ( in==null ) {
							context.strsize.value = -1;
						}
						else if ( con.getContentLength()>=0 ){
								context.strsize.value = con.getContentLength();								
						}
						else {

							final byte[] buf = new byte[1024];
							
							context.strsize.value = 0;
							for(;;){
								int length = in.read(buf);
								if ( length<0) {
									break;
								}
								
								context.strsize.value += length;
							}
						}

					}finally{
						in.close();
					}
				}catch( IOException e ) {
					context.strsize.value = -1;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			context.strsize.value = -1;
		}

	}

	public static void delete(final Context context, final String fileName) {

		context.error(HSPError.UnsupportedOperation, "delete");
	}

	public static void mkdir(final Context context, final String fileName) {

		context.error(HSPError.UnsupportedOperation, "mkdir");
	}

	public static void chdir(final Context context, final String dirName) {

		if (dirName == null) {
			context.error(HSPError.ParameterCannotBeOmitted, "chdir", "dirName");
			return;
		}

		try {
			final String rel = dirName.replace('\\', '/');
			context.curdir = new URL(context.curdir, rel + (rel.endsWith("/") ? "" : "/"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public static void dirlist(final Context context, final Operand v, final int vi) {

		context.stat.value = 0;
		context.error(HSPError.UnsupportedOperation, "dirlist");
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

			final InputStream in = context.getResource(fileName);

			if (in == null) {
				context.error(HSPError.FileNotFound, "bload", fileName);
				return;
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
					// �ϐ��o�b�t�@�I�[�o�[
				}

				context.strsize.value = readedDataSize;

			} finally {
				in.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
			context.error(HSPError.FileNotFound, "bload", fileName);
		}

	}

	public static void bsave(final Context context, final String fileName, final Operand v, final int vi,
			final Operand sizev, final int sizevi, final int offset) {

		context.error(HSPError.UnsupportedOperation, "bsave");

	}

	public static void bcopy(final Context context, final Operand v, final int vi) {

		context.error(HSPError.UnsupportedOperation, "bcopy");

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

	public static void memexpand(final Context context, final Operand v, final int vi) {

		context.error(HSPError.UnsupportedOperation, "memexpand");
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

		// ������^�Ȃ�g�p���̃o�b�t�@���Ԃ��Ă���͂��B
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

	public static void notedel(final Context context, final int line) {

		// ������^�Ȃ�g�p���̃o�b�t�@���Ԃ��Ă���͂��B
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

		// ������^�Ȃ�g�p���̃o�b�t�@���Ԃ��Ă���͂��B
		final ByteString note = context.note.toByteString(0);
		note.set(0, (byte) 0);

		try {
			final InputStream in = context.getResource(fileName);

			if (in == null) {
				context.error(HSPError.FileNotFound, "noteload", fileName);
				return;
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

		} catch (Exception e) {
			e.printStackTrace();
			context.error(HSPError.FileNotFound, "noteload", fileName);
		}

	}

	public static void notesave(final Context context, final Operand v, final int vi) {

		context.error(HSPError.UnsupportedOperation, "notesave");
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

		// ������^�Ȃ�g�p���̃o�b�t�@���Ԃ��Ă���͂��B
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