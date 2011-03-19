/*
 * $Id: ByteCode.java,v 1.2 2006/01/13 20:32:11 Yuki Exp $
 */
package hsplet.compiler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * HSP の中間コード(*.ax)のデータをあらわすデータクラス。
 * 
 * 中間コードは大きく分けて、
 * <ul>
 * <li>ヘッダ</li>
 * <li>コードセグメント</li>
 * <li>データセグメント</li>
 * <li>ラベルオフセット</li>
 * <li>デバッグ情報</li>
 * <li>ライブラリ情報</li>
 * <li>関数情報</li>
 * <li>関数のパラメータ情報</li>
 * <li>古い関数情報(HSPLet では使用しない)</li>
 * <li>HPI情報(HSPLet では使用しない)</li>
 * </ul>
 * から成り立っている。
 * 
 * <p>
 * このクラスは不変なので、コンストラクタにバイトデータを渡すことで初期化する。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/01/13 20:32:11 $
 */
public final class ByteCode implements Serializable {

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = 6171509589300649390L;

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: ByteCode.java,v 1.2 2006/01/13 20:32:11 Yuki Exp $";

	/** ヘッダに格納されていた情報。 */
	public final Header header;

	/** コードセグメントの内容。 */
	public final Code[] codes;

	/** データセグメントの内容。 */
	public final byte[] datas;

	/**
	 * ラベルのオフセットの配列。
	 * <p>
	 * この値は、 codes[?].offset で得られる値になる。 codes 内でのインデックスではないので注意すること。
	 * </p>
	 * 
	 */
	public final int[] labels;

	/** デバッグ情報。 */
	public final int[] debugInfo;

	/** ライブラリ情報。 */
	public final Library[] libraries;

	/** 関数情報。 */
	public final Function[] functions;

	/** 関数のパラメータ情報。 */
	public final Parameter[] parameters;

	/**
	 * メモリ上のバイト配列から中間コードを読み込む。
	 * 
	 * @param bytes 中間コードが格納された配列。
	 * @throws IOException 読み取りエラーが発生したとき。
	 */
	public ByteCode(final byte[] bytes) throws IOException {

		this(new ByteArrayInputStream(bytes));
	}

	/**
	 * 与えられたストリームから中間コードを読み込む。
	 * 
	 * @param stream 中間コードが格納されたストリーム。
	 * @throws IOException 読み取りエラーが発生したとき。
	 */
	public ByteCode(final InputStream stream) throws IOException {

		this.in = new LEInputStream(stream);

		this.header = new Header(in);
		this.codes = readCodes();
		this.datas = readDatas();
		this.labels = readLabels();
		this.debugInfo = readDebugInfo();
		this.libraries = readLibraries();
		this.functions = readFunctions();
		this.parameters = readParameters();

		this.in = null;
	}

	/**
	 * 当に情報を読み取っているストリーム。
	 */
	private LEInputStream in;

	private Code[] readCodes() throws IOException {

		in.seekTo(header.codes.offset);

		final Code[] codes = new Code[header.codes.size / 2];

		int codeCount = 0;
		while (in.getOffset() < header.codes.offset + header.codes.size) {

			codes[codeCount++] = new Code(in);

			// if/else のときは次のWORDが移動量（相対 WORD 数）をあらわす。
			if (codes[codeCount - 1].type == Code.Type.CmpCmd) {
				codes[codeCount++] = new Code(Code.Type.JumpOffset, in
						.readShort() * 2);
			}
		}

		codeCount -= 2; // 最後の二つ(goto *lastLabel)はとりあえずいらない。

		final Code[] result = new Code[codeCount];

		System.arraycopy(codes, 0, result, 0, codeCount);

		return result;
	}

	private byte[] readDatas() throws IOException {

		in.seekTo(header.datas.offset);

		final byte[] result = new byte[header.datas.size];

		in.readBytes(result);

		return result;
	}

	private int[] readLabels() throws IOException {

		in.seekTo(header.labels.offset);

		final int[] result = new int[header.labels.size / 4];

		for (int index = 0; index < result.length; ++index) {

			// ラベルの値はコードセグメントの先頭からの相対アドレスになっている。
			// WORD 単位なので2倍してやる必要がある。

			result[index] = header.codes.offset + in.readInt() * 2;
		}

		return result;
	}

	private int[] readDebugInfo() throws IOException {

		in.seekTo(header.debugInfo.offset);

		final int[] result = new int[header.debugInfo.size / 4];

		for (int index = 0; index < result.length; ++index) {
			result[index] = in.readInt();
		}

		return result;
	}

	private Library[] readLibraries() throws IOException {

		in.seekTo(header.libraries.offset);

		final Library[] result = new Library[header.libraries.size
				/ Library.SIZE];

		for (int index = 0; index < result.length; ++index) {
			result[index] = new Library(in);
		}

		return result;
	}

	private Function[] readFunctions() throws IOException {

		in.seekTo(header.functions.offset);

		final Function[] result = new Function[header.functions.size
				/ Function.SIZE];

		for (int index = 0; index < result.length; ++index) {

			result[index] = new Function(in);
		}

		return result;
	}

	private Parameter[] readParameters() throws IOException {

		in.seekTo(header.parameters.offset);

		final Parameter[] result = new Parameter[header.parameters.size
				/ Parameter.SIZE];

		for (int index = 0; index < result.length; ++index) {
			result[index] = new Parameter(in);
		}

		return result;
	}

	/**
	 * オブジェクトファイル中のヘッダを表すクラス。
	 * 
	 * @author Yuki
	 */
	public final static class Header implements Serializable {

		public static final class OffsetSize implements Serializable {

			public final int offset;

			public final int size;

			public OffsetSize(int offset, int size) {

				this.offset = offset;
				this.size = size;
			}
		}

		public Header(final LEInputStream in) throws IOException {

			magicNumber = in.readInt();
			version = in.readInt();
			variableCount = in.readInt();
			totalSize = in.readInt();
			codes = new OffsetSize(in.readInt(), in.readInt());
			datas = new OffsetSize(in.readInt(), in.readInt());
			labels = new OffsetSize(in.readInt(), in.readInt());
			debugInfo = new OffsetSize(in.readInt(), in.readInt());
			libraries = new OffsetSize(in.readInt(), in.readInt());
			functions = new OffsetSize(in.readInt(), in.readInt());
			parameters = new OffsetSize(in.readInt(), in.readInt());

			// FUNC2 使用しない
			new OffsetSize(in.readInt(), in.readInt());

			// HPI 使用しない
			new OffsetSize(in.readInt(), in.readInt());
		}

		public final int magicNumber;

		public final int version;

		public final int variableCount;

		public final int totalSize;

		public final OffsetSize codes;

		public final OffsetSize datas;

		public final OffsetSize labels;

		public final OffsetSize debugInfo;

		public final OffsetSize libraries;

		public final OffsetSize functions;

		public final OffsetSize parameters;

	}

	/**
	 * @author Yuki
	 * 
	 * オブジェクトファイル中のコードセグメントの各エントリを表すクラス。
	 */
	public final static class Code implements Serializable {

		public static final int TYPE_BITS = 0x1FFF;

		public static final int NEWLINE_BIT = 0x2000;

		public static final int COMMA_BIT = 0x4000;

		public static final int LARGEDATA_BIT = 0x8000;

		public static final class Type {
			public static final int Mark = 0;

			public static final int Var = 1;

			public static final int String = 2;

			public static final int DNum = 3;

			public static final int INum = 4;

			public static final int Struct = 5;

			public static final int XLabel = 6;

			public static final int Label = 7;

			public static final int IntCmd = 8;

			public static final int ExtCmd = 9;

			public static final int ExtSysVar = 10;

			public static final int CmpCmd = 11;

			public static final int ModCmd = 12;

			public static final int IntFunc = 13;

			public static final int SysVar = 14;

			public static final int ProgCmd = 15;

			public static final int DllFunc = 16;

			public static final int DllCtrl = 17;

			public static final int UserDef = 18;

			public static final int JumpOffset = 19;
		}

		public final int offset;

		public final int type;

		public final boolean newLine;

		public final boolean comma;

		public final int value;

		public Code(final LEInputStream in) throws IOException {

			offset = in.getOffset();

			final int bits = in.readShort();

			type = bits & 0x1FFF;
			newLine = (bits & NEWLINE_BIT) != 0;
			comma = (bits & COMMA_BIT) != 0;

			value = (bits & LARGEDATA_BIT) == 0 ? in.readShort() : in.readInt();

		}

		public Code(final int type, final int value) {

			this.type = type;
			this.value = value;
			this.offset = 0;
			this.newLine = false;
			this.comma = false;
		}
	}

	/**
	 * @author Yuki
	 * 
	 * 拡張プラグインのエントリを表すクラス。
	 */
	public final static class Library implements Serializable {

		public static final int SIZE = 16;

		public Library(final LEInputStream in) throws IOException {

			flag = in.readInt();
			nameidx = in.readInt();
			hlib = in.readInt();
			clsid = in.readInt();
		}

		public final int flag; // initalize flag

		public final int nameidx; // function name index (DS)

		// Interface IID ( Com Object )
		public final int hlib; // Lib handle

		public final int clsid; // CLSID (DS) ( Com Object )

	}

	/**
	 * @author Yuki
	 * 
	 * 関数のエントリを表すクラス。
	 */
	public static final class Function implements Serializable {

		public static final int SIZE = 28;

		public Function(final LEInputStream in) throws IOException {

			index = (short) in.readShort();
			subid = (short) in.readShort();
			prmindex = in.readInt();
			prmmax = in.readInt();
			nameidx = in.readInt();
			size = in.readInt();
			otindex = in.readInt();
			funcflag = in.readInt();
		}

		public final short index; // base LIBDAT index

		public final short subid; // struct index

		public final int prmindex; // STRUCTPRM index(MINFO)

		public final int prmmax; // number of STRUCTPRM

		public final int nameidx; // name index (DS)

		public final int size; // struct size (stack)

		public final int otindex; // OT index(Module) / cleanup flag(Dll)

		public final int funcflag; // function flags(Module) or proc address

		public boolean isFunction() {

			return index == -2;
		}
	}

	public static final class Parameter implements Serializable {

		public static final int SIZE = 8;

		public Parameter(final LEInputStream in) throws IOException {

			mptype = (short) in.readShort();
			subid = in.readShort();
			offset = in.readInt();
		}

		public final short mptype; // Parameter type

		public final int subid; // struct index

		public final int offset; // offset from top
	}

}
