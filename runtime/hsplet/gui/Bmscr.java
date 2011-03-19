/*
 * $Id: Bmscr.java,v 1.2 2006/01/29 16:29:17 Yuki Exp $
 */
package hsplet.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ウィンドウの状態を管理するクラス。
 * <p>
 * color/gmode 等で設定された値はすべてこのクラスで管理する。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/01/29 16:29:17 $
 */
public class Bmscr implements Serializable {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: Bmscr.java,v 1.2 2006/01/29 16:29:17 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = 7942441592976959984L;

	/**
	 * オブジェクトを構築する。
	 * <p>サイズはウィンドウサイズを使用する。</p>
	 * @param screen ウィンドウ。
	 * @param contentPane コンテントペイン。
	 */
	public Bmscr(final HSPScreen screen, final ContentPane contentPane) {

		this(screen, contentPane, contentPane.getSize());

	}

	/**
	 * オブジェクトを構築する。
	 * @param screen ウィンドウ。
	 * @param contentPane コンテントペイン。
	 * @param bufferSize バッファサイズ。
	 */
	public Bmscr(final HSPScreen screen, final ContentPane contentPane, final Dimension bufferSize) {

		this.screen = screen;
		component = screen.asComponent();
		contents = contentPane;
		window = component instanceof Window ? (Window) component : null;

		init(Color.white, bufferSize);
	}

	public final HSPScreen screen;

	public final Component component;

	public final ContentPane contents;

	public final Window window;

	public final List controls = new ArrayList();

	public void init(final Color backColor) {

		init(backColor, new Dimension(backImage.getWidth(), backImage.getHeight()));
	}

	public void init(final Color backColor, final Dimension bufferSize) {

		if (contents != null) {
			contents.removeAll();
		}
		controls.clear();

		if (contents != null) {
			contents.setSize(bufferSize);
		}

		backImage = new BufferedImage(Math.max(1, bufferSize.width), Math.max(1, bufferSize.height),
				BufferedImage.TYPE_3BYTE_BGR);
		backGraphics = backImage.createGraphics();

		backGraphics.setColor(backColor);
		backGraphics.fillRect(0, 0, backImage.getWidth(), backImage.getHeight());

		cx = 0;
		cy = 0;
		font = new Font("Monospaced", 0, 18);
		fontStyle = 0;
		color = Color.black;
		gmode = 0;
		gwidth = 0;
		gheight = 0;
		galpha = 0;
		redraw = 1;
		transColor = Color.black;
		owidth = 64;
		oheight = 24;
		mindy = 0;
		mesw = 0;
		mesh = 0;
		objfont = null;
		originx = 0;
		originy = 0;

		backGraphics.setFont(font);
		backGraphics.setColor(color);

	}

	public BufferedImage backImage;

	public Graphics2D backGraphics;

	public int cx;

	public int cy;

	public Font font;

	public int fontStyle;

	public Color color;

	public int gmode;

	public int gwidth;

	public int gheight;

	public int galpha;

	public int redraw;

	public Color transColor;

	public int owidth;

	public int oheight;

	public int mindy;

	public int mesw;

	public int mesh;

	public Font objfont;

	public final Object repaintLock = new Object();

	public int originx;

	public int originy;

	public void redraw(final int x, final int y, final int width, final int height) {

		if (contents != null) {
			if (redraw != 0) {
				try {
					synchronized (repaintLock) {
						contents.repaint(x, y, width, height);
						repaintLock.wait(10);
					}
				} catch (InterruptedException e) {
				}
			}
		}

	}
}
