/*
 * $Id: GuiFrontEnd.java,v 1.10 2006/05/09 11:57:31 Yuki Exp $
 */
package hsplet.compiler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

/**
 * コンパイラの GUI フロントエンド。
 * 
 * @author Yuki
 * @version $Revision: 1.10 $, $Date: 2006/05/09 11:57:31 $
 */
public class GuiFrontEnd extends JFrame {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: GuiFrontEnd.java,v 1.10 2006/05/09 11:57:31 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = 4690822266500902135L;

	private static final BufferedImage logoImage;

	static {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
/*
		try {
			PrintStream s = new PrintStream(new FileOutputStream("compiler.log"));
			System.setOut(s);
			System.setErr(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
*/
		BufferedImage logo = null;

		try {
			logo = ImageIO.read(GuiFrontEnd.class.getResource("logo.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logoImage = logo;

	}

	private String lastPath;

	private boolean modified;

	private GuiFrontEndModel model = new GuiFrontEndModel();

	/**
	 * This method initializes jCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getStrfCheckBox() {
		if (strfCheckBox == null) {
			strfCheckBox = new JCheckBox();
			strfCheckBox.setActionCommand("strf");
			strfCheckBox.setText("strfを使う");
		}
		return strfCheckBox;
	}

	/**
	 * This method initializes jarPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJarPanel() {
		if (jarPanel == null) {
			jarLabel = new JLabel();
			jarLabel.setText("Output JAR :");
			jarPanel = new JPanel();
			jarPanel.setLayout(new BorderLayout());
			jarPanel.setPreferredSize(new java.awt.Dimension(100, 20));
			jarPanel.add(jarLabel, java.awt.BorderLayout.WEST);
			jarPanel.add(getJarButton(), java.awt.BorderLayout.EAST);
			jarPanel.add(getJarText(), java.awt.BorderLayout.CENTER);
		}
		return jarPanel;
	}

	/**
	 * This method initializes jarButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJarButton() {
		if (jarButton == null) {
			jarButton = new JButton();
			jarButton.setText("Browse...");
			jarButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					referJar();
				}
			});
		}
		return jarButton;
	}

	/**
	 * This method initializes htmlPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getHtmlPanel() {
		if (htmlPanel == null) {
			htmlLabel = new JLabel();
			htmlLabel.setText("HTML Output:");
			htmlPanel = new JPanel();
			htmlPanel.setLayout(new BorderLayout());
			htmlPanel.setPreferredSize(new java.awt.Dimension(100, 20));
			htmlPanel.add(htmlLabel, java.awt.BorderLayout.WEST);
			htmlPanel.add(getHtmlText(), java.awt.BorderLayout.CENTER);
			htmlPanel.add(getHtmlButton(), java.awt.BorderLayout.EAST);
		}
		return htmlPanel;
	}

	/**
	 * This method initializes htmlText	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getHtmlText() {
		if (htmlText == null) {
			htmlText = new JTextField();
			htmlText.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					modified = true;
				}
			});
		}
		return htmlText;
	}

	/**
	 * This method initializes htmlButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getHtmlButton() {
		if (htmlButton == null) {
			htmlButton = new JButton();
			htmlButton.setText("Browse...");
			htmlButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					referHtml();
				}
			});
		}
		return htmlButton;
	}

	/**
	 * This method initializes templatePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTemplatePanel() {
		if (templatePanel == null) {
			templateLabel = new JLabel();
			templateLabel.setText("Template:");
			templatePanel = new JPanel();
			templatePanel.setLayout(new BorderLayout());
			templatePanel.setPreferredSize(new java.awt.Dimension(100, 20));
			templatePanel.add(templateLabel, java.awt.BorderLayout.WEST);
			templatePanel.add(getTemplateText(), java.awt.BorderLayout.CENTER);
			templatePanel.add(getTemplateButton(), java.awt.BorderLayout.EAST);
		}
		return templatePanel;
	}

	/**
	 * This method initializes templateText	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTemplateText() {
		if (templateText == null) {
			templateText = new JTextField();
			templateText.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					modified = true;
				}
			});
		}
		return templateText;
	}

	/**
	 * This method initializes templateButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getTemplateButton() {
		if (templateButton == null) {
			templateButton = new JButton();
			templateButton.setText("Browse...");
			templateButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					referTemplate();
				}
			});
		}
		return templateButton;
	}

	/**
	 * This method initializes menuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	private JMenuBar getMenu() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.add(getFileMenu());
		}
		return menuBar;
	}

	/**
	 * This method initializes fileMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.setMnemonic(java.awt.event.KeyEvent.VK_F);
			fileMenu.add(getFileMenu_new());
			fileMenu.add(getFileMenu_open());
			fileMenu.add(getFileMenu_save());
			fileMenu.add(getFileMenu_saveAs());
			fileMenu.add(getFileMenu_sep());
			fileMenu.add(getFileMenu_exit());
		}
		return fileMenu;
	}

	/**
	 * This method initializes fileMenu_new	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getFileMenu_new() {
		if (fileMenu_new == null) {
			fileMenu_new = new JMenuItem();
			fileMenu_new.setText("New");
			fileMenu_new.setMnemonic(java.awt.event.KeyEvent.VK_N);
			fileMenu_new.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					new_();
				}
			});
		}
		return fileMenu_new;
	}

	/**
	 * This method initializes fileMenu_open	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getFileMenu_open() {
		if (fileMenu_open == null) {
			fileMenu_open = new JMenuItem();
			fileMenu_open.setText("Open");
			fileMenu_open.setMnemonic(java.awt.event.KeyEvent.VK_O);
			fileMenu_open.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					open();
				}
			});
		}
		return fileMenu_open;
	}

	/**
	 * This method initializes fileMenu_save	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getFileMenu_save() {
		if (fileMenu_save == null) {
			fileMenu_save = new JMenuItem();
			fileMenu_save.setText("Save");
			fileMenu_save.setMnemonic(java.awt.event.KeyEvent.VK_S);
			fileMenu_save.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					save();
				}
			});
		}
		return fileMenu_save;
	}

	/**
	 * This method initializes fileMenu_saveAs	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getFileMenu_saveAs() {
		if (fileMenu_saveAs == null) {
			fileMenu_saveAs = new JMenuItem();
			fileMenu_saveAs.setText("Save As...");
			fileMenu_saveAs.setMnemonic(java.awt.event.KeyEvent.VK_A);
			fileMenu_saveAs.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					saveAs();
				}
			});
		}
		return fileMenu_saveAs;
	}

	/**
	 * This method initializes fileMenu_sep	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JSeparator getFileMenu_sep() {
		if (fileMenu_sep == null) {
			fileMenu_sep = new JSeparator();
		}
		return fileMenu_sep;
	}

	/**
	 * This method initializes fileMenu_exit	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getFileMenu_exit() {
		if (fileMenu_exit == null) {
			fileMenu_exit = new JMenuItem();
			fileMenu_exit.setText("Exit");
			fileMenu_exit.setMnemonic(java.awt.event.KeyEvent.VK_X);
			fileMenu_exit.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					exit();
				}
			});
		}
		return fileMenu_exit;
	}

	/**
	 * This method initializes toolBar	
	 * 	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getToolBar() {
		if (toolBar == null) {
			toolBar = new JToolBar();
			toolBar.setFloatable(false);
			toolBar.add(getNewButton());
			toolBar.add(getOpenButton());
			toolBar.add(getSaveButton());
		}
		return toolBar;
	}

	/**
	 * This method initializes newButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getNewButton() {
		if (newButton == null) {
			newButton = new JButton();
			newButton.setIcon(new ImageIcon(getClass().getResource("new.gif")));
			newButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					new_();
				}
			});
		}
		return newButton;
	}

	/**
	 * This method initializes openButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOpenButton() {
		if (openButton == null) {
			openButton = new JButton();
			openButton.setIcon(new ImageIcon(getClass().getResource("open.gif")));
			openButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					open();
				}
			});
		}
		return openButton;
	}

	/**
	 * This method initializes saveButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new JButton();
			saveButton.setIcon(new ImageIcon(getClass().getResource("save.gif")));
			saveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					save();
				}
			});
		}
		return saveButton;
	}

	/**
	 * This method initializes jarText	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJarText() {
		if (jarText == null) {
			jarText = new JTextField();
			jarText.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					modified = true;
				}
			});
		}
		return jarText;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getToolBarBorderPanel() {
		if (toolbarBorderPanel == null) {
			toolbarBorderPanel = new JPanel();
			toolbarBorderPanel.setPreferredSize(new Dimension(10, 2));
		}
		return toolbarBorderPanel;
	}

	/**
	 * This method initializes startClassPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getStartClassPanel() {
		if (startClassPanel == null) {
			startClassLabel = new JLabel();
			startClassLabel.setText("Startup file:");
			startClassPanel = new JPanel();
			startClassPanel.setLayout(new BorderLayout());
			startClassPanel.add(startClassLabel, java.awt.BorderLayout.WEST);
			startClassPanel.add(getStartClassText(), java.awt.BorderLayout.CENTER);
		}
		return startClassPanel;
	}

	/**
	 * This method initializes startClassText	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getStartClassText() {
		if (startClassText == null) {
			startClassText = new JTextField();
		}
		return startClassText;
	}

	public static void main(final String[] args) {

		startupFolder = new File("").getAbsoluteFile();

		final GuiFrontEnd dlg = new GuiFrontEnd();

		dlg.setVisible(true);

	}

	public static class LogoPanel extends JComponent {
		//@Override
		public void paint(Graphics g) {
			g.drawImage(logoImage, 0, getHeight() - logoImage.getHeight(), null);

			g.setColor(Color.white);
			g.fillRect(0, 0, getWidth() - 2, getHeight() - logoImage.getHeight());
			g.setColor(Color.lightGray);
			g.fillRect(getWidth() - 2, 0, 1, getHeight());
			g.setColor(Color.white);
			g.fillRect(getWidth() - 1, 0, 1, getHeight());
		}
	}

	public static class FileListModel extends DefaultListModel<File> {

                @Override
		public File getElementAt(int index) {
			return (new File(get(index).toString()));
		}
                
                
	}

	public static File startupFolder;

	/**
	 * This method initializes mp3CheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getMp3CheckBox() {

		if (mp3CheckBox == null) {
			mp3CheckBox = new JCheckBox();
			mp3CheckBox.setText("mp3を使う");
			mp3CheckBox.setActionCommand("mp3");
		}
		return mp3CheckBox;
	}

	/**
	 * This method initializes widthText
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getWidthText() {

		if (widthText == null) {
			widthText = new JTextField();
			widthText.setColumns(4);
			widthText.setText("640");
			widthText.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					modified = true;
				}
			});
		}
		return widthText;
	}

	/**
	 * This method initializes heightText
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getHeightText() {

		if (heightText == null) {
			heightText = new JTextField();
			heightText.setColumns(4);
			heightText.setText("480");
			heightText.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					modified = true;
				}
			});
		}
		return heightText;
	}

	/**
	 * This method initializes titleText	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTitleText() {
		if (titleText == null) {
			titleText = new JTextField();
			titleText.setPreferredSize(new java.awt.Dimension(200, 20));
			titleText.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					modified = true;
				}
			});
		}
		return titleText;
	}

	/**
	 * This method initializes basicOptionPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getBasicOptionPanel() {
		if (basicOptionPanel == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(java.awt.FlowLayout.LEFT);
			basicOptionPanel = new JPanel();
			titleLabel = new JLabel();
			titleLabel.setText("Title:");
			heightLabel = new JLabel();
			heightLabel.setText("Height:");
			widthLabel = new JLabel();
			widthLabel.setText("Width:");
			basicOptionPanel.setLayout(flowLayout1);
			basicOptionPanel.add(titleLabel, null);
			basicOptionPanel.add(getTitleText(), null);
			basicOptionPanel.add(widthLabel, null);
			basicOptionPanel.add(getWidthText(), null);
			basicOptionPanel.add(heightLabel, null);
			basicOptionPanel.add(getHeightText(), null);
		}
		return basicOptionPanel;
	}

	/**
	 * This method initializes extLibPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getExtLibPanel() {
		if (extLibCheckPanel == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(2);
			gridLayout.setColumns(2);
			extLibCheckPanel = new JPanel();
			extLibCheckPanel.setLayout(gridLayout);
			extLibCheckPanel.add(getMp3CheckBox(), null);
			extLibCheckPanel.add(getStrfCheckBox(), null);
		}
		return extLibCheckPanel;
	}

	/**
	 * This method initializes packPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPackPanel() {
		if (packPanel == null) {
			packLabel = new JLabel();
			packLabel.setText("Packed file:");
			packPanel = new JPanel();
			packPanel.setLayout(new BorderLayout());
			packPanel.add(packLabel, java.awt.BorderLayout.NORTH);
			packPanel.add(getPackList(), java.awt.BorderLayout.CENTER);
			packPanel.add(getPackReferPanel(), java.awt.BorderLayout.SOUTH);
		}
		return packPanel;
	}

	/**
	 * This method initializes packList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList<File> getPackList() {
		if (packList == null) {
			packList = new JList<File>();
			packList.setPreferredSize(new java.awt.Dimension(200, 200));
			packList.setModel(getPackListModel());
		}
		return packList;
	}

	/**
	 * This method initializes packReferPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPackReferPanel() {
		if (packReferPanel == null) {
			packReferPanel = new JPanel();
			packReferPanel.setLayout(new BorderLayout());
			packReferPanel.add(getPackReferButton(), java.awt.BorderLayout.EAST);
			packReferPanel.add(getRemovePackButton(), java.awt.BorderLayout.WEST);
		}
		return packReferPanel;
	}

	/**
	 * This method initializes packReferButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getPackReferButton() {
		if (packReferButton == null) {
			packReferButton = new JButton();
			packReferButton.setText("Add...");
			packReferButton.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(java.awt.event.ActionEvent e) {

					referPack();
				}

			});
		}
		return packReferButton;
	}

	/**
	 * This method initializes borderPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getBorderPanel() {
		if (borderPanel == null) {
			borderPanel = new JPanel();
			borderPanel.setPreferredSize(new java.awt.Dimension(10, 2));
		}
		return borderPanel;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (extLibPanel == null) {
			extLibLabel = new JLabel();
			extLibLabel.setText("Additional libraries:");
			extLibPanel = new JPanel();
			extLibPanel.setLayout(new BorderLayout());
			extLibPanel.add(extLibLabel, java.awt.BorderLayout.NORTH);
			extLibPanel.add(getExtLibPanel(), java.awt.BorderLayout.CENTER);
		}
		return extLibPanel;
	}

	/**
	 * This method initializes logoPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private LogoPanel getLogoPanel() {
		if (logoPanel == null) {
			logoPanel = new LogoPanel();
			logoPanel.setBackground(Color.white);
			logoPanel.setPreferredSize(new java.awt.Dimension(66, 256));
		}
		return logoPanel;
	}

	/**
	 * This method initializes packListModel	
	 * 	
	 * @return javax.swing.DefaultListModel	
	 */
	private DefaultListModel<File> getPackListModel() {
		if (packListModel == null) {
			packListModel = new FileListModel();
		}
		return packListModel;
	}

	/**
	 * This method initializes removePackButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRemovePackButton() {
		if (removePackButton == null) {
			removePackButton = new JButton();
			removePackButton.setText("Remove");
			removePackButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					removePack();
				}
			});
		}
		return removePackButton;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRunButton() {
		if (runButton == null) {
			runButton = new JButton();
			runButton.setEnabled(true);
			runButton.setActionCommand("実行");
			runButton.setText("テスト実行");
			runButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					run();
				}
			});
		}
		return runButton;
	}

	private JPanel contents = null;

	private JPanel compileButtonPanel = null;

	private JButton compileButton = null;

	JCheckBox mp3CheckBox = null;

	private JLabel widthLabel = null;

	JTextField widthText = null;

	private JLabel heightLabel = null;

	JTextField heightText = null;

	Map<String, JCheckBox> libChecks = new HashMap<String, JCheckBox>();

	private JLabel titleLabel = null;

	JTextField titleText = null;

	private JPanel basicOptionPanel = null;

	private JPanel extLibCheckPanel = null;

	private JPanel packPanel = null;

	private JLabel packLabel = null;

	private JList<File> packList = null;

	private JPanel packReferPanel = null;

	private JButton packReferButton = null;

	private JPanel borderPanel = null;

	private JPanel extLibPanel = null;

	private JLabel extLibLabel = null;

	private LogoPanel logoPanel = null;

	DefaultListModel<File> packListModel = null; //  @jve:decl-index=0:visual-constraint=""

	private JButton removePackButton = null;

	private JButton runButton = null;

	JCheckBox strfCheckBox = null;

	private JPanel jarPanel = null;

	private JLabel jarLabel = null;

	private JButton jarButton = null;

	private JPanel htmlPanel = null;

	private JLabel htmlLabel = null;

	JTextField htmlText = null;

	private JButton htmlButton = null;

	private JPanel templatePanel = null;

	private JLabel templateLabel = null;

	JTextField templateText = null;

	private JButton templateButton = null;

	private JMenuBar menuBar = null;

	private JMenu fileMenu = null;

	private JMenuItem fileMenu_new = null;

	private JMenuItem fileMenu_open = null;

	private JMenuItem fileMenu_save = null;

	private JMenuItem fileMenu_saveAs = null;

	private JSeparator fileMenu_sep = null;

	private JMenuItem fileMenu_exit = null;

	private JToolBar toolBar = null;

	private JButton newButton = null;

	private JButton openButton = null;

	private JButton saveButton = null;

	JTextField jarText = null;

	private JPanel toolbarBorderPanel = null;

	private JPanel startClassPanel = null;

	private JLabel startClassLabel = null;

	JTextField startClassText = null;

	public GuiFrontEnd() {

		initialize();

		collectExtLibs();

		extLibCheckPanel.setBorder(BorderFactory.createEtchedBorder());
		borderPanel.setBorder(BorderFactory.createEtchedBorder());
		toolbarBorderPanel.setBorder(BorderFactory.createEtchedBorder());
		packList.setBorder(BorderFactory.createLoweredBevelBorder());

		pack();

		//setLocationByPlatform(true);

		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2, (Toolkit.getDefaultToolkit()
				.getScreenSize().height - getHeight()) / 2);

		setVisible(true);

		try {
			final ObjectInputStream i = new ObjectInputStream(new FileInputStream(new File(startupFolder,
					"compiler.settings")));

			lastPath = (String) i.readObject();

			model = GuiFrontEndModel.load(lastPath);

			i.close();
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.synch(this, false);

		/*
		 compileButton.setEnabled(new File(axText.getText()).isFile());
		 runButton.setEnabled(new File(axText.getText()).isFile());
		 */
	}

	private void collectExtLibs() {

		final File[] files = new File("ext").listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".jar");
			}
		});

		for (int i = 0; i < files.length; ++i) {
			final File extLib = files[i];

			final JCheckBox check = new JCheckBox();
			check.setText(extLib.getName().substring(0, extLib.getName().length() - 4));
			extLibCheckPanel.add(check, null);

			libChecks.put("ext/" + extLib.getName(), check);
		}

		((GridLayout) extLibCheckPanel.getLayout()).setRows((libChecks.size() + 2 + 1) / 2);
		((GridLayout) extLibCheckPanel.getLayout()).setColumns(2);

	}

	//@Override
	public void dispose() {

		try {
			final ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(new File(startupFolder,
					"compiler.settings")));

			o.writeObject(lastPath);

			o.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			super.dispose();

			System.exit(0);
		}
	}

	private void referPack() {
		final JFileChooser chooser = new JFileChooser();

		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(true);

		if (packListModel.getSize() != 0) {
			chooser.setCurrentDirectory(((File) packListModel.get(0)).getAbsoluteFile().getParentFile());
		}

		chooser.addChoosableFileFilter(new FileFilter() {

			public boolean accept(File f) {
				return true;
			}

			public String getDescription() {
				return "PACKFILE または追加するファイル";
			}
		});

		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

			for (int i = 0; i < chooser.getSelectedFiles().length; ++i) {
				final File file = chooser.getSelectedFiles()[i];

				if (file.getName().equalsIgnoreCase("PACKFILE")) {

					try {
						final BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file),
								"MS932"));
						try {

							for (;;) {
								final String line = in.readLine();
								if (line == null) {
									break;
								}
								final File file2 = new File(file.getAbsoluteFile().getParentFile(), line);

								if (!packListModel.contains(file2)) {
									packListModel.addElement(file2);
									modified = true;
								}
							}

						} finally {
							in.close();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					if (!packListModel.contains(file)) {
						packListModel.addElement(file);
						modified = true;
					}
				}
			}

		}

		autoStartClass();
	}

	private void removePack() {

		final int[] selecteds = packList.getSelectedIndices();
		for (int i = selecteds.length - 1; i >= 0; --i) {
			packListModel.remove(selecteds[i]);
			modified = true;
		}

		boolean found = false;

		for (int i = 0; i < packListModel.getSize(); ++i) {
			if ((packListModel.getElementAt(i).toString()).equalsIgnoreCase(startClassText.getName() + ".ax")) {
				found = true;
				break;
			}
		}

		if (!found) {
			startClassText.setText("");

			autoStartClass();
		}
	}

	private void autoStartClass() {
		if (startClassText.getText().length() == 0) {

			for (int i = 0; i < packListModel.getSize(); ++i) {
				if ((packListModel.getElementAt(i).getName()).equalsIgnoreCase("start.ax")) {
					startClassText.setText("start");
					modified = true;
					break;
				}
			}
		}

		if (startClassText.getText().length() == 0) {

			for (int i = 0; i < packListModel.getSize(); ++i) {
				final File file = (File) packListModel.get(i);
				if (file.getName().toLowerCase().endsWith(".ax")) {
					startClassText.setText(file.getName().substring(0, file.getName().length() - ".ax".length()));

					if (htmlText.getText().length() == 0) {
						htmlText.setText(new File(file.getAbsoluteFile().getParentFile(), startClassText.getText()
								+ ".html").getPath());
						autoJarName();
					}
					modified = true;
					break;
				}
			}
		}
	}

	private void referJar() {

		final JFileChooser chooser = new JFileChooser();

		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);

		chooser.addChoosableFileFilter(new FileFilter() {

			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".jar");
			}

			public String getDescription() {
				return "Java アーカイブ";
			}
		});

		if (jarText.getText().length() != 0) {
			chooser.setSelectedFile(new File(jarText.getText()));
		}

		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

			jarText.setText(chooser.getSelectedFile().getPath()
					+ (chooser.getSelectedFile().getName().indexOf('.') >= 0 ? "" : ".jar"));

			modified = true;
		}
	}

	private void referHtml() {

		final JFileChooser chooser = new JFileChooser();

		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);

		chooser.addChoosableFileFilter(new FileFilter() {

			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".html")
						|| f.getName().toLowerCase().endsWith(".htm");
			}

			public String getDescription() {
				return "HTML ページ";
			}
		});

		if (htmlText.getText().length() != 0) {
			chooser.setSelectedFile(new File(htmlText.getText()));
		}

		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

			htmlText.setText(chooser.getSelectedFile().getPath()
					+ (chooser.getSelectedFile().getName().indexOf('.') >= 0 ? "" : ".html"));

			modified = true;

			autoJarName();
		}

	}

	private void autoJarName() {

		final File htmlFile = new File(htmlText.getText());

		if (jarText.getText().length() == 0) {
			jarText.setText(new File(htmlFile.getAbsoluteFile().getParentFile(), htmlFile.getName().substring(0,
					htmlFile.getName().lastIndexOf('.') + 1)
					+ "jar").getPath());
		}

	}

	private void referTemplate() {

		final JFileChooser chooser = new JFileChooser();

		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);

		chooser.addChoosableFileFilter(new FileFilter() {

			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".html")
						|| f.getName().toLowerCase().endsWith(".htm");
			}

			public String getDescription() {
				return "HTML テンプレート";
			}
		});

		if (templateText.getText().length() != 0) {
			chooser.setSelectedFile(new File(templateText.getText()));
		}

		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

			templateText.setText(chooser.getSelectedFile().getPath());

			modified = true;
		}
	}

	private void new_() {
		lastPath = null;
		model = new GuiFrontEndModel();
		model.synch(this, false);

	}

	private void open() {

		final JFileChooser chooser = new JFileChooser();

		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);

		chooser.addChoosableFileFilter(new FileFilter() {

			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".lpj");
			}

			public String getDescription() {
				return "HSPLet Project";
			}
		});

		if (lastPath != null) {
			chooser.setSelectedFile(new File(lastPath));
		}

		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

			lastPath = chooser.getSelectedFile().getPath();
			model = GuiFrontEndModel.load(lastPath);
			model.synch(this, false);

		}

	}

	private boolean save() {

		if (lastPath == null) {
			return saveAs();
		}

		model.save(lastPath);
		modified = false;

		return true;
	}

	private boolean saveAs() {

		final JFileChooser chooser = new JFileChooser();

		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(false);

		chooser.addChoosableFileFilter(new FileFilter() {

			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".lpj");
			}

			public String getDescription() {
				return "HSPLet Project";
			}
		});

		if (lastPath != null) {
			chooser.setSelectedFile(new File(lastPath));
		} else if (htmlText.getText().length() != 0) {
			chooser.setCurrentDirectory(new File(htmlText.getText()).getAbsoluteFile().getParentFile());
		} else if (jarText.getText().length() != 0) {
			chooser.setCurrentDirectory(new File(jarText.getText()).getAbsoluteFile().getParentFile());
		}

		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

			lastPath = chooser.getSelectedFile().getPath()
					+ (chooser.getSelectedFile().getName().indexOf('.') >= 0 ? "" : ".lpj");
			model.synch(this, true);
			model.save(lastPath);
			modified = false;
			return true;
		} else {
			return false;
		}
	}

	private void exit() {

		if (modified) {
			switch (JOptionPane.showConfirmDialog(this, "設定が変更されています、保存しますか？", getTitle(),
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)) {
			case JOptionPane.CANCEL_OPTION:
				return;
			case JOptionPane.YES_OPTION:
				if (!save()) {
					return;
				}
			}
		}

		dispose();
	}

	private void compile() {
		model.synch(this, true);
		model.compile(false);
	}

	private void run() {
		model.synch(this, true);
		model.compile(true);
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {

		this.setResizable(false);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setJMenuBar(getMenu());
		this.setTitle("HSPLet 3.0 compiler");
		this.setContentPane(getContents());

		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				exit();
			}
		});
	}

	/**
	 * This method initializes contentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getContents() {

		if (contents == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 3;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints6.insets = new java.awt.Insets(0, 0, 5, 5);
			gridBagConstraints6.gridy = 4;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridwidth = 3;
			gridBagConstraints5.insets = new java.awt.Insets(0, 0, 0, 0);
			gridBagConstraints5.gridy = 2;
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints41.gridy = 1;
			gridBagConstraints41.weightx = 1.0;
			gridBagConstraints41.gridwidth = 3;
			gridBagConstraints41.gridx = 1;
			GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
			gridBagConstraints32.gridx = 2;
			gridBagConstraints32.gridwidth = 2;
			gridBagConstraints32.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints32.insets = new java.awt.Insets(0, 5, 0, 5);
			gridBagConstraints32.gridy = 8;
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.gridx = 2;
			gridBagConstraints22.gridwidth = 2;
			gridBagConstraints22.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints22.insets = new java.awt.Insets(0, 5, 0, 5);
			gridBagConstraints22.gridy = 7;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 2;
			gridBagConstraints12.gridwidth = 2;
			gridBagConstraints12.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints12.insets = new java.awt.Insets(0, 5, 0, 5);
			gridBagConstraints12.gridy = 6;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 1;
			gridBagConstraints31.gridheight = 8;
			gridBagConstraints31.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints31.gridy = 3;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 3;
			gridBagConstraints21.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints21.insets = new java.awt.Insets(0, 0, 0, 5);
			gridBagConstraints21.gridy = 5;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 2;
			gridBagConstraints11.gridwidth = 2;
			gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints11.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints11.gridy = 9;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints2.gridx = 2;
			gridBagConstraints2.gridy = 4;
			gridBagConstraints2.insets = new java.awt.Insets(0, 5, 0, 5);
			gridBagConstraints2.gridheight = 2;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
			gridBagConstraints.gridx = 2;
			gridBagConstraints.gridy = 3;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 2;
			gridBagConstraints4.ipadx = 0;
			gridBagConstraints4.insets = new java.awt.Insets(0, 5, 5, 5);
			gridBagConstraints4.gridwidth = 2;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints4.gridy = 10;
			contents = new JPanel();
			contents.setLayout(new GridBagLayout());
			contents.add(getCompileButtonPanel(), gridBagConstraints4);
			contents.add(getBasicOptionPanel(), gridBagConstraints);
			contents.add(getPackPanel(), gridBagConstraints2);
			contents.add(getBorderPanel(), gridBagConstraints11);
			contents.add(getJPanel(), gridBagConstraints21);
			contents.add(getLogoPanel(), gridBagConstraints31);
			contents.add(getHtmlPanel(), gridBagConstraints12);
			contents.add(getJarPanel(), gridBagConstraints22);
			contents.add(getTemplatePanel(), gridBagConstraints32);
			contents.add(getToolBar(), gridBagConstraints41);
			contents.add(getToolBarBorderPanel(), gridBagConstraints5);
			contents.add(getStartClassPanel(), gridBagConstraints6);
		}
		return contents;
	}

	/**
	 * This method initializes secondLabel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCompileButtonPanel() {

		if (compileButtonPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.RIGHT);
			compileButtonPanel = new JPanel();
			compileButtonPanel.setPreferredSize(new java.awt.Dimension(200, 36));
			compileButtonPanel.setLayout(flowLayout);
			compileButtonPanel.add(getCompileButton(), null);
			compileButtonPanel.add(getRunButton(), null);
		}
		return compileButtonPanel;
	}

	/**
	 * This method initializes compileButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCompileButton() {

		if (compileButton == null) {
			compileButton = new JButton();
			compileButton.setText("コンパイル");
			compileButton.setEnabled(true);
			compileButton.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(java.awt.event.ActionEvent e) {

					compile();
				}
			});
		}
		return compileButton;
	}

}
