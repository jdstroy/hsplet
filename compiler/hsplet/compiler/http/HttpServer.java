package hsplet.compiler.http;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * �e�X�g���s�p�Ɏg�p����Ȉ� Web �T�[�o�[�B
 * 
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/05/09 11:57:31 $
 */
public class HttpServer implements Runnable {

	/**
	 * C: �h���C�u�̒��������J���� main.
	 * @param args ��������܂��B
	 */
	public static void main(String[] args) {

		HttpServer server = new HttpServer(6791);

		new Thread(server).start();
	}

	/** �҂��󂯃|�[�g. */
	private final int port;

	/**
	 * �T�[�o�[�I�u�W�F�N�g���\�z����.
	 * @param port �҂��󂯃|�[�g.
	 */
	public HttpServer(int port) {
		this.port = port;
	}

	/**
	 * ���z�p�X������ۂ̃t�@�C�����擾����.
	 * @param virtualPath ���z�p�X�iURI �̈ꕔ�j
	 * @return ���ۂ̃t�@�C��. null ���Ԃ邱�Ƃ�����B
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
	 * ���ۂ̃t�@�C�����牼�z�p�X���擾����.
	 * @param file �t�@�C���B
	 * @return ���z�p�X�B/ ����n�܂�B
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
	 * �҂��󂯂����s����.
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
