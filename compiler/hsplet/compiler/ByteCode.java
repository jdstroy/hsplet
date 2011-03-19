/*
 * $Id: ByteCode.java,v 1.2 2006/01/13 20:32:11 Yuki Exp $
 */
package hsplet.compiler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * HSP �̒��ԃR�[�h(*.ax)�̃f�[�^������킷�f�[�^�N���X�B
 * 
 * ���ԃR�[�h�͑傫�������āA
 * <ul>
 * <li>�w�b�_</li>
 * <li>�R�[�h�Z�O�����g</li>
 * <li>�f�[�^�Z�O�����g</li>
 * <li>���x���I�t�Z�b�g</li>
 * <li>�f�o�b�O���</li>
 * <li>���C�u�������</li>
 * <li>�֐����</li>
 * <li>�֐��̃p�����[�^���</li>
 * <li>�Â��֐����(HSPLet �ł͎g�p���Ȃ�)</li>
 * <li>HPI���(HSPLet �ł͎g�p���Ȃ�)</li>
 * </ul>
 * ���琬�藧���Ă���B
 * 
 * <p>
 * ���̃N���X�͕s�ςȂ̂ŁA�R���X�g���N�^�Ƀo�C�g�f�[�^��n�����Ƃŏ���������B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/01/13 20:32:11 $
 */
public final class ByteCode implements Serializable {

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = 6171509589300649390L;

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: ByteCode.java,v 1.2 2006/01/13 20:32:11 Yuki Exp $";

	/** �w�b�_�Ɋi�[����Ă������B */
	public final Header header;

	/** �R�[�h�Z�O�����g�̓��e�B */
	public final Code[] codes;

	/** �f�[�^�Z�O�����g�̓��e�B */
	public final byte[] datas;

	/**
	 * ���x���̃I�t�Z�b�g�̔z��B
	 * <p>
	 * ���̒l�́A codes[?].offset �œ�����l�ɂȂ�B codes ���ł̃C���f�b�N�X�ł͂Ȃ��̂Œ��ӂ��邱�ƁB
	 * </p>
	 * 
	 */
	public final int[] labels;

	/** �f�o�b�O���B */
	public final int[] debugInfo;

	/** ���C�u�������B */
	public final Library[] libraries;

	/** �֐����B */
	public final Function[] functions;

	/** �֐��̃p�����[�^���B */
	public final Parameter[] parameters;

	/**
	 * ��������̃o�C�g�z�񂩂璆�ԃR�[�h��ǂݍ��ށB
	 * 
	 * @param bytes ���ԃR�[�h���i�[���ꂽ�z��B
	 * @throws IOException �ǂݎ��G���[�����������Ƃ��B
	 */
	public ByteCode(final byte[] bytes) throws IOException {

		this(new ByteArrayInputStream(bytes));
	}

	/**
	 * �^����ꂽ�X�g���[�����璆�ԃR�[�h��ǂݍ��ށB
	 * 
	 * @param stream ���ԃR�[�h���i�[���ꂽ�X�g���[���B
	 * @throws IOException �ǂݎ��G���[�����������Ƃ��B
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
	 * ���ɏ���ǂݎ���Ă���X�g���[���B
	 */
	private LEInputStream in;

	private Code[] readCodes() throws IOException {

		in.seekTo(header.codes.offset);

		final Code[] codes = new Code[header.codes.size / 2];

		int codeCount = 0;
		while (in.getOffset() < header.codes.offset + header.codes.size) {

			codes[codeCount++] = new Code(in);

			// if/else �̂Ƃ��͎���WORD���ړ��ʁi���� WORD ���j������킷�B
			if (codes[codeCount - 1].type == Code.Type.CmpCmd) {
				codes[codeCount++] = new Code(Code.Type.JumpOffset, in
						.readShort() * 2);
			}
		}

		codeCount -= 2; // �Ō�̓��(goto *lastLabel)�͂Ƃ肠��������Ȃ��B

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

			// ���x���̒l�̓R�[�h�Z�O�����g�̐擪����̑��΃A�h���X�ɂȂ��Ă���B
			// WORD �P�ʂȂ̂�2�{���Ă��K�v������B

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
	 * �I�u�W�F�N�g�t�@�C�����̃w�b�_��\���N���X�B
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

			// FUNC2 �g�p���Ȃ�
			new OffsetSize(in.readInt(), in.readInt());

			// HPI �g�p���Ȃ�
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
	 * �I�u�W�F�N�g�t�@�C�����̃R�[�h�Z�O�����g�̊e�G���g����\���N���X�B
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
	 * �g���v���O�C���̃G���g����\���N���X�B
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
	 * �֐��̃G���g����\���N���X�B
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
