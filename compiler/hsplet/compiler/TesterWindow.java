package hsplet.compiler;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TesterWindow extends JFrame {

	private JPanel jContentPane = null;

	private JLabel messageLabel = null;

	private JLabel messageLabel2 = null;

	private JButton stopButton = null;

	/**
	 * This is the default constructor
	 */
	public TesterWindow() {
		super();
		initialize();

		pack();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setTitle("HSPLet テストサーバ");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new java.awt.Insets(40,0,20,0);
			gridBagConstraints2.gridy = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new java.awt.Insets(0, 20, 0, 20);
			gridBagConstraints1.gridy = 1;
			messageLabel2 = new JLabel();
			messageLabel2.setText("テストが終了したら停止ボタンを押してください。");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new java.awt.Insets(20, 20, 0, 20);
			gridBagConstraints.gridy = 0;
			messageLabel = new JLabel();
			messageLabel.setText("HSPLet のテストサーバが稼動中です。");
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(messageLabel, gridBagConstraints);
			jContentPane.add(messageLabel2, gridBagConstraints1);
			jContentPane.add(getStopButton(), gridBagConstraints2);
		}
		return jContentPane;
	}

	/**
	 * This method initializes stopButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getStopButton() {
		if (stopButton == null) {
			stopButton = new JButton();
			stopButton.setText("停止");
			stopButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.exit(0);
				}
			});
		}
		return stopButton;
	}

}
