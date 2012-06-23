/*
 * $Id: DefaultRuntimeInfo.java,v 1.6 2006/01/16 19:34:22 Yuki Exp $
 */
package hsplet.compiler;

import hsplet.compiler.ByteCode.Code;
import hsplet.function.BasicCommand;
import hsplet.function.BasicFunction;
import hsplet.function.GuiCommand;
import hsplet.function.GuiFunction;
import hsplet.function.ProgramCommand;
import hsplet.variable.ByteString;

import java.lang.reflect.Method;

/**
 * デフォルトのラインタイム情報。
 * 
 * @author Yuki
 * @version $Revision: 1.6 $, $Date: 2006/01/16 19:34:22 $
 */
public class DefaultRuntimeInfo implements RuntimeInfo {

	private ClassLoader extLibLoader;

	/**
	 * オブジェクトを構築する。
	 * @param extLibLoader 拡張ライブラリを読み込むためのクラスローダ。
	 */
	public DefaultRuntimeInfo(final ClassLoader extLibLoader) {
		this.extLibLoader = extLibLoader;
	}

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: DefaultRuntimeInfo.java,v 1.6 2006/01/16 19:34:22 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = -9212746715730027401L;

	private static final String[][] basicFunctions = new String[][] {
			new String[] { "int_", "rnd", "strlen", "length", "length2",
					"length3", "length4", "vartype", "gettime", "peek",
					"wpeek", "lpeek", "varptr", "varuse", "noteinfo", "instr",
					"abs", "limit" },
			new String[] { "str", "strmid", null, "strf", "getpath" },
			new String[] { "sin", "cos", "tan", "atan", "sqrt", "double_",
					"absf", "expf", "logf", "limitf" }, };

	private static final String[] basicCommands = new String[] { "onexit",
			"onerror", "onkey", "onclick", "oncmd", null, null, null, null,
			null, null, null, null, null, null, null, null, "exist", "delete",
			"mkdir", "chdir", "dirlist", "bload", "bsave", "bcopy", "memfile",
			"poke", "wpoke", "lpoke", "getstr", "chdpm", "memexpand", "memcpy",
			"memset", "notesel", "noteadd", "notedel", "noteload", "notesave",
			"randomize", "noteunsel", "noteget", };

	private static final String[] guiCommands = new String[] { "button",
			"chgdisp", "exec", "dialog", null, null, null, null, "mmload",
			"mmplay", "mmstop", "mci", "pset", "pget", "syscolor", "mes", // <
			// 0x10
			"title", "pos", "circle", "cls", "font", "sysfont", "objsize",
			"picload", "color", "palcolor", "palette", "redraw", "width",
			"gsel", "gcopy", "gzoom", // < 0x20
			"gmode", "bmpsave", "hsvcolor", "getkey", "listbox", "chkbox",
			"combox", "input", "mesbox", "buffer", "screen", "bgscr", "mouse",
			"objsel", "groll", // <0x30
			"line", "clrobj", "boxf", "objprm", "objmode", "stick", "grect",
			"grotate", "gsquare", };

	private static final String[] programCommands = new String[] {

	"goto_", "gosub", "return_", "break", "repeat", "loop", "continue_",
			"wait", "await", "dim", "sdim", "foreach", "foreachCheck",
			"dimtype", "dup", "dupptr", // <0x10
			"end", "stop", "newmod", "setmod", "delmod", "alloc", "mref",
			"run", "exgoto", "on", "mcall", "assert_", "logmes",

	};

	private static final String[][] guiFunctions = new String[][] {
			new String[] { "mousex", "mousey", "mousew", "hwnd", "hinstance",
					"hdc" },
			new String[] { "ginfo", "objinfo", "dirinfo", "sysinfo" } };

	public Class getClassFor(final ByteCode ax, final Code code) {

		switch (code.type) {
		case ByteCode.Code.Type.IntCmd:
			return BasicCommand.class;
		case ByteCode.Code.Type.ExtCmd:
			return GuiCommand.class;
		case ByteCode.Code.Type.IntFunc:
			return BasicFunction.class;
		case ByteCode.Code.Type.ExtSysVar:
			return GuiFunction.class;
		case ByteCode.Code.Type.ProgCmd:
			return ProgramCommand.class;
		case ByteCode.Code.Type.ModCmd:
			return ProgramCommand.class;
		case ByteCode.Code.Type.DllFunc: {
			final ByteCode.Function func = ax.functions[code.value];
			final ByteCode.Library lib = ax.libraries[func.index];
			final String libName = new ByteString(ax.datas, lib.nameidx, false)
					.toString();

			try {
				if (libName.toLowerCase().endsWith(".dll") || libName.toLowerCase().endsWith(".hpi")) {
					return extLibLoader.loadClass(libName.substring(0, libName
							.lastIndexOf('.')));
				} else {
					return extLibLoader.loadClass(libName);

				}
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}

		}
                    case ByteCode.Code.Type.DllCtrl:
                        try {
				return extLibLoader.loadClass("DllCtrlClass");
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
                        
		default:
			throw new RuntimeException("命令タイプ " + code.type
					+ " はクラスと関連付けられません。");
		}
	}

	public Method getMethodFor(final ByteCode ax, final Code code, final String name) {
		final Class clazz = getClassFor(ax, code);
		final Method[] methods = clazz.getMethods();
		
		for( int i = 0; i<methods.length; ++i ){
			final Method m = methods[i];
			if (m.getName().equals(name)) {
				return m;
			}
		}
		return null;
	}
	public Method getMethodFor(final ByteCode ax, final Code code) {

		final Class clazz = getClassFor(ax, code);
		final String name;

		switch (code.type) {
		case ByteCode.Code.Type.IntCmd:
			name = basicCommands[code.value];
			break;
		case ByteCode.Code.Type.ExtCmd:
			name = guiCommands[code.value];
			break;
		case ByteCode.Code.Type.IntFunc:
			switch (code.value & 0x180) {
			case 0x000:
				name = basicFunctions[0][code.value & 0x7F];
				break;
			case 0x100:
				name = basicFunctions[1][code.value & 0x7F];
				break;
			case 0x180:
				name = basicFunctions[2][code.value & 0x7F];
				break;
			default:
				throw new UnsupportedOperationException("内部関数 " + code.value
						+ " はサポートされていません。");
			}
			break;
		case ByteCode.Code.Type.ExtSysVar:
			switch (code.value & 0x100) {
			case 0x000:
				name = guiFunctions[0][code.value & 0xFF];
				break;
			case 0x100:
				name = guiFunctions[1][code.value & 0xFF];
				break;
			default:
				throw new UnsupportedOperationException("拡張 GUI 関数 "
						+ code.value + " はサポートされていません。");
			}
			break;
		case ByteCode.Code.Type.ProgCmd:
			name = programCommands[code.value];
			break;
		case ByteCode.Code.Type.ModCmd:
			name = "call";
			break;
		case ByteCode.Code.Type.DllFunc: {
			final ByteCode.Function func = ax.functions[code.value];

			final String rawName = new ByteString(ax.datas, func.nameidx, false)
					.toString();
			if (rawName.indexOf('@') >= 0) {
				if (rawName.charAt(0) == '_') {
					name = rawName.substring(1, rawName.indexOf('@'));
				} else {
					name = rawName.substring(0, rawName.indexOf('@'));
				}
			} else {
				name = rawName;
			}
		}
			break;
                    case ByteCode.Code.Type.DllCtrl:
                        name = DllCtrlClassStub.getMethodNameBySubTypeId(code.value);
                        break;
		default:
			throw new RuntimeException("命令タイプ " + code.type);
		}

		final Method[] methods = clazz.getMethods();
		
		for( int i = 0; i<methods.length; ++i ){
			final Method m = methods[i];
			if (m.getName().equals(name)) {
				return m;
			}
		}
		return null;
	}

}
