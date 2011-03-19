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
 * HTTP �̂ЂƂ̃Z�b�V�����̏������s���I�u�W�F�N�g.
 *
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/05/09 11:57:31 $
 */
public class HttpSession implements Runnable {

	/** ���s�R�[�h. */
	private static final String CRLF = "\r\n";

	/** �N���C�A���g�Ƃ̒ʐM�p�\�P�b�g. */
	private final Socket socket;

	/** �T�[�o�[�I�u�W�F�N�g. */
	private final HttpServer server;

	/**
	 * �Z�b�V�������쐬����.
	 * @param socket �ʐM�p�\�P�b�g.
	 * @param server �T�[�o�[�I�u�W�F�N�g.
	 */
	public HttpSession(Socket socket, HttpServer server) {
		this.socket = socket;
		this.server = server;
	}

	/**
	 * �ʐM�����s����.
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

			final String method = tokenizer.nextToken(); // ���\�b�h�͖����E�E�E�B
			final String uri = tokenizer.nextToken();

			// ��s�ڈȍ~�͖����E�E�E�B

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
	 * URI ����R���e���g�^�C�v�𓾂�.
	 * <p>
	 * TODO �e�[�u����p���Ă�葽���̌`���ɑΉ�������.
	 * </p>
	 * @param uri URI.
	 * @return �R���e���g�^�C�v.
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
	 * URI ����Ή�����t�@�C�����擾����.
	 * @param uri URI.
	 * @return �Ή�����t�@�C���B null ���Ԃ邱�Ƃ�����B
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
	 * �ʏ탂�[�h�Ń��X�|���X��Ԃ�.
	 * <code>uri</code>�ŗ^����ꂽURI���t�@�C�����ɕϊ����A
	 * ���̃t�@�C�����I�[�v�����ē��e��HTTP client�ɕԂ��B
	 * Content-type�̓t�@�C���̊g���q���画�f����B
	 * �t�@�C����������Ȃ������ꍇ�ɂ̓G���[��\���y�[�W�𐶐����ĕԂ��B
	 *
	 * @param uri �^����ꂽURI.
	 * @throws IOException ���o�̓G���[.
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
