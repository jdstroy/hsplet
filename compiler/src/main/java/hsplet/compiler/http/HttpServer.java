package hsplet.compiler.http;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * テスト実行用に使用する簡易 Web サーバー。
 * 
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/05/09 11:57:31 $
 */
public class HttpServer implements Runnable {

	/**
	 * C: ドライブの直下を公開する main.
	 * @param args 無視されます。
	 */
	public static void main(String[] args) {

		HttpServer server = new HttpServer(6791);

		new Thread(server).start();
	}

	/** 待ち受けポート. */
	private final int port;

	/**
	 * サーバーオブジェクトを構築する.
	 * @param port 待ち受けポート.
	 */
	public HttpServer(int port) {
		this.port = port;
	}

	/**
	 * 仮想パスから実際のファイルを取得する.
	 * @param virtualPath 仮想パス（URI の一部）
	 * @return 実際のファイル. null が返ることもある。
	 */
	public static File mapPath(String virtualPath) {

		virtualPath = virtualPath.substring(virtualPath.indexOf('/', 1)+1);

		try {
			return new File(URLDecoder.decode(virtualPath, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * 実際のファイルから仮想パスを取得する.
	 * @param file ファイル。
	 * @return 仮想パス。/ から始まる。
	 */
	public static String mapURL(File file) {

		try {
			return "/" + Math.random() + "/"
					+ URLEncoder.encode(file.getAbsolutePath().replace('\\','/'), "UTF-8").replaceAll("%2F", "/");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * 待ち受けを実行する.
	 */
	public void run() {

		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);

			for (;;) {
				try {
					new Thread(new HttpSession(serverSocket.accept(), this)).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException ignored) {
				}
			}
		}
	}
}
