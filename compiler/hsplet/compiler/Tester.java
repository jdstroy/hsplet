package hsplet.compiler;

import hsplet.compiler.http.HttpServer;

import java.io.File;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingExecutionException;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;

public class Tester {

	/**
	 * テストを実行する。
	 * 
	 * @param args
	 *            実行時の引数。
	 *            <ol>
	 *            <li>--port=生成するJARファイル名</li>
	 *            <li>--html=表示するHTMLファイル名</li>
	 *            <li>--hidden=ウィンドウを表示しない</li>
	 *            </ol>
	 * @throws BrowserLaunchingInitializingException 
	 * @throws BrowserLaunchingExecutionException 
	 * @throws UnsupportedOperatingSystemException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws UnsupportedOperatingSystemException,
			BrowserLaunchingExecutionException, BrowserLaunchingInitializingException, InterruptedException {

		int port = 5262;
		File htmlFile = null;
		boolean hidden = false;

		for (int i = 0; i < args.length; ++i) {

			final String arg = args[i];
			if (arg.startsWith("--port=")) {
				port = Integer.parseInt(arg.substring("--port=".length()));
			} else if (arg.startsWith("--html=")) {
				htmlFile = new File(arg.substring("--html=".length()));
			} else if (arg.startsWith("--hidden=")) {
				hidden = Boolean.valueOf(arg.substring("--hidden=".length())).booleanValue();
			}
		}

		if (htmlFile == null) {
			throw new RuntimeException("HTML ファイル名が指定されていません。");
		}

		test(port, htmlFile, hidden);
	}

	private static void test(int port, File htmlFile, boolean hidden) throws UnsupportedOperatingSystemException,
			BrowserLaunchingExecutionException, BrowserLaunchingInitializingException, InterruptedException {

		Thread t = new Thread(new HttpServer(port));

		t.start();

		Thread.sleep(100);

		BrowserLauncher.openURL("http://localhost:" + port + HttpServer.mapURL(htmlFile));

		if (t.isAlive()) {
			if (!hidden) {
				new TesterWindow().setVisible(true);
			}
		}
	}
}
