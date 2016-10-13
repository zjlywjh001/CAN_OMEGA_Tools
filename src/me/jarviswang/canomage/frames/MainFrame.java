package me.jarviswang.canomage.frames;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JMenuBar;
import java.awt.GridBagLayout;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import jssc.SerialPortList;
import me.jarviswang.canomega.commons.CommonUtils;
import me.jarviswang.canomega.dialogs.AboutDialog;
import me.jarviswang.canomega.protocols.CANProtocols;

import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JTabbedPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DropMode;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import java.awt.FlowLayout;
import javax.swing.SpinnerNumberModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

	private JPanel MainPanel;
	private JTextField tFBaudRate;
	private JTable table;
	private JTextField txtId;
	private JTextField textData7;
	private JTextField textData6;
	private JTextField textData5;
	private JTextField textData4;
	private JTextField textData3;
	private JTextField textData2;
	private JTextField textData1;
	private JTextField textData0;
	private CANProtocols CANObj;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.LookAndFeelInfo[] skins = UIManager.getInstalledLookAndFeels();
			for (UIManager.LookAndFeelInfo localLookAndFeelInfo:skins) {
				if ("Nimbus".equals(localLookAndFeelInfo.getName())) {
					UIManager.setLookAndFeel(localLookAndFeelInfo.getClassName());
					break;
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("CAN Omega tools v"+CommonUtils.version);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmFirmwareUpdate = new JMenuItem("Firmware Update");
		mntmFirmwareUpdate.setEnabled(false);
		mnFile.add(mntmFirmwareUpdate);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);
		
		JMenu mnAnalysis = new JMenu("Analysis");
		menuBar.add(mnAnalysis);
		
		JMenuItem mntmPacketDiffTool = new JMenuItem("Packets Diff tool");
		mnAnalysis.add(mntmPacketDiffTool);
		
		JMenu mnAttack = new JMenu("Attack");
		menuBar.add(mnAttack);
		
		JMenuItem mntmCanFuzzingTool = new JMenuItem("CAN fuzzing tool");
		mnAttack.add(mntmCanFuzzingTool);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AboutDialog ad = new AboutDialog();
				JPanel buttonPane = new JPanel();
				buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
				ad.getContentPane().add(buttonPane, BorderLayout.SOUTH);
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ad.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				ad.setVisible(true);
			}
		});
		mnHelp.add(mntmAbout);
		MainPanel = new JPanel();
		MainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(MainPanel);
		MainPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(37dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(42dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(39dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.PREF_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(24dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(62dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:default"),
				ColumnSpec.decode("max(16dlu;default)"),
				FormSpecs.GLUE_COLSPEC,
				ColumnSpec.decode("left:default"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:default"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(11dlu;default)"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("top:292dlu:grow"),}));
		
		JLabel lblSerialPort = 
				
				
				
				new JLabel("SerialPort:");
		MainPanel.add(lblSerialPort, "2, 2, left, default");
		
		JComboBox cbSerialPort = new JComboBox();
		MainPanel.add(cbSerialPort, "4, 2, fill, default");
		cbSerialPort.setModel(new DefaultComboBoxModel(SerialPortList.getPortNames()));
		
		JLabel lblBaudrate = new JLabel("BaudRate:");
		MainPanel.add(lblBaudrate, "6, 2, left, default");
		
		tFBaudRate = new JTextField();
		tFBaudRate.setText("115200");
		tFBaudRate.setHorizontalAlignment(SwingConstants.LEFT);
		MainPanel.add(tFBaudRate, "8, 2, center, default");
		tFBaudRate.setColumns(9);
		
		JLabel lblMode = new JLabel("Mode:");
		MainPanel.add(lblMode, "10, 2, left, default");
		
		JComboBox cbMode = new JComboBox();
		MainPanel.add(cbMode, "12, 2, fill, default");
		cbMode.setModel(new DefaultComboBoxModel(CommonUtils.OpenMode.values()));
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String serialport = (String) cbSerialPort.getSelectedItem();
				String baudrate = tFBaudRate.getText();
				CANObj = new CANProtocols();
				int resp = CANObj.connect(serialport, baudrate);
				if (resp==0) {
					
				} else {
					switch (resp) {
					case 1:
						JOptionPane.showMessageDialog(null, "Port Busy.Open Port Failed.","Error", JOptionPane.ERROR_MESSAGE);
						break;
					default:
						JOptionPane.showMessageDialog(null, "Unknown Error.","Error",JOptionPane.ERROR_MESSAGE);
						break;
					}
				}
			}
		});
		MainPanel.add(btnConnect, "14, 2");
		
		JButton btnClear = new JButton("Clear");
		MainPanel.add(btnClear, "17, 2");
		
		JToggleButton tglbtnFollow = new JToggleButton("Follow");
		tglbtnFollow.setSelected(true);
		MainPanel.add(tglbtnFollow, "19, 2");
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		MainPanel.add(tabbedPane, "2, 4, 18, 1");
		
		JPanel panelCAN = new JPanel();
		tabbedPane.addTab("CAN", null, panelCAN, null);
		panelCAN.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("88px"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(83dlu;default)"),
				FormSpecs.UNRELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.GLUE_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				RowSpec.decode("400px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("39px"),}));
		
		JScrollPane scrollPane = new JScrollPane();
		panelCAN.add(scrollPane, "1, 1, 27, 1, fill, fill");
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Time (ms)", "Type", "Id", "DLC", "Data"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(40);
		table.getColumnModel().getColumn(2).setPreferredWidth(90);
		table.getColumnModel().getColumn(3).setPreferredWidth(40);
		table.getColumnModel().getColumn(4).setPreferredWidth(370);
		scrollPane.setViewportView(table);
		
		txtId = new JTextField();
		txtId.setEnabled(false);
		panelCAN.add(txtId, "1, 3, fill, default");
		txtId.setColumns(10);
		txtId.setText("001");
		
		JSpinner spinDLC = new JSpinner();
		spinDLC.setEnabled(false);
		spinDLC.setModel(new SpinnerNumberModel(new Byte((byte) 8), new Byte((byte) 0), new Byte((byte) 8), new Byte((byte) 1)));
		panelCAN.add(spinDLC, "3, 3");
		
		textData7 = new JTextField();
		textData7.setEnabled(false);
		panelCAN.add(textData7, "5, 3, fill, default");
		textData7.setColumns(2);
		textData7.setText("11");
		
		textData6 = new JTextField();
		textData6.setEnabled(false);
		panelCAN.add(textData6, "7, 3, fill, default");
		textData6.setColumns(2);
		textData6.setText("22");
		
		textData5 = new JTextField();
		textData5.setEnabled(false);
		panelCAN.add(textData5, "9, 3, fill, default");
		textData5.setColumns(2);
		textData5.setText("33");
		
		textData4 = new JTextField();
		textData4.setEnabled(false);
		panelCAN.add(textData4, "11, 3, fill, default");
		textData4.setColumns(2);
		textData4.setText("44");
		
		textData3 = new JTextField();
		textData3.setEnabled(false);
		panelCAN.add(textData3, "13, 3, fill, default");
		textData3.setColumns(2);
		textData3.setText("55");
		
		textData2 = new JTextField();
		textData2.setEnabled(false);
		panelCAN.add(textData2, "15, 3, fill, default");
		textData2.setColumns(2);
		textData2.setText("66");
		
		textData1 = new JTextField();
		textData1.setEnabled(false);
		panelCAN.add(textData1, "17, 3, fill, default");
		textData1.setColumns(2);
		textData1.setText("77");
		
		textData0 = new JTextField();
		textData0.setEnabled(false);
		panelCAN.add(textData0, "19, 3, fill, default");
		textData0.setColumns(2);
		textData0.setText("88");
		
		JComboBox comboProt = new JComboBox();
		comboProt.setEnabled(false);
		panelCAN.add(comboProt, "21, 3, fill, default");
		comboProt.setModel(new DefaultComboBoxModel(CommonUtils.CANProtocols.values()));
		
		JCheckBox chckbxRtr = new JCheckBox("RTR");
		chckbxRtr.setEnabled(false);
		panelCAN.add(chckbxRtr, "23, 3");
		
		JButton btnSend = new JButton("Send");
		btnSend.setEnabled(false);
		panelCAN.add(btnSend, "27, 3");
		
		JPanel panelKline = new JPanel();
		tabbedPane.addTab("K-Line", null, panelKline, null);
		panelKline.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		tabbedPane.setEnabledAt(1, false);
		
		JPanel panelSAE = new JPanel();
		tabbedPane.addTab("J1850", null, panelSAE, null);
		tabbedPane.setEnabledAt(2, false);
	}
	
	public void ResetUI() {
		
	}

}
