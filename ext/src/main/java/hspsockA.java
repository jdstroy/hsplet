/*
 * $Id: hspsockA.java,v 1.4 2006/05/20 06:12:07 Yuki Exp $
 */
import hsplet.Context;
import hsplet.HSPError;
import hsplet.variable.Operand;
import hsplet.variable.Scalar;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * hspsockA 拡張ライブラリ。
 * @author Yuki
 * @version $Revision: 1.4 $, $Date: 2006/05/20 06:12:07 $
 */
public final class hspsockA extends hspsockBase {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: hspsockA.java,v 1.4 2006/05/20 06:12:07 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = 8450563058418420541L;

	public hspsockA(final Context context) {
		super(context);
	}

	public int sockinit() {
		return 0;
	}

	public int sockopen(final int id, final String host, final int port) {

		if (id < 0 || id >= sockets.length || sockets[id] != null) {
			context.error(HSPError.InvalidParameterValue, "sockopen", "id==" + id);
			return 1;
		}

		try {
			final InetSocketAddress address = new InetSocketAddress(InetAddress.getByName(host), port);

			final SocketChannel socket = SocketChannel.open();
			socket.configureBlocking(false);

			socket.connect(address);

			sockets[id] = new BufferedSocket(socket);

			return 0;
		} catch (Exception e) {
			return 1;
		}
	}

	public int sockmake(final int id, final int port) {
		return 1;
	}

	public int sockwait(final int id) {

		if (id < 0 || id >= sockets.length || sockets[id] == null) {
			context.error(HSPError.InvalidParameterValue, "sockwait", "id==" + id);
			return 1;
		}

		final BufferedSocket socket = sockets[id];
		try {
			socket.finishConnect();
		} catch (IOException e) {
			e.printStackTrace();
			return 2;
		}

		if (socket.isConnectionPending()) {
			return 0;
		} else if (socket.isConnected()) {
			return 1;
		} else {
			return 2;
		}
	}

	public int sockshut(final int id) {

		if (id < 0 || id >= sockets.length || sockets[id] == null) {
			context.error(HSPError.InvalidParameterValue, "sockshut", "id==" + id);
			return 1;
		}

		final BufferedSocket socket = sockets[id];

		try {
			socket.socket().shutdownOutput();

			return 0;
		} catch (Exception e) {
			return 1;
		}
	}

	public int sockget(final Operand buf, final int bufi, final int length, final int id) {

		if (id < 0 || id >= sockets.length || sockets[id] == null) {
			context.error(HSPError.InvalidParameterValue, "sockget", "id==" + id);
			return 0;
		}

		final int result = sockgetb(buf, bufi, 0, length == 0 ? 63 : length - 1, id);
		buf.poke(bufi, result, (byte) 0);
		return result;
	}

	public int sockgetw(final Operand buf, final int bufi, final int id) {

		if (id < 0 || id >= sockets.length || sockets[id] == null) {
			context.error(HSPError.InvalidParameterValue, "sockgetw", "id==" + id);
			return 1;
		}

		return sockgetBlock(buf, bufi, 0, 2, id) == 2 ? 0 : 1;
	}

	public int sockgetd(final Operand buf, final int bufi, final int id) {

		if (id < 0 || id >= sockets.length || sockets[id] == null) {
			context.error(HSPError.InvalidParameterValue, "sockgetd", "id==" + id);
			return 1;
		}

		return sockgetBlock(buf, bufi, 0, 4, id) == 4 ? 0 : 1;
	}

	public int sockputw(final Operand buf, final int bufi, final int id) {

		if (id < 0 || id >= sockets.length || sockets[id] == null) {
			context.error(HSPError.InvalidParameterValue, "sockputw", "id==" + id);
			return 1;
		}

		return sockputb(buf, bufi, 0, 2, id) == 2 ? 0 : 1;
	}

	public int sockputd(final Operand buf, final int bufi, final int id) {

		if (id < 0 || id >= sockets.length || sockets[id] == null) {
			context.error(HSPError.InvalidParameterValue, "sockputd", "id==" + id);
			return 1;
		}

		return sockputb(buf, bufi, 0, 4, id) == 4 ? 0 : 1;
	}

	public int sockcount(final Operand buf, final int bufi, final int id) {

		if (id < 0 || id >= sockets.length || sockets[id] == null) {
			context.error(HSPError.InvalidParameterValue, "sockcount", "id==" + id);
			return 0;
		}

		final BufferedSocket socket = sockets[id];

		try {
			buf.assign(bufi, Scalar.fromValue(socket.avail()), 0);
			return 0;
		} catch (IOException e) {
			return 1;
		}
	}

	public int sockerror(final Operand buf, final int bufi, final int id) {

		if (id < 0 || id >= sockets.length || sockets[id] == null) {
			context.error(HSPError.InvalidParameterValue, "sockerror", "id==" + id);
			return 0;
		}

		buf.assign(bufi, Scalar.fromValue(0), 0);

		return 0;
	}

}
