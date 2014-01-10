/*
 * $Id: hspsock.java,v 1.4 2006/05/20 06:12:07 Yuki Exp $
 */
import hsplet.Context;
import hsplet.HSPError;
import hsplet.variable.Operand;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

/**
 * hspsock 拡張ライブラリ。
 * @author Yuki
 * @version $Revision: 1.4 $, $Date: 2006/05/20 06:12:07 $
 */
public final class hspsock extends hspsockBase {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: hspsock.java,v 1.4 2006/05/20 06:12:07 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = -5493703364882298218L;

	public hspsock(final Context context) {
		super(context);
	}

	public int sockopen(final int id, final String host, final int port) {

		if (id < 0 || id >= sockets.length || sockets[id] != null) {
			context.error(HSPError.InvalidParameterValue, "sockopen", "id==" + id);
			return 1;
		}

		final InetSocketAddress address;
		try {
			address = new InetSocketAddress(InetAddress.getByName(host), port);
		} catch (UnknownHostException e) {
			return 3;
		}

		final SocketChannel socket;
		try {
			socket = SocketChannel.open();
			socket.configureBlocking(false);
		} catch (IOException e) {
			return 2;
		}

		try {
			socket.connect(address);
			socket.finishConnect();
		} catch (IOException e) {
			return 3;
		}

		sockets[id] = new BufferedSocket(socket);

		return 0;
	}

	public int sockmake(final int id, final int port) {
		return 1;
	}

	public int sockwait(final int id) {

		return 2;
	}

	public int sockget(final Operand buf, final int bufi, final int length, final int id) {

		if (id < 0 || id >= sockets.length || sockets[id] == null) {
			context.error(HSPError.InvalidParameterValue, "sockget", "id==" + id);
			return 0;
		}

		final int result = sockgetb(buf, bufi, 0, length == 0 ? 63 : length - 1, id);
		buf.poke(bufi, result, (byte) 0);
		return result == 0 && length != -1 && !sockets[id].isClosed() ? 1 : 0;
	}

}
