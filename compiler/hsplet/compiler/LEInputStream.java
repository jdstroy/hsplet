/*
 * $Id: LEInputStream.java,v 1.3 2006/01/13 20:32:11 Yuki Exp $
 */
package hsplet.compiler;

import java.io.IOException;
import java.io.InputStream;

/**
 * 包含された InputStream をリトルエンディアンとみなして多バイトの情報を一度に取り出すためのクラス。
 * <p>
 * HSP の中間コードは WORD/DWORD がデータの基本単位となっているので、Java の InputStream は利用しにくい。 このクラスでラップをかぶせ、容易にデータを取り出せるようにする。
 * </p>
 * 
 * @note Java のライブラリに同等のクラスがありそうなので、もしあれば置き換える。
 * @author Yuki
 * @version $Revision: 1.3 $, $Date: 2006/01/13 20:32:11 $
 */
public final class LEInputStream extends InputStream {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: LEInputStream.java,v 1.3 2006/01/13 20:32:11 Yuki Exp $";

	/** このオブジェクトが包含する InputStream */
	private final InputStream inner;

	/** 現在の読み取り済みオフセット */
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
