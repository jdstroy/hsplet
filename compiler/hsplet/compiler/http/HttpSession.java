package hsplet.compiler.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * HTTP のひとつのセッションの処理を行うオブジェクト.
 *
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/05/09 11:57:31 $
 */
public class HttpSession implements Runnable {

	/** 改行コード. */
	private static final String CRLF = "\r\n";

	/** クライアントとの通信用ソケット. */
	private final Socket socket;

	/** サーバーオブジェクト. */
	private final HttpServer server;

	/**
	 * セッションを作成する.
	 * @param socket 通信用ソケット.
	 * @param server サーバーオブジェクト.
	 */
	public HttpSession(Socket socket, HttpServer server) {
		this.socket = socket;
		this.server = server;
	}

	/**
	 * 通信を実行する.
	 */
	public void run() {
		try {

			Set addressSet = new HashSet();
			addressSet.addAll(Arrays.asList(InetAddress.getAllByName(null)));
			addressSet.add(InetAddress.getLocalHost());

			if (!addressSet.contains(((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress())) {
				System.out.println("Reject request from " + socket.getRemoteSocketAddress());
				socket.close();
				return;
			}

			final BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream()), 1024);

			final String request = r.readLine();

			final StringTokenizer tokenizer = new StringTokenizer(request, " ");

			final String method = tokenizer.nextToken(); // メソッドは無視・・・。
			final String uri = tokenizer.nextToken();

			// 二行目以降は無視・・・。

			sendContent(uri);

		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				socket.close();
			} catch (IOException ignored) {
			}
		}
	}

	/**
	 * URI からコンテントタイプを得る.
	 * <p>
	 * TODO テーブルを用いてより多くの形式に対応したい.
	 * </p>
	 * @param uri URI.
	 * @return コンテントタイプ.
	 */
	private String getContentType(String uri) {
		if (uri.endsWith(".htm") || uri.endsWith(".html")) {
			return "text/html";
		} else if (uri.endsWith(".gif")) {
			return "image/gif";
		} else if (uri.endsWith(".txt")) {
			return "text/plain";
		} else {
			// Unknown case
			return "application/octet-stream";
		}
	}

	/**
	 * URI から対応するファイルを取得する.
	 * @param uri URI.
	 * @return 対応するファイル。 null が返ることもある。
	 */
	private File getFile(String uri) {

		File file = server.mapPath(uri);
		if (file != null && file.isDirectory()) {
			if (new File(file, "index.htm").isFile()) {
				file = new File(file, "index.htm");
			} else if (new File(file, "index.html").isFile()) {
				file = new File(file, "index.html");
			}
		}

		return file;
	}

	/**
	 * 通常モードでレスポンスを返す.
	 * <code>uri</code>で与えられたURIをファイル名に変換し、
	 * そのファイルをオープンして内容をHTTP clientに返す。
	 * Content-typeはファイルの拡張子から判断する。
	 * ファイルが見つからなかった場合にはエラーを表すページを生成して返す。
	 *
	 * @param uri 与えられたURI.
	 * @throws IOException 入出力エラー.
	 */
	private void sendContent(String uri) throws IOException {

		File file = getFile(uri);

		OutputStream o = socket.getOutputStream();
		OutputStreamWriter w = new OutputStreamWriter(o, "UTF-8");

		if (file != null && file.isFile()) {
			String contentType = getContentType(uri);

			w.write("HTTP/1.0 200 OK" + CRLF);
			w.write("Content-type: " + contentType + CRLF);
			w.write("Pragma: no-cache" + CRLF);
			w.write("Cache-Control: no-cache" + CRLF);
			w.write("Expires: Thu, 01 Dec 1994 16:00:00 GMT" + CRLF);
			w.write(CRLF);
			w.flush();

			InputStream in = new FileInputStream(file);
			byte[] buf = new byte[1024];

			for (;;) {
				int l = in.read(buf);
				if (l < 0) {
					break;
				}
				o.write(buf, 0, l);
			}
		} else {
			w.write("HTTP/1.0 404 Not found" + CRLF);
			w.write("Content-type: text/html" + CRLF);
			w.write("Pragma: no-cache" + CRLF);
			w.write("Cache-Control: no-cache" + CRLF);
			w.write("Expires: Thu, 01 Dec 1994 16:00:00 GMT" + CRLF);
			w.write(CRLF);
			w.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
			w.write("<html><head><title>404 Not Found</title></head><body>");
			w.write("<h1>404 Not found</h1>");
			w.write("<hr>");
			w.write("<table summary='Query Information'><tr><th>URI:</th><td>" + uri + "</td></tr>");
			w.write("<tr><th>File:</th><td>" + file + "</td></tr></table>");
			w.write("</p></body></html>");
			w.flush();
		}
		o.close();
	}
}
