/*
 * $Id: hspsockBase.java,v 1.4 2006/05/20 06:12:07 Yuki Exp $
 */
import hsplet.Context;
import hsplet.HSPError;
import hsplet.function.FunctionBase;
import hsplet.variable.Operand;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * hspsock、hspsockA はほとんど共通なので、その共通部分。
 * @author Yuki
 * @version $Revision: 1.4 $, $Date: 2006/05/20 06:12:07 $
 */
public abstract class hspsockBase extends FunctionBase {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: hspsockBase.java,v 1.4 2006/05/20 06:12:07 Yuki Exp $";

	protected static class BufferedSocket {

		private final SocketChannel socket;

		private byte[] readBuffer = new byte[1024 * 64];

		private int readOffset = 0;

		private int readLength = 0;

		public BufferedSocket(final SocketChannel socket) {
			this.socket = socket;
		}

		public void finishConnect() throws IOException {
			socket.finishConnect();

		}

		public boolean isConnectionPending() {
			return socket.isConnectionPending();
		}

		public boolean isConnected() {
			return socket.isConnected();
		}

		public Socket socket() {
			return socket.socket();
		}

		public void close() throws IOException {
			socket.close();

		}

		public int read(ByteBuffer buffer) throws IOException {

			readBuffer();

			if (readLength < 0) {
				return -1;
			}

			int i = 0;
			for (; i < buffer.capacity(); ++i) {

				if (readOffset + i >= readLength) {
					break;
				}

				buffer.put(i, readBuffer[readOffset + i]);

			}

			readOffset += i;

			if (readOffset == readLength) {
				readOffset = 0;
				readLength = 0;
			} else if (readOffset >= readBuffer.length / 2) {
				System.arraycopy(readBuffer, readOffset, readBuffer, 0, readLength - readOffset);

				readLength -= readOffset;
				readOffset = 0;

			}

			return i;
		}

		private void readBuffer() throws IOException {
			if (readLength >= 0 && readLength < readBuffer.length) {

				final int l = socket.read(ByteBuffer.wrap(readBuffer, readLength, readBuffer.length - readLength));

				if (l < 0) {
					if (readLength == 0) {
						readLength = -1;
					}
				} else {
					readLength += l;
				}
			}

		}

		public int avail() throws IOException {
			readBuffer();
			return readLength - readOffset;
		}

		public int write(ByteBuffer buffer) throws IOException {
			return socket.write(buffer);
		}

		public boolean isClosed() {
			return readLength < 0;
		}
	}

	public hspsockBase(final Context context) {
		this.context = context;
	}

	protected BufferedSocket[] sockets = new BufferedSocket[32];

	protected final Context context;

	public void ipget() {
		try {
			context.refstr.value.assign(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			context.refstr.value.assign("");
		}
	}

	public void sockbye() {
		for (int i = 0; i < sockets.length; ++i) {
			if (sockets[i] != null) {
				try {
					sockets[i].close();
				} catch (IOException e) {
				}
				sockets[i] = null;
			}
		}
	}

	public int sockclose(final int id) {

		if (id < 0 || id >= sockets.length || sockets[id] == null) {
			context.error(HSPError.InvalidParameterValue, "sockclose", "id==" + id);
			return 1;
		}

		final BufferedSocket socket = sockets[id];

		try {
			socket.close();

			sockets[id] = null;

			return 0;
		} catch (Exception e) {
			return 1;
		}
	}

	public int sockput(final Operand buf, final int bufi, final int id) {

		if (id < 0 || id >= sockets.length || sockets[id] == null) {
			context.error(HSPError.InvalidParameterValue, "sockput", "id==" + id);
			return 1;
		}

		final int length = buf.toByteString(bufi).length();

		return sockputb(buf, bufi, 0, length, id) == length ? 0 : 1;
	}

	public int sockputc(final Operand buf, final int bufi, final int id) {

		if (id < 0 || id >= sockets.length || sockets[id] == null) {
			context.error(HSPError.InvalidParameterValue, "sockputc", "id==" + id);
			return 1;
		}

		return sockputb(buf, bufi, 0, 1, id) == 1 ? 0 : 1;

	}


	public int sockputb(final Operand buf, final int bufi, final int offset2, final int length, final int id) {

		if (id < 0 || id >= sockets.length || sockets[id] == null) {
			context.error(HSPError.InvalidParameterValue, "sockputb", "id==" + id);
			return 0;
		}

		final BufferedSocket socket = sockets[id];

		try {
			final byte[] data = new byte[length];

			for (int i = 0; i < length; ++i) {

				data[i] = buf.peek(bufi, offset2 + i);
			}

			int writeLength = 0;
			while (writeLength < length) {

				int result = socket.write(ByteBuffer.wrap(data, writeLength, length - writeLength));
				if (result < 0) {
					break;
				}

				writeLength += result;
			}

			return writeLength;

		} catch (Exception e) {
			return 0;
		}
	}

	public int sockgetc(final Operand buf, final int bufi, final int id) {

		if (id < 0 || id >= sockets.length || sockets[id] == null) {
			context.error(HSPError.InvalidParameterValue, "sockgetc", "id==" + id);
			return 1;
		}

		return sockgetBlock(buf, bufi, 0, 1, id) == 1 ? 0 : 1;

	}

	public int sockgetb(final Operand buf, final int bufi, final int offset, final int length, final int id) {

		if (id < 0 || id >= sockets.length || sockets[id] == null) {
			context.error(HSPError.InvalidParameterValue, "sockgetb", "id==" + id);
			return 0;
		}

		final BufferedSocket socket = sockets[id];

		try {
			final byte[] data = new byte[length];

			int readLength = 0;
			while (readLength < length) {

				int result = socket.read(ByteBuffer.wrap(data, readLength, length - readLength));
				if ( result==0 && readLength!=0 ) {
					break;
				}
				if (result < 0) {
					break;
				}

				readLength += result;
			}

			for (int i = 0; i < length; ++i) {
				buf.poke(bufi, offset + i, data[i]);
			}

			return readLength;

		} catch (Exception e) {
			return 0;
		}
	}


	public int sockgetBlock(final Operand buf, final int bufi, final int offset, final int length, final int id) {

		if (id < 0 || id >= sockets.length || sockets[id] == null) {
			context.error(HSPError.InvalidParameterValue, "sockgetb", "id==" + id);
			return 0;
		}

		final BufferedSocket socket = sockets[id];

		try {
			final byte[] data = new byte[length];

			int readLength = 0;
			while (readLength < length) {

				int result = socket.read(ByteBuffer.wrap(data, readLength, length - readLength));
				if (result < 0) {
					break;
				}

				readLength += result;
				
				if ( result==0 ) {
					Thread.sleep(10);
				}
			}

			for (int i = 0; i < length; ++i) {
				buf.poke(bufi, offset + i, data[i]);
			}

			return readLength;

		} catch (Exception e) {
			return 0;
		}
	}
	
	public int sockcheck(final int id) {

		if (id < 0 || id >= sockets.length || sockets[id] == null) {
			context.error(HSPError.InvalidParameterValue, "sockcheck", "id==" + id);
			return 3;
		}

		final BufferedSocket socket = sockets[id];

		try {
			if (!socket.isConnected()) {
				return 3;
			} else if (socket.avail() > 0) {
				return 0;
			} else if (socket.avail() == 0) {
				return 1;
			} else {
				return 2;
			}
		} catch (IOException e) {
			return 3;
		}

	}
}
