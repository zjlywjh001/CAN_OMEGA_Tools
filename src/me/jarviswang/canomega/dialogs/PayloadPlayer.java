package me.jarviswang.canomega.dialogs;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import me.jarviswang.canomega.commons.CommonUtils;
import me.jarviswang.canomega.commons.LogFileFilter;
import me.jarviswang.canomega.models.CANMessage;
import me.jarviswang.canomega.models.LogMessage;
import me.jarviswang.canomega.models.LogMessageTableModel;
import me.jarviswang.canomega.models.LogMessage.MessageType;

import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PayloadPlayer extends JDialog {
	private JTextField textField;
	private JTextField tfSpeed;
	private JButton btnPlay;
	private JButton btnStop;
	private JCheckBox chckbxPlayToCan;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PayloadPlayer dialog = new PayloadPlayer();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public PayloadPlayer() {
		setTitle("Payload Player");
		setBounds(100, 100, 450, 189);
		getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(10dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("right:default"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(125dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel lblNewLabel = new JLabel("Log File:");
		getContentPane().add(lblNewLabel, "4, 4, right, default");
		
		textField = new JTextField();
		getContentPane().add(textField, "6, 4, 3, 1, fill, default");
		textField.setColumns(10);
		
		JButton button = new JButton("...");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc=new JFileChooser(); 
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setDialogTitle("Save Log to File...");
				LogFileFilter ff = new LogFileFilter();
				jfc.addChoosableFileFilter(ff);
				jfc.setFileFilter(ff);
				int res=jfc.showOpenDialog(PayloadPlayer.this);
				if (res == JFileChooser.APPROVE_OPTION ) {
					File targetfile = jfc.getSelectedFile();
					PayloadPlayer.this.textField.setText(targetfile.getAbsolutePath());
					
				}
			}
		});
		getContentPane().add(button, "10, 4");
		
		JLabel lblPacketsecond = new JLabel("Speed:");
		getContentPane().add(lblPacketsecond, "4, 6, right, default");
		
		tfSpeed = new JTextField();
		tfSpeed.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int inputkey = e.getKeyChar();
				if (!(inputkey>=48 && inputkey <=57)) {
					e.consume();
				}
			}
		});
		tfSpeed.setText("50");
		getContentPane().add(tfSpeed, "6, 6, 3, 1, fill, default");
		tfSpeed.setColumns(10);
		
		JLabel lblPackets = new JLabel("Packet/s");
		getContentPane().add(lblPackets, "10, 6");
		
		chckbxPlayToCan = new JCheckBox("Send to CAN");
		getContentPane().add(chckbxPlayToCan, "6, 8");
		
		btnPlay = new JButton("Start");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		getContentPane().add(btnPlay, "8, 10");
		
		btnStop = new JButton("Stop");
		getContentPane().add(btnStop, "10, 10");

	}
	
	public JButton getStartButton() {
		
		return this.btnPlay;
	}
	
	public JButton getStopButton() {
		
		return this.btnStop;
	}
	
	public boolean isPlaytoCAN() {
		return this.chckbxPlayToCan.isSelected();
	}
	
	public String getLogFilePath() {
		
		return this.textField.getText();
	}
	
	public int getSpeed() {
		int speed = 50;
		try {
			speed = Integer.parseInt(this.tfSpeed.getText());
		} catch (Exception e) {
			// ingore 
		}
		if (speed>10000) {
			speed = 10000;
		}
		return speed;
	}

}
