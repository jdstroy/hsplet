/*
 * $Id: Browser.java,v 1.3 2006/03/26 14:35:36 Yuki Exp $
 */
package hsplet.gui;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;

/**
 * テスト実行時に使用するブラウザオブジェクト。
 * 
 * @author Yuki
 * @version $Revision: 1.3 $, $Date: 2006/03/26 14:35:36 $
 */
public class Browser extends JFrame {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: Browser.java,v 1.3 2006/03/26 14:35:36 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = 3252110042711048829L;

	public static final Map<String, Browser> namedInstance = new HashMap<String, Browser>();

	public static Browser getInstance(final String name) {
		return (Browser) namedInstance.get(name);
	}

	public static void showPage(final URL url, final String target) {
		
		try{
			
			Class.forName("edu.stanford.ejalbert.BrowserLauncher").getMethod("openURL", new Class[]{ String.class } ).invoke(null, new Object[]{ url.toString() } );
			
			return;
		} catch( Throwable e ){
			
		}

		final Browser named = getInstance(target);
		if (named != null) {
			named.showPage(url);
		} else {
			new Browser(target).showPage(url);
		}
	}

	private final String name;

	private JScrollPane scrollPane = null;

	private JEditorPane htmlPane = null;

	public Browser(final String name) {
		super();
		this.name = name;

		initialize();

		if (!name.equals("_blank")) {
			namedInstance.put(name, this);
		}

		pack();

		setVisible(true);
	}

	public void dispose() {
		namedInstance.remove(name);
	}

	public void showPage(final URL url) {
		try {
			htmlPane.setPage(url);
			setTitle(url.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setContentPane(getScrollPane());
	}

	/**
	 * This method initializes scrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setPreferredSize(new java.awt.Dimension(640, 480));
			scrollPane.setViewportView(getHtmlPane());
		}
		return scrollPane;
	}

	/**
	 * This method initializes htmlPane	
	 * 	
	 * @return javax.swing.JEditorPane	
	 */
	private JEditorPane getHtmlPane() {
		if (htmlPane == null) {
			htmlPane = new JEditorPane();
			htmlPane.setContentType("text/html");
			htmlPane.setEditable(false);
			htmlPane.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
				public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent e) {
					if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED)
						return;
					showPage(e.getURL());
				}
			});
		}
		return htmlPane;
	}

}
