/*
 * Copyright 2012 John Stroy
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import hsplet.Context;
import hsplet.PEXInfo;
import hsplet.function.FunctionBase;
import hsplet.function.GuiCommand;
import hsplet.gui.Bmscr;
import hsplet.variable.Operand;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javolution.context.ArrayFactory;

/**
 *
 * @author jdstroy
 */
public class elona extends FunctionBase {

    private Context context;

    public elona(final Context context) {
        this.context = context;
    }
    /**
     * grotate, elona style - do not let pixels reach 0,0,0 Not implemented
     * correctly.
     *
     * @param source_window_id
     * @param p2_x_coordinate
     * @param p3_y_coordinate
     * @param p4_rotation_angle_radians
     * @param dwv
     * @param dwi
     * @param dhv
     * @param dhi
     */

    public void grotate(@PEXInfo int source_window_id, int p2_x_coordinate,
            int p3_y_coordinate, double p4_rotation_angle_radians,
            //int p5_x_size, int p6_y_size
            final Operand dwv, final int dwi, final Operand dhv, final int dhi) {
        final Bmscr target = context.windows.get(context.targetWindow), source = context.windows.get(source_window_id);
        int minX = Math.min(target.cx, p2_x_coordinate);
        int width = Math.abs(target.cx - p2_x_coordinate);
        int minY = Math.min(target.cy, p3_y_coordinate);
        int height = Math.abs(target.cy - p3_y_coordinate);


        final int dw = toInt(dwv, dwi, target.gwidth);
        final int dh = toInt(dhv, dhi, target.gheight);

        final double w = dw / 2 - 0.5;
        final double h = dh / 2 - 0.5;

        final int cx = target.cx;
        final int cy = target.cy;

        final double si = Math.sin(p4_rotation_angle_radians);
        final double co = Math.cos(p4_rotation_angle_radians);

        int[] dx = new int[]{(int) Math.round(-w * co + h * si) + cx, (int) Math.round(w * co + h * si) + cx,
            (int) Math.round(w * co - h * si) + cx, (int) Math.round(-w * co - h * si) + cx,};
        int[] dy = new int[]{(int) Math.round(-w * si - h * co) + cy, (int) Math.round(w * si - h * co) + cy,
            (int) Math.round(w * si + h * co) + cy, (int) Math.round(-w * si + h * co) + cy,};
        int[] sxs = new int[]{p2_x_coordinate, p2_x_coordinate + target.gwidth - 1, p2_x_coordinate + target.gwidth - 1, p2_x_coordinate};
        int[] sys = new int[]{p3_y_coordinate, p3_y_coordinate, p3_y_coordinate + target.gheight - 1, p3_y_coordinate + target.gheight - 1};

        try {
            gsquare(target, dx, dy, source.backImage, sxs, sys);
        } catch (ArrayIndexOutOfBoundsException ex) {
            Logger.getLogger(GuiCommand.class.getName()).log(Level.SEVERE, "", ex);
        }

        final int l = Math.min(Math.min(Math.min(dx[0], dx[1]), dx[2]), dx[3]);
        final int t = Math.min(Math.min(Math.min(dy[0], dy[1]), dy[2]), dy[3]);
        final int r = Math.max(Math.max(Math.max(dx[0], dx[1]), dx[2]), dx[3]);
        final int b = Math.max(Math.max(Math.max(dy[0], dy[1]), dy[2]), dy[3]);

        target.redraw(l, t, r - l, b - t);
    }

    public void gsquare(final Bmscr win, final int[] dx, final int[] dy, final BufferedImage src,
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

    private void gsquare_line(final Bmscr win, final BufferedImage dest, final int[] dxs, final int[] dys,
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

        final int[] srcPixels = ArrayFactory.INTS_FACTORY.array(1024 * 32);
        final int[] destPixels = ArrayFactory.INTS_FACTORY.array(1024 * 32);

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

        final int[] destPixelsFinal = destPixels;
        final int[] dOpt = new int[]{dx, dy, dw};
        gcopy_line(win, destPixelsFinal, srcPixels, dOpt[2]);
        try {
            EventQueue.invokeAndWait(new Runnable() {

                @Override
                public void run() {

                    dr.setPixels(dOpt[0], dOpt[1], dOpt[2], 1, destPixelsFinal);
                }
            });
        } catch (InterruptedException ex) {
            Logger.getLogger(elona.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(elona.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ArrayFactory.INTS_FACTORY.recycle(srcPixels);
        ArrayFactory.INTS_FACTORY.recycle(destPixels);

    }

    private void gcopy_line(final Bmscr win, final int[] destPixels, final int[] srcPixels, final int w) {

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
}
