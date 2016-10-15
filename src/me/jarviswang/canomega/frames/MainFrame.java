package me.jarviswang.canomega.frames;

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
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.JButton;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import jssc.SerialPortException;
import jssc.SerialPortList;
import me.jarviswang.canomega.commons.CANMessageListener;
import me.jarviswang.canomega.commons.CommonUtils;
import me.jarviswang.canomega.commons.CommonUtils.CANProtos;
import me.jarviswang.canomega.commons.CommonUtils.OpenMode;
import me.jarviswang.canomega.dialogs.AboutDialog;
import me.jarviswang.canomega.dialogs.PacketDiff;
import me.jarviswang.canomega.models.CANMessage;
import me.jarviswang.canomega.models.LogMessage;
import me.jarviswang.canomega.models.LogMessageTableModel;
import me.jarviswang.canomega.models.MonitorMessageTableModel;
import me.jarviswang.canomega.models.LogMessage.MessageType;
import me.jarviswang.canomega.protocols.CANProtocols;

import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JTabbedPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DropMode;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.StringUtils;

import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerModel;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import java.awt.FlowLayout;
import javax.swing.SpinnerNumberModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class MainFrame extends JFrame implements CANMessageListener {

	private JPanel MainPanel;
	private JTextField tFBaudRate;
	private JTable Logtable;
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
	private JSpinner spinDLC;
	private JComboBox comboProt;
	private JCheckBox chckbxRtr;
	private JButton btnSend;
	private OpenMode lastMode;
	private CANProtos lastProto;
	private JToggleButton tglbtnFollow;
	private long baseTimestamp = 0L;
	private JMenuItem mntmPacketDiffTool;
	private JMenuItem mntmCanFuzzingTool;
	private PacketDiff difftoolWindow;
	private final ArrayList<LogMessage> MonitorBuffer = new ArrayList<LogMessage>();

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
		
		mntmPacketDiffTool = new JMenuItem("Packet Diff tool");
		mntmPacketDiffTool.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (CommonUtils.state == 0) {
					JOptionPane.showMessageDialog(null, "Please Connect First.","Error", JOptionPane.ERROR_MESSAGE);
				} else {
					MainFrame.this.difftoolWindow.getCloseButton().addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							MainFrame.this.difftoolWindow.setVisible(false);
						}
						
					});
					MainFrame.this.difftoolWindow.setVisible(true);
				}
			}
		});
		mnAnalysis.add(mntmPacketDiffTool);
		
		difftoolWindow = new PacketDiff();
		difftoolWindow.getPauseButton().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!difftoolWindow.getPauseButton().isSelected()) {
					MonitorMessageTableModel mmtm = (MonitorMessageTableModel) difftoolWindow.getmonitorTable().getModel();
					for (LogMessage msg:MainFrame.this.MonitorBuffer) {
				    	mmtm.add(msg);
					}
					MainFrame.this.MonitorBuffer.clear();
				}
			}
		});
		
		
		JMenu mnAttack = new JMenu("Attack");
		menuBar.add(mnAttack);
		
		mntmCanFuzzingTool = new JMenuItem("CAN fuzzing tool");
		mntmCanFuzzingTool.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (CommonUtils.state == 0) {
					JOptionPane.showMessageDialog(null, "Please Connect First.","Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
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
		
		JLabel lblSerialPort = new JLabel("SerialPort:");
		MainPanel.add(lblSerialPort, "2, 2, left, default");
		
		JComboBox cbSerialPort = new JComboBox();
		MainPanel.add(cbSerialPort, "4, 2, fill, default");
		cbSerialPort.setModel(new DefaultComboBoxModel(SerialPortList.getPortNames()));
		
		JLabel lblBaudrate = new JLabel("BaudRate:");
		MainPanel.add(lblBaudrate, "6, 2, left, default");
		
		tFBaudRate = new JTextField();
		tFBaudRate.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int inputkey = e.getKeyChar();
				if (tFBaudRate.getText().length()>=7 && inputkey!=8) {
					e.consume();
					return ;
				}
				if (inputkey == 10 || inputkey == 13) {
					e.consume();
				} else {
					if (inputkey>57 || inputkey<48) {
						e.consume();
					}
				}
			}
		});
		tFBaudRate.setText("115200");
		tFBaudRate.setHorizontalAlignment(SwingConstants.LEFT);
		MainPanel.add(tFBaudRate, "8, 2, center, default");
		tFBaudRate.setColumns(9);
		
		JLabel lblMode = new JLabel("Mode:");
		MainPanel.add(lblMode, "10, 2, left, default");

		
		JComboBox cbMode = new JComboBox();
		lastMode = (OpenMode)cbMode.getSelectedItem();
		cbMode.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					switch (CommonUtils.state) {
					case 0: break;
					case 1:
						OpenMode newMode = (OpenMode)cbMode.getSelectedItem();
						if (newMode==lastMode) {
							return ;
						}
						if (MainFrame.this.CANObj!=null) {
							boolean res = CANObj.changeConnectMode(CANObj.getCurrentProto(),newMode);
							if (!res) {
								JOptionPane.showMessageDialog(null, "Change Connect Mode Failed.","Error", JOptionPane.ERROR_MESSAGE);
								cbMode.setSelectedItem(MainFrame.this.lastMode);
							} else {
								if (newMode == CommonUtils.OpenMode.LISTENONLY) {
									txtId.setEnabled(false);
									spinDLC.setEnabled(false);
									textData7.setEnabled(false);
									textData6.setEnabled(false);
									textData5.setEnabled(false);
									textData4.setEnabled(false);
									textData3.setEnabled(false);
									textData2.setEnabled(false);
									textData1.setEnabled(false);
									textData0.setEnabled(false);
									//comboProt.setEnabled(false);
									chckbxRtr.setEnabled(false);
									btnSend.setEnabled(false);
								} else {
									txtId.setEnabled(true);
									spinDLC.setEnabled(true);
									MainFrame.this.setFrameState();
									comboProt.setEnabled(true);
									chckbxRtr.setEnabled(true);
									btnSend.setEnabled(true);
								}
									MainFrame.this.log("Changed to "+newMode+" Mode.",  MessageType.INFO);
								lastMode = newMode;
							}
						}
						break;
					case 2:break;
					case 3:break;
					default:break;
					}
				}
			}
		});
		MainPanel.add(cbMode, "12, 2, fill, default");
		cbMode.setModel(new DefaultComboBoxModel(CommonUtils.OpenMode.values()));
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnConnect.getText().equals("Connect")) {
					String serialport = (String) cbSerialPort.getSelectedItem();
					String baudrate = tFBaudRate.getText();
					int resp = 0;
					if (!StringUtils.isNumeric(baudrate)) {
						JOptionPane.showMessageDialog(null, "Invalid Baudrate.","Error", JOptionPane.ERROR_MESSAGE);
						return ;
					}
					switch (CommonUtils.protoSelected) {
					case 0:
						if (CANObj == null) {
							CANObj = new CANProtocols();
						}
						resp = CANObj.connect(serialport, baudrate);
						if (resp==0) {
							CANProtos proto = CANObj.getDefaultProtocol();
							lastProto = proto;
							comboProt.setSelectedItem(proto);
							OpenMode mode =  (OpenMode)cbMode.getSelectedItem();
							lastMode = mode;
							CANObj.openCANChannel(proto,mode);
							btnConnect.setText("Disconnect");
							tFBaudRate.setEnabled(false);
							cbSerialPort.setEnabled(false);
							if (mode!=CommonUtils.OpenMode.LISTENONLY) {
								txtId.setEnabled(true);
								spinDLC.setEnabled(true);
								MainFrame.this.setFrameState();
								comboProt.setEnabled(true);
								chckbxRtr.setEnabled(true);
								btnSend.setEnabled(true);
							}
							CommonUtils.state = 1;
							CANObj.addMessageListener(MainFrame.this);
							MainFrame.this.log("Connected to CANOmega (FW"+CommonUtils.firmwareVersion+"/HW"+CommonUtils.hardwareVersion+
									", SN: "+CommonUtils.serialNumber+")", MessageType.INFO);
							if (MainFrame.this.baseTimestamp == 0L) {
								MainFrame.this.baseTimestamp = System.currentTimeMillis();
						    }
						}
						break;
					case 1:break;
					case 2:break;
					default:
						JOptionPane.showMessageDialog(null, "Invalid Protocol Selected.","Error", JOptionPane.ERROR_MESSAGE);
					}
					
					if (resp!=0) {
						switch (resp) {
						case 1:
							JOptionPane.showMessageDialog(null, "Port Busy.Open Port Failed.","Error", JOptionPane.ERROR_MESSAGE);
							break;
						case 2:
							JOptionPane.showMessageDialog(null, "Device no Response.Please check your SerialPort/Baudrate.","Error", JOptionPane.ERROR_MESSAGE);
							break;
						default:
							JOptionPane.showMessageDialog(null, "Unknown Error.","Error",JOptionPane.ERROR_MESSAGE);
							break;
						}
						CommonUtils.serialPort = null;
					}
				} else {
					switch (CommonUtils.protoSelected) {
					case 0:
						if(CANObj!=null) {
							CANObj.closeCANChannel();
							CANObj.disconnect();
							CANObj.removeMessageListener(MainFrame.this);
							CANObj = null;
						}
						btnConnect.setText("Connect");
						tFBaudRate.setEnabled(true);
						cbSerialPort.setEnabled(true);
						txtId.setEnabled(false);
						spinDLC.setEnabled(false);
						textData7.setEnabled(false);
						textData6.setEnabled(false);
						textData5.setEnabled(false);
						textData4.setEnabled(false);
						textData3.setEnabled(false);
						textData2.setEnabled(false);
						textData1.setEnabled(false);
						textData0.setEnabled(false);
						comboProt.setEnabled(false);
						chckbxRtr.setEnabled(false);
						btnSend.setEnabled(false);
						CommonUtils.state = 0;
						MainFrame.this.log("Disconnected.", MessageType.INFO);
						comboProt.setSelectedItem(CommonUtils.CANProtos.CAN500Kbps_11bits);
						txtId.setText("001");
						break;
					case 1: break;
					case 2: break;
					default: break;
					}
				}
			}
		});
		MainPanel.add(btnConnect, "14, 2");
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LogMessageTableModel ltm = (LogMessageTableModel) MainFrame.this.Logtable.getModel();
				MonitorMessageTableModel mmtm = (MonitorMessageTableModel) MainFrame.this.difftoolWindow.getmonitorTable().getModel();
				ltm.clear();
				mmtm.clear();
				MainFrame.this.MonitorBuffer.clear();
				MainFrame.this.baseTimestamp = System.currentTimeMillis();
			}
		});
		MainPanel.add(btnClear, "17, 2");
		
		tglbtnFollow = new JToggleButton("Follow");
		tglbtnFollow.setSelected(true);
		MainPanel.add(tglbtnFollow, "19, 2");
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int selectedPane = tabbedPane.getSelectedIndex();
				CommonUtils.protoSelected = selectedPane;
				//System.out.println("Select: "+selectedPane);
				
				
			}
		});
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
		
		Logtable = new JTable();
		Logtable.setModel(new LogMessageTableModel());
		Logtable.getColumnModel().getColumn(0).setPreferredWidth(100);
		Logtable.getColumnModel().getColumn(1).setPreferredWidth(40);
		Logtable.getColumnModel().getColumn(2).setPreferredWidth(90);
		Logtable.getColumnModel().getColumn(3).setPreferredWidth(40);
		Logtable.getColumnModel().getColumn(4).setPreferredWidth(370);
		scrollPane.setViewportView(Logtable);
		
		txtId = new JTextField();
		txtId.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String msgId = txtId.getText();
				if (MainFrame.this.lastProto == CommonUtils.CANProtos.CAN250Kbps_11bits
						|| MainFrame.this.lastProto == CommonUtils.CANProtos.CAN500Kbps_11bits) {
					while (msgId.length() < 3) {
						msgId = "0" + msgId;
					}
					if (msgId.length() > 3) {
						msgId = msgId.substring(msgId.length()-3);
					}
					if (msgId.getBytes()[0]>55) {
						msgId = "7" + msgId.substring(1);
					}
				} else {
					while (msgId.length() < 8) {
						msgId = "0" + msgId;
					}
					if (msgId.length() > 8) {
						msgId = msgId.substring(msgId.length()-8);
					}
					if (msgId.getBytes()[0]>49) {
						msgId = "1" + msgId.substring(1);
					}
				}
				txtId.setText(msgId);
			}
		});
		txtId.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int inputkey = e.getKeyChar();
				if (CANObj == null) {
					e.consume();
					return ;
				}
				CANProtos proto = CANObj.getCurrentProto();
				if ((proto == CommonUtils.CANProtos.CAN500Kbps_11bits
						|| proto == CommonUtils.CANProtos.CAN250Kbps_11bits) 
						&& txtId.getText().length()>=8 && inputkey!=8) {
					e.consume();
					return ;
				}
				if (inputkey == 10 || inputkey == 13) {
					e.consume();
				} else {
					if (!((inputkey>=48 && inputkey <=57) 
							|| (inputkey>=97 && inputkey<=102)
							|| (inputkey>=65 && inputkey<=70))) {
						e.consume();
					}
				}
			}
		});
		txtId.setEnabled(false);
		panelCAN.add(txtId, "1, 3, fill, default");
		txtId.setColumns(10);
		txtId.setText("001");
		
		
		SpinnerModel model = new SpinnerNumberModel(new Byte((byte) 8), new Byte((byte) 0), new Byte((byte) 8), new Byte((byte) 1));
		spinDLC = new JSpinner(model);
		spinDLC.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (chckbxRtr.isSelected()) {
					return ;
				}
				MainFrame.this.setFrameState();
				
			}
		});
		JFormattedTextField spintextField = ((JSpinner.NumberEditor) spinDLC.getEditor()).getTextField();
		spintextField.setEditable(false);
		spinDLC.setEnabled(false);
		
		panelCAN.add(spinDLC, "3, 3");
		
		textData7 = new JTextField();
		textData7.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int inputkey = e.getKeyChar();
				if (CANObj == null) {
					e.consume();
					return ;
				}
				if (textData7.getText().length()>=2 && inputkey!=8) {
					e.consume();
					return ;
				}
				if (inputkey == 10 || inputkey == 13) {
					e.consume();
				} else {
					if (!((inputkey>=48 && inputkey <=57) 
							|| (inputkey>=97 && inputkey<=102)
							|| (inputkey>=65 && inputkey<=70))) {
						e.consume();
					}
				}
			}
		});
		textData7.setEnabled(false);
		panelCAN.add(textData7, "5, 3, fill, default");
		textData7.setColumns(2);
		textData7.setText("11");
		
		textData6 = new JTextField();
		textData6.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int inputkey = e.getKeyChar();
				if (CANObj == null) {
					e.consume();
					return ;
				}
				if (textData6.getText().length()>=2 && inputkey!=8) {
					e.consume();
					return ;
				}
				if (inputkey == 10 || inputkey == 13) {
					e.consume();
				} else {
					if (!((inputkey>=48 && inputkey <=57) 
							|| (inputkey>=97 && inputkey<=102)
							|| (inputkey>=65 && inputkey<=70))) {
						e.consume();
					}
				}
			}
		});
		textData6.setEnabled(false);
		panelCAN.add(textData6, "7, 3, fill, default");
		textData6.setColumns(2);
		textData6.setText("22");
		
		textData5 = new JTextField();
		textData5.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int inputkey = e.getKeyChar();
				if (CANObj == null) {
					e.consume();
					return ;
				}
				if (textData5.getText().length()>=2 && inputkey!=8) {
					e.consume();
					return ;
				}
				if (inputkey == 10 || inputkey == 13) {
					e.consume();
				} else {
					if (!((inputkey>=48 && inputkey <=57) 
							|| (inputkey>=97 && inputkey<=102)
							|| (inputkey>=65 && inputkey<=70))) {
						e.consume();
					}
				}
			}
		});
		textData5.setEnabled(false);
		panelCAN.add(textData5, "9, 3, fill, default");
		textData5.setColumns(2);
		textData5.setText("33");
		
		textData4 = new JTextField();
		textData4.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int inputkey = e.getKeyChar();
				if (CANObj == null) {
					e.consume();
					return ;
				}
				if (textData4.getText().length()>=2 && inputkey!=8) {
					e.consume();
					return ;
				}
				if (inputkey == 10 || inputkey == 13) {
					e.consume();
				} else {
					if (!((inputkey>=48 && inputkey <=57) 
							|| (inputkey>=97 && inputkey<=102)
							|| (inputkey>=65 && inputkey<=70))) {
						e.consume();
					}
				}
			}
		});
		textData4.setEnabled(false);
		panelCAN.add(textData4, "11, 3, fill, default");
		textData4.setColumns(2);
		textData4.setText("44");
		
		textData3 = new JTextField();
		textData3.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int inputkey = e.getKeyChar();
				if (CANObj == null) {
					e.consume();
					return ;
				}
				if (textData3.getText().length()>=2 && inputkey!=8) {
					e.consume();
					return ;
				}
				if (inputkey == 10 || inputkey == 13) {
					e.consume();
				} else {
					if (!((inputkey>=48 && inputkey <=57) 
							|| (inputkey>=97 && inputkey<=102)
							|| (inputkey>=65 && inputkey<=70))) {
						e.consume();
					}
				}
			}
		});
		textData3.setEnabled(false);
		panelCAN.add(textData3, "13, 3, fill, default");
		textData3.setColumns(2);
		textData3.setText("55");
		
		textData2 = new JTextField();
		textData2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int inputkey = e.getKeyChar();
				if (CANObj == null) {
					e.consume();
					return ;
				}
				if (textData2.getText().length()>=2 && inputkey!=8) {
					e.consume();
					return ;
				}
				if (inputkey == 10 || inputkey == 13) {
					e.consume();
				} else {
					if (!((inputkey>=48 && inputkey <=57) 
							|| (inputkey>=97 && inputkey<=102)
							|| (inputkey>=65 && inputkey<=70))) {
						e.consume();
					}
				}
			}
		});
		textData2.setEnabled(false);
		panelCAN.add(textData2, "15, 3, fill, default");
		textData2.setColumns(2);
		textData2.setText("66");
		
		textData1 = new JTextField();
		textData1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int inputkey = e.getKeyChar();
				if (CANObj == null) {
					e.consume();
					return ;
				}
				if (textData1.getText().length()>=2 && inputkey!=8) {
					e.consume();
					return ;
				}
				if (inputkey == 10 || inputkey == 13) {
					e.consume();
				} else {
					if (!((inputkey>=48 && inputkey <=57) 
							|| (inputkey>=97 && inputkey<=102)
							|| (inputkey>=65 && inputkey<=70))) {
						e.consume();
					}
				}
			}
		});
		textData1.setEnabled(false);
		panelCAN.add(textData1, "17, 3, fill, default");
		textData1.setColumns(2);
		textData1.setText("77");
		
		textData0 = new JTextField();
		textData0.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				int inputkey = e.getKeyChar();
				if (CANObj == null) {
					e.consume();
					return ;
				}
				if (textData0.getText().length()>=2 && inputkey!=8) {
					e.consume();
					return ;
				}
				if (inputkey == 10 || inputkey == 13) {
					e.consume();
				} else {
					if (!((inputkey>=48 && inputkey <=57) 
							|| (inputkey>=97 && inputkey<=102)
							|| (inputkey>=65 && inputkey<=70))) {
						e.consume();
					}
				}
			}
		});
		textData0.setEnabled(false);
		panelCAN.add(textData0, "19, 3, fill, default");
		textData0.setColumns(2);
		textData0.setText("88");
		
		comboProt = new JComboBox();
		comboProt.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					switch (CommonUtils.state) {
					case 0: break;
					case 1:
						CANProtos newProto = (CANProtos)comboProt.getSelectedItem();
						if (newProto==lastProto) {
							return ;
						}
						if (MainFrame.this.CANObj!=null) {
							boolean res = CANObj.changeConnectMode(newProto,lastMode);
							if (!res) {
								JOptionPane.showMessageDialog(null, "Change CAN Protocol Failed.","Error", JOptionPane.ERROR_MESSAGE);
								comboProt.setSelectedItem(MainFrame.this.lastProto);
							} else {
								if (newProto == CommonUtils.CANProtos.CAN250Kbps_11bits 
										|| newProto == CommonUtils.CANProtos.CAN500Kbps_11bits) {
									String strId = txtId.getText();
									while (strId.length() < 3) {
										strId = "0" + strId;
									}
									if (strId.length() > 3) {
										strId = strId.substring(strId.length()-3);
									}
									if (strId.getBytes()[0]>55) {
										strId = "7" + strId.substring(1);
									}
									txtId.setText(strId);
								} else {
									String strId = txtId.getText();
									while (strId.length() < 8) {
										strId = "0" + strId;
									}
									if (strId.length() > 8) {
										strId = strId.substring(strId.length()-8);
									}
									if (strId.getBytes()[0]>49) {
										strId = "1" + strId.substring(1);
									}
									txtId.setText(strId);
								}
									MainFrame.this.log("Changed to "+newProto+" Protocol.",  MessageType.INFO);
									lastProto = newProto;
							}
						}
						break;
					default:break;
					}
				}
			}
		});
		comboProt.setEnabled(false);
		panelCAN.add(comboProt, "21, 3, fill, default");
		comboProt.setModel(new DefaultComboBoxModel(CommonUtils.CANProtos.values()));
		
		chckbxRtr = new JCheckBox("RTR");
		chckbxRtr.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange()==ItemEvent.SELECTED) {
					textData7.setEnabled(false);
					textData6.setEnabled(false);
					textData5.setEnabled(false);
					textData4.setEnabled(false);
					textData3.setEnabled(false);
					textData2.setEnabled(false);
					textData1.setEnabled(false);
					textData0.setEnabled(false);
				}
				if (e.getStateChange()==ItemEvent.DESELECTED) {
					MainFrame.this.setFrameState();
				}
			}
		});
		chckbxRtr.setEnabled(false);
		panelCAN.add(chckbxRtr, "23, 3");
		
		btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainFrame.this.sendButtonActionPerformed(e);
			}
		});
		btnSend.setEnabled(false);
		panelCAN.add(btnSend, "27, 3");
		
		JPanel panelKline = new JPanel();
		tabbedPane.addTab("K-Line", null, panelKline, null);
		tabbedPane.setEnabledAt(1, false);
		panelKline.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JPanel panelSAE = new JPanel();
		tabbedPane.addTab("J1850", null, panelSAE, null);
		tabbedPane.setEnabledAt(2, false);
	}
	
	public void ResetUI() {
		
	}
	
	public void log(String msg,MessageType type) {
		LogMessageTableModel ltm = (LogMessageTableModel) this.Logtable.getModel();
		ltm.addMessage(new LogMessage(null,msg,type,System.currentTimeMillis()));
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (this.tglbtnFollow.isSelected()) {
			this.Logtable.scrollRectToVisible(this.Logtable.getCellRect(ltm.getRowCount() - 1, 0, false));
		}
	}
	
	public void log(LogMessage msg)
	  {
	    LogMessageTableModel lmt = (LogMessageTableModel)this.Logtable.getModel();
	    lmt.addMessage(msg);
	    try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    if (this.tglbtnFollow.isSelected()) {
	      this.Logtable.scrollRectToVisible(this.Logtable.getCellRect(lmt.getRowCount() - 1, 0, false));
	    }
	    if ((msg.getType() == MessageType.OUT) || (msg.getType() == MessageType.IN)) {
	    	if (this.difftoolWindow.getPauseButton().isSelected()) {
	    		this.MonitorBuffer.add(msg);
	    	} else {
	    		MonitorMessageTableModel mmtm = (MonitorMessageTableModel) difftoolWindow.getmonitorTable().getModel();
		    	mmtm.add(msg);
	    	}
	    	
	    }
	  }
	
	private void sendButtonActionPerformed(ActionEvent e) {
		if (this.CANObj == null) {
			return ;
		}
		String sendString = "";
		String msgId = txtId.getText();
		boolean rtr = chckbxRtr.isSelected();
		byte DLC = ((Byte)this.spinDLC.getValue()).byteValue();
		String data7 = textData7.getText();
		String data6 = textData6.getText();
		String data5 = textData5.getText();
		String data4 = textData4.getText();
		String data3 = textData3.getText();
		String data2 = textData2.getText();
		String data1 = textData1.getText();
		String data0 = textData0.getText();
		String[] dataArray = new String[]{data7,data6,data5,data4,data3,data2,data1,data0};
		if (this.lastProto == CommonUtils.CANProtos.CAN250Kbps_11bits
				|| this.lastProto == CommonUtils.CANProtos.CAN500Kbps_11bits) {
			if (rtr) {
				sendString += "r";
			} else {
				sendString += "t";
			}
			while (msgId.length() < 3) {
				msgId = "0" + msgId;
			}
			if (msgId.length() > 3) {
				msgId = msgId.substring(msgId.length()-3);
			}
			if (msgId.getBytes()[0]>55) {
				msgId = "7" + msgId.substring(1);
			}
		} else {
			if (rtr) {
				sendString += "R";
			} else {
				sendString += "T";
			}
			while (msgId.length() < 8) {
				msgId = "0" + msgId;
			}
			if (msgId.length() > 8) {
				msgId = msgId.substring(msgId.length()-8);
			}
			if (msgId.getBytes()[0]>49) {
				msgId = "1" + msgId.substring(1);
			}
		}
		sendString += msgId;
		sendString += String.valueOf(DLC);
		if (!rtr) {
			for (int i = 0; i < DLC; i++) {
				if (dataArray[i].length()<2) {
					sendString = sendString + "0" + dataArray[i];
				} else {
					sendString += dataArray[i];
				}
			}
		}
		CANMessage msgsend = new CANMessage(sendString);
		this.log(new LogMessage(msgsend,null,MessageType.OUT,System.currentTimeMillis() - this.baseTimestamp));
		int res = this.CANObj.send(msgsend);
		System.out.println("Packet sent: "+msgsend);
		if (res!=0) {
			log("Send Message Failed!!", MessageType.ERROR);
		} 
	}
	
	private void setFrameState() {
		int spinvalue = ((Byte)spinDLC.getValue()).byteValue();
		boolean rtr = chckbxRtr.isSelected();
		if (rtr) {
			textData7.setEnabled(false);
			textData6.setEnabled(false);
			textData5.setEnabled(false);
			textData4.setEnabled(false);
			textData3.setEnabled(false);
			textData2.setEnabled(false);
			textData1.setEnabled(false);
			textData0.setEnabled(false);
			return ;
		}
		switch (spinvalue) {
		case 0:
			textData7.setEnabled(false);
			textData6.setEnabled(false);
			textData5.setEnabled(false);
			textData4.setEnabled(false);
			textData3.setEnabled(false);
			textData2.setEnabled(false);
			textData1.setEnabled(false);
			textData0.setEnabled(false);
			break;
		case 1:
			textData7.setEnabled(true);
			textData6.setEnabled(false);
			textData5.setEnabled(false);
			textData4.setEnabled(false);
			textData3.setEnabled(false);
			textData2.setEnabled(false);
			textData1.setEnabled(false);
			textData0.setEnabled(false);
			break;
		case 2:
			textData7.setEnabled(true);
			textData6.setEnabled(true);
			textData5.setEnabled(false);
			textData4.setEnabled(false);
			textData3.setEnabled(false);
			textData2.setEnabled(false);
			textData1.setEnabled(false);
			textData0.setEnabled(false);
			break;
		case 3:
			textData7.setEnabled(true);
			textData6.setEnabled(true);
			textData5.setEnabled(true);
			textData4.setEnabled(false);
			textData3.setEnabled(false);
			textData2.setEnabled(false);
			textData1.setEnabled(false);
			textData0.setEnabled(false);
			break;
		case 4:
			textData7.setEnabled(true);
			textData6.setEnabled(true);
			textData5.setEnabled(true);
			textData4.setEnabled(true);
			textData3.setEnabled(false);
			textData2.setEnabled(false);
			textData1.setEnabled(false);
			textData0.setEnabled(false);
			break;
		case 5:
			textData7.setEnabled(true);
			textData6.setEnabled(true);
			textData5.setEnabled(true);
			textData4.setEnabled(true);
			textData3.setEnabled(true);
			textData2.setEnabled(false);
			textData1.setEnabled(false);
			textData0.setEnabled(false);
			break;
		case 6:
			textData7.setEnabled(true);
			textData6.setEnabled(true);
			textData5.setEnabled(true);
			textData4.setEnabled(true);
			textData3.setEnabled(true);
			textData2.setEnabled(true);
			textData1.setEnabled(false);
			textData0.setEnabled(false);
			break;
		case 7:
			textData7.setEnabled(true);
			textData6.setEnabled(true);
			textData5.setEnabled(true);
			textData4.setEnabled(true);
			textData3.setEnabled(true);
			textData2.setEnabled(true);
			textData1.setEnabled(true);
			textData0.setEnabled(false);
			break;
		case 8:
			textData7.setEnabled(true);
			textData6.setEnabled(true);
			textData5.setEnabled(true);
			textData4.setEnabled(true);
			textData3.setEnabled(true);
			textData2.setEnabled(true);
			textData1.setEnabled(true);
			textData0.setEnabled(true);
			break;
		default:break;
		}
	}

	@Override
	public void receiveCANMessage(CANMessage msg) {
		 this.log(new LogMessage(msg, null, MessageType.IN, System.currentTimeMillis() - this.baseTimestamp));
		
	}

}
