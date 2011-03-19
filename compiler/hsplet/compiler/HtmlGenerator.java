/*
 * $Id: HtmlGenerator.java,v 1.4 2006/01/16 19:34:22 Yuki Exp $
 */
package hsplet.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * HSPLet で目的のコードを実行するための HTML を生成するクラス。
 * <p>
 * テンプレートを読み込んで、必要な箇所を書き換えて出力する。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.4 $, $Date: 2006/01/16 19:34:22 $
 */
public final class HtmlGenerator implements Serializable {

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = -5216057552518726784L;

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: HtmlGenerator.java,v 1.4 2006/01/16 19:34:22 Yuki Exp $";

	/** テンプレートファイル名。 */
	private static final String TEMPLATE_NAME = "template.html";

	/** クラス名を置き換えるタグ。 */
	private static final String START_CLASS_NAME_TAG_REGEX = "\\{START_CLASS_NAME\\}";

	/** タイトルを置き換えるタグ。 */
	private static final String TITLE_TAG_REGEX = "\\{TITLE\\}";

	/** mp3用ライブラリを置き換えるタグ。 */
	private static final String LIBS_TAG_REGEX = "\\{LIBS\\}";

	/** 横幅を置き換えるタグ。 */
	private static final String WIDTH_TAG_REGEX = "\\{WIDTH\\}";

	/** 高さを置き換えるタグ。 */
	private static final String HEIGHT_TAG_REGEX = "\\{HEIGHT\\}";

	/** 実行するクラス名。 */
	private final String className;

	/** ページタイトル */
	private final String title;

	/** 拡張ライブラリ。 */
	private final Collection libs;

	/** 横幅。 */
	private final int width;

	/** 高さ。 */
	private final int height;

	/** テンプレートファイル */
	private final File template;

	/**
	 * 実行するクラス名を指定してオブジェクトを構築する。
	 * 
	 * @param className 実行するクラス名。
	 * @param title ページタイトル。
	 * @param libs 拡張ライブラリのリスト。
	 * @param h 横幅
	 * @param w 高さ
	 * @param template テンプレートファイル。
	 */
	public HtmlGenerator(final String className, final String title,
			final Collection libs, final int w, int h, final File template) {

		this.className = className;
		this.title = title;
		this.libs = libs;
		this.width = w;
		this.height = h;
		this.template = template;
	}

	/**
	 * HTML を生成する。
	 * 
	 * @param out 出力先。
	 * @throws IOException 入出力エラーが発生したとき。
	 */
	public void generate(final OutputStream out) throws IOException {

		final PrintWriter w = new PrintWriter(out, true);

		final InputStream in = template == null ? getClass()
				.getResourceAsStream(TEMPLATE_NAME) : new FileInputStream(
				template);
		try {

			final BufferedReader r = new BufferedReader(new InputStreamReader(
					in));

			for (;;) {

				final String line = r.readLine();
				if (line == null) {
					break;
				}

				final StringBuffer extLibNames = new StringBuffer();
				for (final Iterator i = libs.iterator(); i.hasNext();) {
					extLibNames.append(i.next() + ",");
				}

				w.println(line
						.replaceAll(START_CLASS_NAME_TAG_REGEX, className)
						.replaceAll(LIBS_TAG_REGEX, extLibNames.toString())
						.replaceAll(WIDTH_TAG_REGEX, "" + width).replaceAll(
								HEIGHT_TAG_REGEX, "" + height).replaceAll(
								TITLE_TAG_REGEX, title));
			}
		} finally {
			in.close();
		}

	}

}
