/*
 * $Id: LEInputStream.java,v 1.3 2006/01/13 20:32:11 Yuki Exp $
 */
package hsplet.compiler;

import java.io.IOException;
import java.io.InputStream;

/**
 * ��܂��ꂽ InputStream �����g���G���f�B�A���Ƃ݂Ȃ��đ��o�C�g�̏�����x�Ɏ��o�����߂̃N���X�B
 * <p>
 * HSP �̒��ԃR�[�h�� WORD/DWORD ���f�[�^�̊�{�P�ʂƂȂ��Ă���̂ŁAJava �� InputStream �͗��p���ɂ����B ���̃N���X�Ń��b�v�����Ԃ��A�e�ՂɃf�[�^�����o����悤�ɂ���B
 * </p>
 * 
 * @note Java �̃��C�u�����ɓ����̃N���X�����肻���Ȃ̂ŁA��������Βu��������B
 * @author Yuki
 * @version $Revision: 1.3 $, $Date: 2006/01/13 20:32:11 $
 */
public final class LEInputStream extends InputStream {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	private static final String fileVersionID = "$Id: LEInputStream.java,v 1.3 2006/01/13 20:32:11 Yuki Exp $";

	/** ���̃I�u�W�F�N�g����܂��� InputStream */
	private final InputStream inner;

	/** ���݂̓ǂݎ��ς݃I�t�Z�b�g */
	private int offset;

	public LEInputStream(final InputStream inner) {

		this.inner = inner;
		this.offset = 0;
	}

	public int getOffset() {

		return offset;
	}

	public int readInt() throws IOException {

		final byte[] bits = new byte[4];

		readBytes(bits);

		return bits[0] & 0xFF | bits[1] << 8 & 0x0000FF00 | bits[2] << 16
				& 0x00FF0000 | bits[3] << 24 & 0xFF000000;
	}

	public int readShort() throws IOException {

		final byte[] bits = new byte[2];

		readBytes(bits);

		return bits[0] & 0xFF | bits[1] << 8 & 0x0000FF00;
	}

	public void readBytes(final byte[] bytes) throws IOException {

		int totalReadSize = 0;

		while (totalReadSize < bytes.length) {

			final int readSize = read(bytes, totalReadSize, bytes.length
					- totalReadSize);

			if (readSize < 0) {
				throw new IOException("Unexpected EOF");
			}

			totalReadSize += readSize;
		}

		this.offset += bytes.length;
	}

	public void seekTo(final int offset) throws IOException {

		if (offset < this.offset)
			throw new IOException("Cannot seek back.");

		this.offset += skip(offset - this.offset);

		if (this.offset < offset) {
			throw new IOException("Unexpected EOF");
		}
	}

	//@Override
	public int read() throws IOException {

		return inner.read();
	}
}
