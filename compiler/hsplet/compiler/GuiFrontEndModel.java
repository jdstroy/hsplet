/*
 * $Id: GuiFrontEndModel.java,v 1.5 2006/05/09 11:57:31 Yuki Exp $
 */
package hsplet.compiler;

import hsplet.compiler.http.HttpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import edu.stanford.ejalbert.BrowserLauncher;

/**
 * GUI フロントエンドの情報とロジックを保持するクラス。
 * 
 * @author Yuki
 * @version $Revision: 1.5 $, $Date: 2006/05/09 11:57:31 $
 */
public class GuiFrontEndModel implements Serializable {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: GuiFrontEndModel.java,v 1.5 2006/05/09 11:57:31 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = 8731834569307009875L;

	/** mp3 用ライブラリのファイル名。 */
	private static final String MP3_LIB_NAME = "lib/jl1.0.jar";

	/** strf 用ライブラリのファイル名。 */
	private static final String STRF_LIB_NAME = "lib/hb16.jar";

	private String title = "";

	private int width = 640;

	private int height = 480;

	private List<String> packFiles = new ArrayList<String>();

	private boolean mp3;

	private boolean strf;

	private List<String> extLibs = new ArrayList<String>();

	private String htmlName = "";

	private String jarName = "";

	private String templateName = "";

	private String startClass = "";

	void synch(final GuiFrontEnd gui, final boolean save) {

		if (save) {

			title = gui.titleText.getText();
			try {
				width = Integer.parseInt(gui.widthText.getText());
			} catch (NumberFormatException e) {
			}
			try {
				height = Integer.parseInt(gui.heightText.getText());
			} catch (NumberFormatException e) {
			}

			jarName = gui.jarText.getText();
			htmlName = gui.htmlText.getText();
			templateName = gui.templateText.getText();
			startClass = gui.startClassText.getText();

			packFiles.clear();
			for (int i = 0; i < gui.packListModel.getSize(); ++i) {
				packFiles.add(gui.packListModel.get(i).toString());
			}

			mp3 = gui.mp3CheckBox.isSelected();

			strf = gui.strfCheckBox.isSelected();

			extLibs.clear();
			for (final Iterator<Map.Entry<String, JCheckBox>> i = gui.libChecks.entrySet().iterator(); i.hasNext();) {
				final Map.Entry<String, JCheckBox> libCheck = i.next();

				if ((libCheck.getValue()).isSelected()) {
					extLibs.add(libCheck.getKey());
				}
			}

		} else {

			gui.titleText.setText(title);
			gui.widthText.setText("" + width);
			gui.heightText.setText("" + height);
			gui.jarText.setText(jarName);
			gui.htmlText.setText(htmlName);
			gui.templateText.setText(templateName);
			gui.startClassText.setText(startClass);

			gui.packListModel.clear();

			for (int i = 0; i < packFiles.size(); ++i) {
				gui.packListModel.add(i, new File(packFiles.get(i)));
			}

			gui.mp3CheckBox.setSelected(mp3);
			gui.strfCheckBox.setSelected(strf);

			for (final Iterator i = gui.libChecks.entrySet().iterator(); i.hasNext();) {
				final Map.Entry libCheck = (Map.Entry) i.next();

				((JCheckBox) libCheck.getValue()).setSelected(extLibs.contains(libCheck.getKey()));
			}

		}

	}

	static HttpServer server = new HttpServer(6791);

	static Thread serverThread = new Thread(server);

	public void compile(final boolean run) {

		try {

			final Collection<String> packs = new HashSet<String>();
			for (int i = 0; i < packFiles.size(); ++i) {
				packs.add("" + packFiles.get(i));
			}

			final Collection<String> libs = new HashSet<String>();
			libs.addAll(this.extLibs);

			if (mp3) {
				libs.add(MP3_LIB_NAME);
			}
			if (strf) {
				libs.add(STRF_LIB_NAME);
			}

			final Collection<String> libdirs = new HashSet<String>();
			libdirs.add("ext");
			libdirs.add("lib");

			Compiler.build(jarName.length() == 0 ? null : new File(jarName), htmlName.length() == 0 ? null : new File(
					htmlName), templateName.length() == 0 ? null : new File(templateName), startClass, title, packs,
					libs, libdirs, width, height);

			if (run && htmlName.length() != 0) {

				System.out.println(server);

				if (!serverThread.isAlive()) {
					serverThread = new Thread(server);
					serverThread.start();
				}
				BrowserLauncher.openURL("http://localhost:6791" + HttpServer.mapURL(new File(htmlName)));
			} else {
				JOptionPane.showMessageDialog(null, "コンパイルに成功しました。", "HSPLet", JOptionPane.INFORMATION_MESSAGE);

			}
		} catch (Throwable e) {
			e.printStackTrace();

			JOptionPane.showMessageDialog(null, e.toString(), "HSPLet", JOptionPane.ERROR_MESSAGE);
		}
	}

	private static void execTest(final File folder, final String className, final int width, final int height)
			throws IOException {

		final File java = new File(System.getProperty("java.home"), "bin/java");
		final String pathSeparator = System.getProperty("path.separator");

		final String[] jars = folder.list(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".jar");
			}
		});

		final StringBuffer cmdline = new StringBuffer();

		cmdline.append("\"" + java + "\" -cp \".");

		for (int i = 0; i < jars.length; ++i) {
			cmdline.append(pathSeparator + jars[i]);
		}

		cmdline.append(pathSeparator + new File("./BrowserLauncher2-10rc4.jar").getAbsolutePath());

		cmdline.append("\" hsplet.Application --debug=true --width=" + width + " --height=" + height + " --startClass="
				+ Compiler.generateClassName(className));

		Runtime.getRuntime().exec(cmdline.toString(), null, folder);

	}

	public static GuiFrontEndModel load(final String path) {

		try {
			final ObjectInputStream i = new ObjectInputStream(new FileInputStream(path));

			final GuiFrontEndModel result = (GuiFrontEndModel) i.readObject();

			i.close();

			return result;

		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new GuiFrontEndModel();
	}

	public void save(final String path) {

		try {
			final ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(path));

			o.writeObject(this);

			o.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
