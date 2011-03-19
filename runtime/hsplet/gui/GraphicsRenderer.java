/*
 * $Id: GraphicsRenderer.java,v 1.6 2006/01/29 16:29:17 Yuki Exp $
 */
package hsplet.gui;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * 高度な(？)画像描画をサポートするクラス。
 * <p>
 * gcopy/gsquare 等を実装する。 現在 TYPE_3BYTE_BGR の BufferedImage しかサポートしない。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.6 $, $Date: 2006/01/29 16:29:17 $
 */
public class GraphicsRenderer {

	private static int[] srcPixels = new int[1024 * 32];

	private static int[] destPixels = new int[1024 * 32];

	/**
	 * 画像をコピーする。
	 * @param win コピー先画面。
	 * @param dx コピー先X。
	 * @param dy コピー先Y。
	 * @param src コピー元画像。
	 * @param sx コピー元X。
	 * @param sy コピー元Y。
	 * @param w コピー幅。
	 * @param h コピー高さ。
	 */
	public static synchronized void gcopy(final Bmscr win, int dx, int dy, final BufferedImage src, int sx, int sy,
			int w, int h) {

		final BufferedImage dest = win.backImage;
		final Graphics2D destG = win.backGraphics;

		if (dest.getType() != BufferedImage.TYPE_3BYTE_BGR) {
			throw new IllegalArgumentException("ビットタイプ " + dest.getType() + " はサポートされません。");
		}
		if (src.getType() != BufferedImage.TYPE_3BYTE_BGR) {
			throw new IllegalArgumentException("ビットタイプ " + src.getType() + " はサポートされません。");
		}

		if (dx < 0) {
			sx += -dx;
			w -= -dx;
			dx += -dx;
		}
		if (sx < 0) {
			dx += -sx;
			w -= -sx;
			sx += -sx;
		}

		if (dx + w > dest.getWidth()) {
			w = dest.getWidth() - dx;
		}
		if (sx + w > src.getWidth()) {
			w = src.getWidth() - sx;
		}

		if (dy < 0) {
			sy += -dy;
			h -= -dy;
			dy += -dy;
		}
		if (sy < 0) {
			dy += -sy;
			h -= -sy;
			sy += -sy;
		}

		if (dy + h > dest.getHeight()) {
			h = dest.getHeight() - dy;
		}
		if (sy + h > src.getHeight()) {
			h = src.getHeight() - sy;
		}

		if (w <= 0 || h <= 0) {
			return;
		}

		if (win.gmode < 2) {
			destG.drawImage(src, dx, dy, dx + w, dy + h, sx, sy, sx + w, sy + h, null);
		} else if (win.gmode == 3) {

			final Composite prev = win.backGraphics.getComposite();
			try {
				destG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, win.galpha / 256.0F));

				destG.drawImage(src, dx, dy, dx + w, dy + h, sx, sy, sx + w, sy + h, null);
			} finally {
				destG.setComposite(prev);
			}
		} else {

			final WritableRaster dr = dest.getRaster();
			final Raster sr = src.getRaster();

			for (int i = 0; i < h; ++i) {
				sr.getPixels(sx, sy + i, w, 1, srcPixels);
				dr.getPixels(dx, dy + i, w, 1, destPixels);

				gcopy_line(win, destPixels, srcPixels, w);

				dr.setPixels(dx, dy + i, w, 1, destPixels);
			}
		}
	}

	private static void gcopy_line(final Bmscr win, final int[] destPixels, final int[] srcPixels, final int w) {

		final int byteCount = w * 3;

		switch (win.gmode) {
		case 0:
		case 1:
			for (int i = 0; i < byteCount; ++i) {

				destPixels[i] = srcPixels[i];
			}
			break;
		case 2: {
			for (int i = 0; i < byteCount; i += 3) {

				if (srcPixels[i] != 0 || srcPixels[i + 1] != 0 || srcPixels[i + 2] != 0) {
					destPixels[i] = srcPixels[i];
					destPixels[i + 1] = srcPixels[i + 1];
					destPixels[i + 2] = srcPixels[i + 2];
				}
			}
		}
			break;
		case 3: {
			final int srcA = win.galpha;
			if (srcA == 256) {
				for (int i = 0; i < byteCount; ++i) {

					destPixels[i] = srcPixels[i];
				}
			} else if (srcA != 0) {
				for (int i = 0; i < byteCount; ++i) {
					destPixels[i] += (srcPixels[i] - destPixels[i]) * srcA >>> 8;
				}
			}
		}
			break;
		case 4: {
			final int srcA = win.galpha;

			final int transR = win.transColor.getRed();
			final int transG = win.transColor.getGreen();
			final int transB = win.transColor.getBlue();

			if (srcA == 256) {
				for (int i = 0; i < byteCount; i += 3) {

					if (srcPixels[i] != transR || srcPixels[i + 1] != transG || srcPixels[i + 2] != transB) {
						destPixels[i] = srcPixels[i];
						destPixels[i + 1] = srcPixels[i + 1];
						destPixels[i + 2] = srcPixels[i + 2];
					}
				}
			} else if (srcA != 0) {
				for (int i = 0; i < byteCount; i += 3) {

					if (srcPixels[i] != transR || srcPixels[i + 1] != transG || srcPixels[i + 2] != transB) {
						destPixels[i] += (srcPixels[i] - destPixels[i]) * srcA >>> 8;
						destPixels[i + 1] += (srcPixels[i + 1] - destPixels[i + 1]) * srcA >>> 8;
						destPixels[i + 2] += (srcPixels[i + 2] - destPixels[i + 2]) * srcA >>> 8;
					}
				}
			}
		}
			break;
		case 5: {
			final int srcA = win.galpha;

			if (srcA == 256) {
				for (int i = 0; i < byteCount; ++i) {

					destPixels[i] += srcPixels[i];
					if (destPixels[i] > 255) {
						destPixels[i] = 255;
					}
				}
			} else if (srcA != 0) {
				for (int i = 0; i < byteCount; ++i) {

					destPixels[i] += srcPixels[i] * srcA >>> 8;
					if (destPixels[i] > 255) {
						destPixels[i] = 255;
					}

				}
			}
		}
			break;
		case 6: {
			final int srcA = win.galpha;

			if (srcA == 256) {
				for (int i = 0; i < byteCount; ++i) {

					destPixels[i] -= srcPixels[i];
					if (destPixels[i] < 0) {
						destPixels[i] = 0;
					}
				}
			} else if (srcA != 0) {
				for (int i = 0; i < byteCount; ++i) {

					destPixels[i] -= srcPixels[i] * srcA >>> 8;
					if (destPixels[i] < 0) {
						destPixels[i] = 0;
					}
				}
			}
		}
			break;
		// TODO gcopy mode 7 ピクセルアルファブレンド
		}
	}

	/**
	 * 画像を変形コピーする。
	 * @param win コピー先画面。
	 * @param dx コピー先X。
	 * @param dy コピー元X。
	 * @param src コピー元画像。
	 * @param sx コピー元X。
	 * @param sy コピー元Y。
	 */
	public static synchronized void gsquare(final Bmscr win, final int[] dx, final int[] dy, final BufferedImage src,
			final int[] sx, final int[] sy) {

		final BufferedImage dest = win.backImage;

		if (dest.getType() != BufferedImage.TYPE_3BYTE_BGR) {
			throw new IllegalArgumentException("ビットタイプ " + dest.getType() + " はサポートされません。");
		}
		if (src.getType() != BufferedImage.TYPE_3BYTE_BGR) {
			throw new IllegalArgumentException("ビットタイプ " + src.getType() + " はサポートされません。");
		}

		// 描画先の Y 座標の範囲を求める。
		int minY = dy[0];
		int maxY = dy[0];
		for (int i = 1; i < 4; ++i) {
			if (dy[i] < minY) {
				minY = dy[i];
			}
			if (dy[i] > maxY) {
				maxY = dy[i];
			}
		}
		if (minY < 0) {
			minY = 0;
		}
		if (maxY > dest.getHeight()) {
			maxY = dest.getHeight();
		}

		// minY <= y < maxY について、一行ずつコピー
		final int[] dxs = new int[2];
		final int[] dys = new int[2];
		final float[] sxs = new float[2];
		final float[] sys = new float[2];

		for (int y = minY; y < maxY; ++y) {

			// この y 座標を通る辺を探し、そのときの x 座標を記録
			// 通る辺が複数ある場合（普通２本ある）、左のほうがdxs[0]、右のほうがdxs[1] にはいる。
			// sxs/sys にはそのときのコピー元座標が入る

			dys[0] = dys[1] = y;
			dxs[0] = 0x7FFFFFFF;
			dxs[1] = -0x80000000;

			for (int i = 0; i < 4; ++i) {
				final int j = (i + 1) & 3;

				final int x;
				final float srx;
				final float sry;
				if (dy[i] == y) {
					// 始点の y 座標が一致
					x = dx[i];
					srx = sx[i];
					sry = sy[i];
				} else if (dy[j] == y) {
					// 終点の y 座標が一致
					x = dx[j];
					srx = sx[j];
					sry = sy[j];
				} else if (dy[i] > y ^ dy[j] > y) {
					// またがってる

					final int toY = (y - dy[i]);
					final int toJ = (dy[j] - dy[i]);

					x = ((dx[j] - dx[i]) * toY + toJ * dx[i]) / toJ;
					srx = (float) ((sx[j] - sx[i]) * toY) / toJ + sx[i];
					sry = (float) ((sy[j] - sy[i]) * toY) / toJ + sy[i];
				} else {
					continue;
				}

				// x　が小さいなら左辺
				if (x < dxs[0]) {
					dxs[0] = x;
					sxs[0] = srx;
					sys[0] = sry;
				}

				// x が大きいなら右辺
				if (x > dxs[1]) {
					dxs[1] = x;
					sxs[1] = srx;
					sys[1] = sry;
				}

			}

			// 一行分コピー
			gsquare_line(win, dest, dxs, dys, src, sxs, sys);
		}
	}

	private static void gsquare_line(final Bmscr win, final BufferedImage dest, final int[] dxs, final int[] dys,
			final BufferedImage src, final float[] sxs, final float[] sys) {

		if (dxs[1] == dxs[0]) {
			return;
		}
		if (dxs[1] <= 0 || dxs[0] >= dest.getWidth()) {
			return;
		}
		if (dys[0] < 0 || dys[0] >= dest.getHeight()) {
			return;
		}

		int dx = dxs[0];
		int dw = dxs[1] - dxs[0];
		int dy = dys[0];
		float sx = sxs[0];
		float sw = sxs[1] - sxs[0];
		float sy = sys[0];
		float sh = sys[1] - sys[0];

		if (dx >= dest.getWidth()) {
			return;
		}

		if (dx < 0) {

			sx += sw * (0 - dx) / dw;
			sw -= sw * (0 - dx) / dw;
			sy += sh * (0 - dx) / dw;
			sh -= sh * (0 - dx) / dw;

			dw -= -dx;
			dx += -dx;
		}

		if (dw <= 0) {
			return;
		}

		if (dx + dw > dest.getWidth()) {

			sw = sw * (dest.getWidth() - dx) / dw;
			sh = sh * (dest.getWidth() - dx) / dw;
			dw = dest.getWidth() - dx;
		}

		// ソースのクリッピングはしない、失敗したら自己責任。

		final int[] srcPixels = GraphicsRenderer.srcPixels;

		final WritableRaster dr = dest.getRaster();
		final Raster sr = src.getRaster();

		// int で計算してスピードアップ！

		final int vsx = (int) (sw * 0x10000 / dw);
		final int vsy = (int) (sh * 0x10000 / dw);

		int srx = (int) (sx * 0x10000);
		int sry = (int) (sy * 0x10000);

		if (vsy == 0) {
			// 回転無しコピーはよく使われると思われるのでちょっとだけ高速化
			final int syi = (int) sy;
			for (int i = 0, end = dw * 3; i < end; i += 3) {

				srcPixels[i] = sr.getSample(srx >>> 16, syi, 0);
				srcPixels[i + 1] = sr.getSample(srx >>> 16, syi, 1);
				srcPixels[i + 2] = sr.getSample(srx >>> 16, syi, 2);
				srx += vsx;
			}
		} else {
			for (int i = 0, end = dw * 3; i < end; i += 3) {
				srcPixels[i] = sr.getSample(srx >>> 16, sry >>> 16, 0);
				srcPixels[i + 1] = sr.getSample(srx >>> 16, sry >>> 16, 1);
				srcPixels[i + 2] = sr.getSample(srx >>> 16, sry >>> 16, 2);
				srx += vsx;
				sry += vsy;
			}
		}

		if (win.gmode >= 2) {
			dr.getPixels(dx, dy, dw, 1, destPixels);
		}

		gcopy_line(win, destPixels, srcPixels, dw);

		dr.setPixels(dx, dy, dw, 1, destPixels);
	}

	/**
	 * 四角形を描画。
	 * @param win 描画先画面。
	 * @param dx 描画先X。
	 * @param dy 描画先Y。
	 */
	public static synchronized void gsquare(final Bmscr win, int[] dx, int[] dy) {

		final BufferedImage dest = win.backImage;

		if (dest.getType() != BufferedImage.TYPE_3BYTE_BGR) {
			throw new IllegalArgumentException("ビットタイプ " + dest.getType() + " はサポートされません。");
		}

		if (win.gmode < 3) {

			win.backGraphics.fillPolygon(dx, dy, 4);

		} else if (win.gmode == 3) {

			final Composite prev = win.backGraphics.getComposite();
			try {
				win.backGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, win.galpha / 256.0F));

				win.backGraphics.fillPolygon(dx, dy, 4);
			} finally {
				win.backGraphics.setComposite(prev);
			}
		} else {

			//			gtriangle(win, dest, new int[] { dx[0], dx[1], dx[2] }, new int[] {
			//					dy[0], dy[1], dy[2] });
			//			gtriangle(win, dest, new int[] { dx[0], dx[2], dx[3] }, new int[] {
			//					dy[0], dy[2], dy[3] });
			//		

			int minY = dy[0];
			int maxY = dy[0];
			for (int i = 1; i < 4; ++i) {
				if (dy[i] < minY) {
					minY = dy[i];
				}
				if (dy[i] > maxY) {
					maxY = dy[i];
				}
			}
			if (minY < 0) {
				minY = 0;
			}
			if (maxY > dest.getHeight()) {
				maxY = dest.getHeight();
			}

			for (int y = minY; y < maxY; ++y) {

				int dx1 = 0x7FFFFFFF;
				int dx2 = -0x80000000;
				for (int i = 0; i < 4; ++i) {
					final int j = (i + 1) & 3;

					final int x;
					if (dy[i] == y) {
						x = dx[i];
					} else if (dy[j] == y) {
						x = dx[j];
					} else if (dy[i] > y ^ dy[j] > y) {

						final int toJ = (dy[j] - dy[i]);

						x = ((dx[j] - dx[i]) * (y - dy[i]) + toJ * dx[i]) / toJ;
					} else {
						continue;
					}

					if (x < dx1) {
						dx1 = x;
					}
					if (x > dx2) {
						dx2 = x;
					}

				}

				gsquare_line(win, dest, dx1, dx2, y);
			}
		}
	}

	private static void gsquare_line(Bmscr win, BufferedImage dest, int x1, int x2, final int y) {

		if (x1 == x2) {
			return;
		}
		if (y < 0 || y >= dest.getHeight()) {
			return;
		}

		//		if (x2 < x1) {
		//			int tmp = x1;
		//			x1 = x2;
		//			x2 = tmp;
		//		}

		if (x1 < 0) {
			x1 = 0;
		}

		if (x2 > dest.getWidth()) {
			x2 = dest.getWidth();
		}

		if (x2 <= x1) {
			return;
		}

		final WritableRaster dr = dest.getRaster();

		dr.getPixels(x1, y, x2 - x1, 1, destPixels);

		fill_line(win, destPixels, x2 - x1);

		dr.setPixels(x1, y, x2 - x1, 1, destPixels);
	}

	private static void fill_line(Bmscr win, int[] destPixels, int w) {

		final int byteCount = w * 3;
		final int red = win.color.getRed();
		final int green = win.color.getGreen();
		final int blue = win.color.getBlue();

		switch (win.gmode) {
		case 0:
		case 1:
		case 2:
			for (int i = 0; i < byteCount; i += 3) {

				destPixels[i] = red;
				destPixels[i + 1] = green;
				destPixels[i + 2] = blue;
			}
			break;
		case 3:
		case 4: {
			final int srcA = win.galpha;
			if (srcA == 256) {
				for (int i = 0; i < byteCount; i += 3) {

					destPixels[i] = red;
					destPixels[i + 1] = green;
					destPixels[i + 2] = blue;
				}

			} else if (srcA != 0) {
				for (int i = 0; i < byteCount; i += 3) {

					destPixels[i] += (red - destPixels[i]) * srcA >>> 8;
					destPixels[i + 1] += (green - destPixels[i + 1]) * srcA >>> 8;
					destPixels[i + 2] += (blue - destPixels[i + 2]) * srcA >>> 8;
				}
			}
		}
			break;
		case 5: {
			final int srcA = win.galpha;

			if (srcA == 256) {
				for (int i = 0; i < byteCount; i += 3) {

					destPixels[i] += red;
					if (destPixels[i] > 255) {
						destPixels[i] = 255;
					}

					destPixels[i + 1] += green;
					if (destPixels[i + 1] > 255) {
						destPixels[i + 1] = 255;
					}

					destPixels[i + 2] += blue;
					if (destPixels[i + 2] > 255) {
						destPixels[i + 2] = 255;
					}
				}
			} else if (srcA != 0) {
				for (int i = 0; i < byteCount; i += 3) {

					destPixels[i] += red * srcA >>> 8;
					if (destPixels[i] > 255) {
						destPixels[i] = 255;
					}

					destPixels[i + 1] += green * srcA >>> 8;
					if (destPixels[i + 1] > 255) {
						destPixels[i + 1] = 255;
					}

					destPixels[i + 2] += blue * srcA >>> 8;
					if (destPixels[i + 2] > 255) {
						destPixels[i + 2] = 255;
					}
				}
			}
		}
			break;
		case 6: {
			final int srcA = win.galpha;

			if (srcA == 256) {
				for (int i = 0; i < byteCount; i += 3) {

					destPixels[i] -= red;
					if (destPixels[i] < 0) {
						destPixels[i] = 0;
					}

					destPixels[i + 1] -= green;
					if (destPixels[i + 1] < 0) {
						destPixels[i + 1] = 0;
					}

					destPixels[i + 2] -= blue;
					if (destPixels[i + 2] < 0) {
						destPixels[i + 2] = 0;
					}
				}
			} else if (srcA != 0) {
				for (int i = 0; i < byteCount; i += 3) {

					destPixels[i] -= red * srcA >>> 8;
					if (destPixels[i] < 0) {
						destPixels[i] = 0;
					}

					destPixels[i + 1] -= green * srcA >>> 8;
					if (destPixels[i + 1] < 0) {
						destPixels[i + 1] = 0;
					}

					destPixels[i + 2] -= blue * srcA >>> 8;
					if (destPixels[i + 2] < 0) {
						destPixels[i + 2] = 0;
					}
				}
			}
		}
			break;
		}
	}

}
