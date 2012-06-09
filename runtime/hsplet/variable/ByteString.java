/*
 * $Id: ByteString.java,v 1.7.2.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.variable;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

/**
 * �o�C�g�P�ʂŊǗ�����镶����N���X�B
 * <p>
 * HSP �̕����� MS932 �Ȃ̂ɑ΂��� Java �� UCS-2�A�܂� HSP �̕����񂪉ςɑ΂��āAJava �̕�����͕s�ςȂǁA
 * �����̏�ʂ� Java �̕���������̂܂܎g�p���邱�Ƃ��o���Ȃ��̂ŁA����ɂ��̃N���X���g�p����B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.7.2.1 $, $Date: 2006/08/02 12:13:06 $
 */
public class ByteString implements Serializable {

	/** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
	private static final long serialVersionUID = 6225913660341986054L;

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: ByteString.java,v 1.7.2.1 2006/08/02 12:13:06 Yuki Exp $";

	private static final Charset charset = Charset.forName("MS932");

	private static final CharsetDecoder decorder = charset.newDecoder();

	private byte[] bytes;

	private int offset;

	private int length;

	/**
	 * ���������񂩂�I�u�W�F�N�g���\�z����B
	 * @param text ���̃I�u�W�F�N�g�̏����l�ɂȂ镶����B
	 */
	public ByteString(final String text) {

		final ByteBuffer encoded = charset.encode(text);
		this.offset = 0;
		this.length = encoded.remaining();
		this.bytes = new byte[length + 1];
		encoded.get(bytes, 0, length);
	}

	/**
	 * �w�肳�ꂽ�o�C�g�z����g�p����I�u�W�F�N�g���\�z����B
	 * @param bytes MS932 �ŕ������ێ�����o�C�g�̔z��B
	 * @param offset ��������J�n����I�t�Z�b�g�B
	 * @param uniqueBuffer ���̃I�u�W�F�N�g�� bytes �Ƃ͕ʂ̃o�b�t�@���g�p���邩�ǂ����B
	 * false �̕��������ł����A�������ύX�\��̂Ƃ��� true ���w�肵�Ă��������B
	 */
	public ByteString(final byte[] bytes, final int offset, final boolean uniqueBuffer) {
		this(bytes, offset, bytes.length - offset, uniqueBuffer);
	}

	/**
	 * �w�肳�ꂽ�o�C�g�z����g�p����I�u�W�F�N�g���\�z����B
	 * <p>���ۂ̕�����̒����� length ���Z���Ƃ��́A���ۂ̒������D�悳���B
	 * </p>
	 * @param bytes MS932 �ŕ������ێ�����o�C�g�̔z��B
	 * @param offset ��������J�n����I�t�Z�b�g�B
	 * @param length ������̒����B
	 * @param uniqueBuffer ���̃I�u�W�F�N�g�� bytes �Ƃ͕ʂ̃o�b�t�@���g�p���邩�ǂ����B
	 * false �̕��������ł����A�������ύX�\��̂Ƃ��� true ���w�肵�Ă��������B
	 */
	public ByteString(final byte[] bytes, final int offset, final int length, final boolean uniqueBuffer) {

		this.length = Math.min(length, calculateLength(bytes, offset));

		if (uniqueBuffer) {

			this.bytes = new byte[this.length + 256];
			this.offset = 0;
			System.arraycopy(bytes, offset, this.bytes, this.offset, this.length);

		} else {

			this.bytes = bytes;
			this.offset = offset;
		}
	}

	/**
	 * �w�肳�ꂽ������Ɠ������e�̃I�u�W�F�N�g���\�z����B
	 * @param str ���������ƕ�����B
	 * @param uniqueBuffer ���̃I�u�W�F�N�g�� uniqueBuffer �Ƃ͕ʂ̃o�b�t�@���g�p���邩�ǂ����B
	 * false �̕��������ł����A�������ύX�\��̂Ƃ��� true ���w�肵�Ă��������B
	 */
	public ByteString(final ByteString str, final boolean uniqueBuffer) {
		this(str.bytes, str.offset, str.length, uniqueBuffer);
	}

	private static int calculateLength(final byte[] bytes, final int offset) {

		for (int i = offset; i < bytes.length; ++i) {
			if (bytes[i] == 0) {
				return i - offset;
			}
		}

		return bytes.length - offset;
	}

	public static ByteString concat(final ByteString lhs, final ByteString rhs) {

		final byte[] newBytes = new byte[lhs.length + rhs.length + 1];

		System.arraycopy(lhs.bytes, lhs.offset, newBytes, 0, lhs.length);
		System.arraycopy(rhs.bytes, rhs.offset, newBytes, lhs.length, rhs.length);

		return new ByteString(newBytes, 0, lhs.length + rhs.length, false);
	}

	public void append(final ByteString rhs) {
		replace(length, 0, rhs);
	}

	//@Override
	public String toString() {

		return decode(ByteBuffer.wrap(bytes, offset, length)).toString();
	}

	private CharBuffer decode(final ByteBuffer in) {

		// Java �̂���̓o�O���Ă���̂Ŏ��͂ł��

		int n = (int) (in.remaining() * decorder.averageCharsPerByte() + 1);
		CharBuffer out = CharBuffer.allocate(n);

		if (in.remaining() != 0) {
			decorder.reset();
			for (;;) {
				final CoderResult cr;
				if (in.hasRemaining()) {
					cr = decorder.decode(in, out, true);
				} else {
					cr = decorder.flush(out);
				}

				if (cr.isUnderflow()) {
					break;
				}
				if (cr.isOverflow()) {
					n *= 2;
					CharBuffer o = CharBuffer.allocate(n);
					out.flip();
					o.put(out);
					out = o;
					continue;
				}
				break;
			}
		}
		out.flip();
		return out;
	}

	//@Overwrite
	public boolean equals(Object obj) {

		if (obj instanceof ByteString) {

			final ByteString rhs = (ByteString) obj;

			if (rhs.length != length) {
				return false;
			}

			return compareTo(rhs) == 0;
		}

		return super.equals(obj);
	}

	public int compareTo(final ByteString rhs) {

		final int ret = compareSub(0, rhs);

		if (ret != 0) {
			return ret;
		}

		if ( rhs.length < length ) {
			return 1;
		}

		return 0;
	}

	public int compareSub(final int index, final ByteString rhs) {

		final byte[] lb = bytes;
		final int lo = offset + index;
		final byte[] rb = rhs.bytes;
		final int ro = rhs.offset;

		final int l = Math.min(length - index, rhs.length);

		for (int i = 0; i < l; ++i) {

			if (lb[lo + i] != rb[ro + i]) {
				return (lb[lo + i] > rb[ro + i] ? 1 : -1);
			}
		}

		// ���̕�����̕����Z��
		if (l < rhs.length) {
			return -1;
		}

		return 0;
	}

	/**
	 * length �̒l���擾����B
	 * 
	 * @return length �̒l�B
	 */
	public int length() {

		return this.length;
	}

	public byte get(int index) {

		try {
			return this.bytes[offset + index];
		} catch (Exception e) {
			return 0;
		}
	}

	public void set(int index, byte b) {

		bytes[offset + index] = b;
		if (index < length) {
			if (b == 0) {
				length = index;
			}
		} else if (index == length) {
			length = calculateLength(bytes, offset);
		}
	}

	public int indexOf(final ByteString sub, final int index) {

		for (int i = index; i + sub.length <= length; ++i) {

			if (compareSub(i, sub) == 0) {
				return i - index;
			}

		}

		return -1;
	}

	public ByteString substring(int index, int count) {

		if (index < 0) {
			count += index;
			index = 0;
		}
		if (index >= length) {
			index = length;
		}

		if (index + count >= length) {
			count = length - index;
		}

		final byte[] b = new byte[count + 1];
		System.arraycopy(bytes, offset + index, b, 0, count);
		return new ByteString(b, 0, count, false);
	}

	public void assign(final String string) {

		assign(new ByteString(string));

	}

	public void assign(final ByteString string) {

		replace(0, length, string);
	}

	public void replace(final int index, final int length, final ByteString string) {

		final int newLength = this.length + string.length - length;
		if (bytes.length < offset + newLength + 1) {

			// �o�b�t�@�̊g�����K�v

			final byte[] newBytes = new byte[newLength + 1024];

			System.arraycopy(bytes, offset, newBytes, 0, index);
			System.arraycopy(string.bytes, string.offset, newBytes, index, string.length);
			System.arraycopy(bytes, offset + index + length, newBytes, index + string.length, this.length
					- (index + length));

			bytes = newBytes;
			this.offset = 0;
			this.length = newLength;
		} else {

			System.arraycopy(bytes, offset + index + length, bytes, offset + index + string.length, this.length
					- (index + length));
			System.arraycopy(string.bytes, string.offset, bytes, offset + index, string.length);

			bytes[offset + newLength] = 0;
			this.length = newLength;
		}

	}

	public int lineIndex(final int line) {

		if (line < 0) {
			return length;
		} else {
			int index;
			int readedLine = 0;
			for (index = 0; index < length; ++index) {
				if (readedLine == line) {
					break;
				}
				if (get(index) == '\n') {
					++readedLine;
				}
			}
			return index;
		}
	}

	public int nextLineIndex(final int index) {

		int nextIndex;
		for (nextIndex = index; nextIndex < length; ++nextIndex) {
			if (get(nextIndex) == '\n') {
				++nextIndex;
				break;
			}
		}
		return nextIndex;
	}
        
        public int lineCount() {
            int lines = 0;
            for(int i = 0; i < length; i++) {
                if (get(i) == '\n') {
                    lines++;
                }
            }
            return lines;
        }
        
        public String getLine(int index) {
            
		int lineIndex = lineIndex(index);
		int lineLength = nextLineIndex(lineIndex) - lineIndex;

		final ByteString string = substring(lineIndex, lineLength);

		if (string.length() >= 1 && string.get(string.length() - 1) == '\n') {

			string.set(string.length() - 1, (byte) 0);
			if (string.length() >= 1 && string.get(string.length() - 1) == '\r') {
				string.set(string.length() - 1, (byte) 0);
			}
		}
		return string.toString();
        }
        
        public void dump(OutputStream out) throws IOException {
            out.write(bytes);
            out.flush();
        }
}
