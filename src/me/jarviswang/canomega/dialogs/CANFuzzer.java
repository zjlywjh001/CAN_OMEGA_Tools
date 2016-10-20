package me.jarviswang.canomega.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import me.jarviswang.canomega.commons.CommonUtils;
import me.jarviswang.canomega.commons.CommonUtils.CANProtos;
import me.jarviswang.canomega.commons.FuzzMessageListener;
import me.jarviswang.canomega.frames.MainFrame;
import me.jarviswang.canomega.models.FuzzMessage;
import me.jarviswang.canomega.models.LogMessage.MessageType;
import me.jarviswang.canomega.protocols.CANProtocols;

import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CANFuzzer extends JDialog implements KeyListener,ItemListener,FuzzMessageListener {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtId;
	private JTextField tffromData7;
	private JTextField tffromData6;
	private JTextField tffromData5;
	private JTextField tffromData4;
	private JTextField tffromData3;
	private JTextField tffromData2;
	private JTextField tffromData1;
	private JTextField tffromData0;
	private JLabel lblTo;
	private JTextField tftoData7;
	private JTextField tftoData6;
	private JTextField tftoData5;
	private JTextField tftoData4;
	private JTextField tftoData3;
	private JTextField tftoData2;
	private JTextField tftoData1;
	private JTextField tftoData0;
	private JCheckBox chckbxUntil;
	private JTextField tfUntil7;
	private JTextField tfUntil6;
	private JTextField tfUntil5;
	private JTextField tfUntil4;
	private JTextField tfUntil3;
	private JTextField tfUntil2;
	private JTextField tfUntil1;
	private JTextField tfUntil0;
	private JComboBox cbOrder;
	private JLabel lblOrder;
	private JLabel lblCurrent;
	private JTextField txtProcess;
	private JLabel lblPeriodus;
	private JTextField tfPeriod;
	private CANProtocols CANObj;
	private JButton okButton;
	private JButton cancelButton;
	private JSpinner spinDLC;
	private JCheckBox um7;
	private JCheckBox um6;
	private JCheckBox um5;
	private JCheckBox um4;
	private JCheckBox um3;
	private JCheckBox um2;
	private JCheckBox um1;
	private JCheckBox um0;
	private JButton btnPause;
	private Object parentwindow;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			CANFuzzer dialog = new CANFuzzer(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public CANFuzzer(Object Parent) {
		setResizable(false);
		
		if (Parent!=null) {
			this.parentwindow = Parent;
		}
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setTitle("CAN Fuzzer");
		setBounds(100, 100, 589, 354);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(21dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(23dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(20dlu;pref)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(20dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(20dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(20dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(20dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(20dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(20dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(20dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(25dlu;default)"),
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
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(14dlu;default)"),}));
		{
			JLabel lblId = new JLabel("ID:");
			contentPanel.add(lblId, "4, 4, right, default");
		}
		{
			txtId = new JTextField();
			txtId.setText("001");
			txtId.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					String msgId = txtId.getText();
					if (CANFuzzer.this.CANObj.getCurrentProto() == CommonUtils.CANProtos.CAN250Kbps_11bits
							|| CANFuzzer.this.CANObj.getCurrentProto() == CommonUtils.CANProtos.CAN500Kbps_11bits) {
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
					CANProtos proto = CANFuzzer.this.CANObj.getCurrentProto();
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
			contentPanel.add(txtId, "6, 4, 3, 1, fill, default");
			txtId.setColumns(10);
		}
		{
			JLabel lblDlc = new JLabel("DLC:");
			contentPanel.add(lblDlc, "10, 4, center, default");
		}
		{
			
			SpinnerModel model = new SpinnerNumberModel(new Byte((byte) 8), new Byte((byte) 0), new Byte((byte) 8), new Byte((byte) 1));
			spinDLC = new JSpinner(model);
			spinDLC.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					int spinvalue = ((Byte)spinDLC.getValue()).byteValue();
					switch (spinvalue) {
					case 0:
						tffromData7.setEnabled(false);
						tffromData6.setEnabled(false);
						tffromData5.setEnabled(false);
						tffromData4.setEnabled(false);
						tffromData3.setEnabled(false);
						tffromData2.setEnabled(false);
						tffromData1.setEnabled(false);
						tffromData0.setEnabled(false);
						tftoData7.setEnabled(false);
						tftoData6.setEnabled(false);
						tftoData5.setEnabled(false);
						tftoData4.setEnabled(false);
						tftoData3.setEnabled(false);
						tftoData2.setEnabled(false);
						tftoData1.setEnabled(false);
						tftoData0.setEnabled(false);
						break;
					case 1:
						tffromData7.setEnabled(true);
						tffromData6.setEnabled(false);
						tffromData5.setEnabled(false);
						tffromData4.setEnabled(false);
						tffromData3.setEnabled(false);
						tffromData2.setEnabled(false);
						tffromData1.setEnabled(false);
						tffromData0.setEnabled(false);
						tftoData7.setEnabled(true);
						tftoData6.setEnabled(false);
						tftoData5.setEnabled(false);
						tftoData4.setEnabled(false);
						tftoData3.setEnabled(false);
						tftoData2.setEnabled(false);
						tftoData1.setEnabled(false);
						tftoData0.setEnabled(false);
						break;
					case 2:
						tffromData7.setEnabled(true);
						tffromData6.setEnabled(true);
						tffromData5.setEnabled(false);
						tffromData4.setEnabled(false);
						tffromData3.setEnabled(false);
						tffromData2.setEnabled(false);
						tffromData1.setEnabled(false);
						tffromData0.setEnabled(false);
						tftoData7.setEnabled(true);
						tftoData6.setEnabled(true);
						tftoData5.setEnabled(false);
						tftoData4.setEnabled(false);
						tftoData3.setEnabled(false);
						tftoData2.setEnabled(false);
						tftoData1.setEnabled(false);
						tftoData0.setEnabled(false);
						break;
					case 3:
						tffromData7.setEnabled(true);
						tffromData6.setEnabled(true);
						tffromData5.setEnabled(true);
						tffromData4.setEnabled(false);
						tffromData3.setEnabled(false);
						tffromData2.setEnabled(false);
						tffromData1.setEnabled(false);
						tffromData0.setEnabled(false);
						tftoData7.setEnabled(true);
						tftoData6.setEnabled(true);
						tftoData5.setEnabled(true);
						tftoData4.setEnabled(false);
						tftoData3.setEnabled(false);
						tftoData2.setEnabled(false);
						tftoData1.setEnabled(false);
						tftoData0.setEnabled(false);
						break;
					case 4:
						tffromData7.setEnabled(true);
						tffromData6.setEnabled(true);
						tffromData5.setEnabled(true);
						tffromData4.setEnabled(true);
						tffromData3.setEnabled(false);
						tffromData2.setEnabled(false);
						tffromData1.setEnabled(false);
						tffromData0.setEnabled(false);
						tftoData7.setEnabled(true);
						tftoData6.setEnabled(true);
						tftoData5.setEnabled(true);
						tftoData4.setEnabled(true);
						tftoData3.setEnabled(false);
						tftoData2.setEnabled(false);
						tftoData1.setEnabled(false);
						tftoData0.setEnabled(false);
						break;
					case 5:
						tffromData7.setEnabled(true);
						tffromData6.setEnabled(true);
						tffromData5.setEnabled(true);
						tffromData4.setEnabled(true);
						tffromData3.setEnabled(true);
						tffromData2.setEnabled(false);
						tffromData1.setEnabled(false);
						tffromData0.setEnabled(false);
						tftoData7.setEnabled(true);
						tftoData6.setEnabled(true);
						tftoData5.setEnabled(true);
						tftoData4.setEnabled(true);
						tftoData3.setEnabled(true);
						tftoData2.setEnabled(false);
						tftoData1.setEnabled(false);
						tftoData0.setEnabled(false);
						break;
					case 6:
						tffromData7.setEnabled(true);
						tffromData6.setEnabled(true);
						tffromData5.setEnabled(true);
						tffromData4.setEnabled(true);
						tffromData3.setEnabled(true);
						tffromData2.setEnabled(true);
						tffromData1.setEnabled(false);
						tffromData0.setEnabled(false);
						tftoData7.setEnabled(true);
						tftoData6.setEnabled(true);
						tftoData5.setEnabled(true);
						tftoData4.setEnabled(true);
						tftoData3.setEnabled(true);
						tftoData2.setEnabled(true);
						tftoData1.setEnabled(false);
						tftoData0.setEnabled(false);
						break;
					case 7:
						tffromData7.setEnabled(true);
						tffromData6.setEnabled(true);
						tffromData5.setEnabled(true);
						tffromData4.setEnabled(true);
						tffromData3.setEnabled(true);
						tffromData2.setEnabled(true);
						tffromData1.setEnabled(true);
						tffromData0.setEnabled(false);
						tftoData7.setEnabled(true);
						tftoData6.setEnabled(true);
						tftoData5.setEnabled(true);
						tftoData4.setEnabled(true);
						tftoData3.setEnabled(true);
						tftoData2.setEnabled(true);
						tftoData1.setEnabled(true);
						tftoData0.setEnabled(false);
						break;
					case 8:
						tffromData7.setEnabled(true);
						tffromData6.setEnabled(true);
						tffromData5.setEnabled(true);
						tffromData4.setEnabled(true);
						tffromData3.setEnabled(true);
						tffromData2.setEnabled(true);
						tffromData1.setEnabled(true);
						tffromData0.setEnabled(true);
						tftoData7.setEnabled(true);
						tftoData6.setEnabled(true);
						tftoData5.setEnabled(true);
						tftoData4.setEnabled(true);
						tftoData3.setEnabled(true);
						tftoData2.setEnabled(true);
						tftoData1.setEnabled(true);
						tftoData0.setEnabled(true);
						break;
					}
				}
			});
			JFormattedTextField spintextField = ((JSpinner.NumberEditor) spinDLC.getEditor()).getTextField();
			spintextField.setEditable(false);
			contentPanel.add(spinDLC, "12, 4, 3, 1");
		}
		{
			lblOrder = new JLabel("Fuzz Order:");
			contentPanel.add(lblOrder, "16, 4, 3, 1, right, default");
		}
		{
			cbOrder = new JComboBox();
			cbOrder.setModel(new DefaultComboBoxModel(CommonUtils.FuzzOrder.values()));
			contentPanel.add(cbOrder, "19, 4, 6, 1, fill, default");
		}
		{
			JLabel lblFuzzing = new JLabel("Fuzzing");
			contentPanel.add(lblFuzzing, "4, 6");
		}
		{
			JLabel lblFuzzingParameters = new JLabel("Parameters:");
			contentPanel.add(lblFuzzingParameters, "6, 6, 3, 1");
		}
		{
			JLabel lblFrom = new JLabel("From:");
			contentPanel.add(lblFrom, "6, 10, 3, 1, right, default");
		}
		{
			tffromData7 = new JTextField();
			
			tffromData7.addKeyListener(this);
			
			contentPanel.add(tffromData7, "10, 10, fill, default");
			tffromData7.setColumns(2);
		}
		{
			tffromData6 = new JTextField();
			tffromData6.addKeyListener(this);
			contentPanel.add(tffromData6, "12, 10, fill, default");
			tffromData6.setColumns(2);
		}
		{
			tffromData5 = new JTextField();
			tffromData5.addKeyListener(this);
			contentPanel.add(tffromData5, "14, 10, fill, default");
			tffromData5.setColumns(2);
		}
		{
			tffromData4 = new JTextField();
			tffromData4.addKeyListener(this);
			contentPanel.add(tffromData4, "16, 10, fill, default");
			tffromData4.setColumns(2);
		}
		{
			tffromData3 = new JTextField();
			tffromData3.addKeyListener(this);
			contentPanel.add(tffromData3, "18, 10, fill, default");
			tffromData3.setColumns(2);
		}
		{
			tffromData2 = new JTextField();
			tffromData2.addKeyListener(this);
			contentPanel.add(tffromData2, "20, 10, fill, default");
			tffromData2.setColumns(2);
		}
		{
			tffromData1 = new JTextField();
			tffromData1.addKeyListener(this);
			contentPanel.add(tffromData1, "22, 10, fill, default");
			tffromData1.setColumns(2);
		}
		{
			tffromData0 = new JTextField();
			tffromData0.addKeyListener(this);
			contentPanel.add(tffromData0, "24, 10, fill, default");
			tffromData0.setColumns(2);
		}
		{
			lblTo = new JLabel("To:");
			contentPanel.add(lblTo, "6, 14, 3, 1, right, default");
		}
		{
			tftoData7 = new JTextField();
			tftoData7.addKeyListener(this);
			tftoData7.setColumns(2);
			contentPanel.add(tftoData7, "10, 14, fill, default");
		}
		{
			tftoData6 = new JTextField();
			tftoData6.addKeyListener(this);
			tftoData6.setColumns(2);
			contentPanel.add(tftoData6, "12, 14, fill, default");
		}
		{
			tftoData5 = new JTextField();
			tftoData5.addKeyListener(this);
			tftoData5.setColumns(2);
			contentPanel.add(tftoData5, "14, 14, fill, default");
		}
		{
			tftoData4 = new JTextField();
			tftoData4.addKeyListener(this);
			tftoData4.setColumns(2);
			contentPanel.add(tftoData4, "16, 14, fill, default");
		}
		{
			tftoData3 = new JTextField();
			tftoData3.addKeyListener(this);
			tftoData3.setColumns(2);
			contentPanel.add(tftoData3, "18, 14, fill, default");
		}
		{
			tftoData2 = new JTextField();
			tftoData2.addKeyListener(this);
			tftoData2.setColumns(2);
			contentPanel.add(tftoData2, "20, 14, fill, default");
		}
		{
			tftoData1 = new JTextField();
			tftoData1.addKeyListener(this);
			tftoData1.setColumns(2);
			contentPanel.add(tftoData1, "22, 14, fill, default");
		}
		{
			tftoData0 = new JTextField();
			tftoData0.addKeyListener(this);
			tftoData0.setColumns(2);
			contentPanel.add(tftoData0, "24, 14, fill, default");
		}
		{
			chckbxUntil = new JCheckBox("Stop on Receive:");
			chckbxUntil.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange()==ItemEvent.SELECTED) {
						um7.setEnabled(true);
						um6.setEnabled(true);
						um5.setEnabled(true);
						um4.setEnabled(true);
						um3.setEnabled(true);
						um2.setEnabled(true);
						um1.setEnabled(true);
						um0.setEnabled(true);
						if (um7.isSelected()) {
							tfUntil7.setEnabled(true);
						} else {
							tfUntil7.setEnabled(false);
						}
						if (um6.isSelected()) {
							tfUntil6.setEnabled(true);
						} else {
							tfUntil6.setEnabled(false);
						}
						if (um5.isSelected()) {
							tfUntil5.setEnabled(true);
						} else {
							tfUntil5.setEnabled(false);
						}
						if (um4.isSelected()) {
							tfUntil4.setEnabled(true);
						} else {
							tfUntil4.setEnabled(false);
						}
						if (um3.isSelected()) {
							tfUntil3.setEnabled(true);
						} else {
							tfUntil3.setEnabled(false);
						}
						if (um2.isSelected()) {
							tfUntil2.setEnabled(true);
						} else {
							tfUntil2.setEnabled(false);
						}
						if (um1.isSelected()) {
							tfUntil1.setEnabled(true);
						} else {
							tfUntil1.setEnabled(false);
						}
						if (um0.isSelected()) {
							tfUntil0.setEnabled(true);
						} else {
							tfUntil0.setEnabled(false);
						}
					}
					if (e.getStateChange()==ItemEvent.DESELECTED) {
						tfUntil7.setEnabled(false);
						tfUntil6.setEnabled(false);
						tfUntil5.setEnabled(false);
						tfUntil4.setEnabled(false);
						tfUntil3.setEnabled(false);
						tfUntil2.setEnabled(false);
						tfUntil1.setEnabled(false);
						tfUntil0.setEnabled(false);
						um7.setEnabled(false);
						um6.setEnabled(false);
						um5.setEnabled(false);
						um4.setEnabled(false);
						um3.setEnabled(false);
						um2.setEnabled(false);
						um1.setEnabled(false);
						um0.setEnabled(false);
						
					}
				}
			});
			chckbxUntil.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseMoved(MouseEvent e) {
					chckbxUntil.setToolTipText("Stop fuzzing when a received packet match this pattern.");
				}
			});
			contentPanel.add(chckbxUntil, "3, 18, 6, 1, right, default");
		}
		{
			tfUntil7 = new JTextField();
			tfUntil7.addKeyListener(this);
			tfUntil7.setEnabled(false);
			tfUntil7.setColumns(2);
			contentPanel.add(tfUntil7, "10, 18, fill, default");
		}
		{
			tfUntil6 = new JTextField();
			tfUntil6.addKeyListener(this);
			tfUntil6.setEnabled(false);
			tfUntil6.setColumns(2);
			contentPanel.add(tfUntil6, "12, 18, fill, default");
		}
		{
			tfUntil5 = new JTextField();
			tfUntil5.addKeyListener(this);
			tfUntil5.setEnabled(false);
			tfUntil5.setColumns(2);
			contentPanel.add(tfUntil5, "14, 18, fill, default");
		}
		{
			tfUntil4 = new JTextField();
			tfUntil4.addKeyListener(this);
			tfUntil4.setEnabled(false);
			tfUntil4.setColumns(2);
			contentPanel.add(tfUntil4, "16, 18, fill, default");
		}
		{
			tfUntil3 = new JTextField();
			tfUntil3.addKeyListener(this);
			tfUntil3.setEnabled(false);
			tfUntil3.setColumns(2);
			contentPanel.add(tfUntil3, "18, 18, fill, default");
		}
		{
			tfUntil2 = new JTextField();
			tfUntil2.addKeyListener(this);
			tfUntil2.setEnabled(false);
			tfUntil2.setColumns(2);
			contentPanel.add(tfUntil2, "20, 18, fill, default");
		}
		{
			tfUntil1 = new JTextField();
			tfUntil1.addKeyListener(this);
			tfUntil1.setEnabled(false);
			tfUntil1.setColumns(2);
			contentPanel.add(tfUntil1, "22, 18, fill, default");
		}
		{
			tfUntil0 = new JTextField();
			tfUntil0.addKeyListener(this);
			tfUntil0.setEnabled(false);
			tfUntil0.setColumns(2);
			contentPanel.add(tfUntil0, "24, 18, fill, default");
		}
		{
			um7 = new JCheckBox("");
			um7.addItemListener(this);
			um7.setEnabled(false);
			um7.setSelected(true);
			contentPanel.add(um7, "10, 20, center, default");
		}
		{
			um6 = new JCheckBox("");
			um6.addItemListener(this);
			um6.setEnabled(false);
			um6.setSelected(true);
			contentPanel.add(um6, "12, 20, center, default");
		}
		{
			um5 = new JCheckBox("");
			um5.addItemListener(this);
			um5.setEnabled(false);
			um5.setSelected(true);
			contentPanel.add(um5, "14, 20, center, default");
		}
		{
			um4 = new JCheckBox("");
			um4.addItemListener(this);
			um4.setEnabled(false);
			um4.setSelected(true);
			contentPanel.add(um4, "16, 20, center, default");
		}
		{
			um3 = new JCheckBox("");
			um3.addItemListener(this);
			um3.setEnabled(false);
			um3.setSelected(true);
			contentPanel.add(um3, "18, 20, center, default");
		}
		{
			um2 = new JCheckBox("");
			um2.addItemListener(this);
			um2.setEnabled(false);
			um2.setSelected(true);
			contentPanel.add(um2, "20, 20, center, default");
		}
		{
			um1 = new JCheckBox("");
			um1.addItemListener(this);
			um1.setEnabled(false);
			um1.setSelected(true);
			contentPanel.add(um1, "22, 20, center, default");
		}
		{
			um0 = new JCheckBox("");
			um0.addItemListener(this);
			um0.setEnabled(false);
			um0.setSelected(true);
			contentPanel.add(um0, "24, 20, center, default");
		}
		{
			lblCurrent = new JLabel("Current Process:");
			contentPanel.add(lblCurrent, "6, 22, 3, 1, right, default");
		}
		{
			txtProcess = new JTextField();
			txtProcess.setEditable(false);
			contentPanel.add(txtProcess, "10, 22, 8, 1, fill, default");
			txtProcess.setColumns(10);
		}
		{
			lblPeriodus = new JLabel("Period (us):");
			contentPanel.add(lblPeriodus, "18, 22, 4, 1, center, default");
		}
		{
			tfPeriod = new JTextField();
			tfPeriod.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					int inputkey = e.getKeyChar();
					if (inputkey == 10 || inputkey == 13) {
						e.consume();
					} else {
						if ((inputkey<48) || (inputkey >57)) {
							e.consume();
						}
					}
				}
			});
			tfPeriod.setText("100000");
			contentPanel.add(tfPeriod, "22, 22, 3, 1, fill, default");
			tfPeriod.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnPause = new JButton("Pause");
				btnPause.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (btnPause.getText().equals("Pause")) {
							if (CANFuzzer.this.CANObj.PauseFuzzing()==0) {
								btnPause.setText("Resume");
							} else {
								JOptionPane.showMessageDialog(CANFuzzer.this, "Pause Fuzzing Failed!","Error", JOptionPane.ERROR_MESSAGE);
							}
						} else {
							if (CANFuzzer.this.CANObj.ResumeFuzzing()==0) {
								btnPause.setText("Pause");
							} else {
								JOptionPane.showMessageDialog(CANFuzzer.this, "Resume Fuzzing Failed!","Error", JOptionPane.ERROR_MESSAGE);
							}
							
						}
					}
				});
				btnPause.setEnabled(false);
				buttonPane.add(btnPause);
			}
			{
				okButton = new JButton("Start");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (okButton.getText().equals("Start")) {
							if (CANFuzzer.this.CANObj !=null && CommonUtils.state!=0) {
								okButton.setText("Stop");
								btnPause.setEnabled(true);
								CANFuzzer.this.setFuzzConfig();
							} else {
								JOptionPane.showMessageDialog(CANFuzzer.this, "Connect state error.","Error", JOptionPane.ERROR_MESSAGE);
							}
						} else {
							if (CANFuzzer.this.CANObj.StopFuzzing()==0) {
								okButton.setText("Start");
								btnPause.setText("Pause");
								btnPause.setEnabled(false);
							} else {
								JOptionPane.showMessageDialog(CANFuzzer.this, "Stop Fuzzing Failed!","Error", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Close");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	
	public void setCANObj(CANProtocols obj) {
		this.CANObj = obj;
	}
	
	public JTextField gettfFuzzId()
	{
		return this.txtId;
		
	}
	
	public JButton getCloseButton()
	{
		return this.cancelButton;
	}
	
	private int setFuzzConfig() {
		StringBuilder sb = new StringBuilder();
		sb.append('f');
		String msgId = this.txtId.getText();
		if (this.CANObj.getCurrentProto() == CommonUtils.CANProtos.CAN250Kbps_11bits
				|| this.CANObj.getCurrentProto() == CommonUtils.CANProtos.CAN500Kbps_11bits) {
			sb.append('d');
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
			sb.append('D');
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
		sb.append(msgId);
		byte DLC = ((Byte)this.spinDLC.getValue()).byteValue();
		sb.append(DLC);	
		if (this.chckbxUntil.isSelected()) {
			sb.append('s');
		} else {
			sb.append('S');
		}
		String fromdata7 = this.tffromData7 .getText();
		String fromdata6 = this.tffromData6.getText();
		String fromdata5 = this.tffromData5.getText();
		String fromdata4 = this.tffromData4.getText();
		String fromdata3 = this.tffromData3.getText();
		String fromdata2 = this.tffromData2.getText();
		String fromdata1 = this.tffromData1.getText();
		String fromdata0 = this.tffromData0.getText();
		String[] dataFrom = new String[]{fromdata7,fromdata6,fromdata5,fromdata4,fromdata3,fromdata2,fromdata1,fromdata0};
		String todata7 = this.tftoData7.getText();
		String todata6 = this.tftoData6.getText();
		String todata5 = this.tftoData5.getText();
		String todata4 = this.tftoData4.getText();
		String todata3 = this.tftoData3.getText();
		String todata2 = this.tftoData2.getText();
		String todata1 = this.tftoData1.getText();
		String todata0 = this.tftoData0.getText();
		String[] dataTo = new String[]{todata7,todata6,todata5,todata4,todata3,todata2,todata1,todata0};
		for (int i = 0; i < DLC; i++) {
			if (dataFrom[i].length()==0) {
				dataFrom[i] = "0";
			}
			if (dataFrom[i].length()<2) {
				sb.append("0" + dataFrom[i]);
			} else {
				sb.append(dataFrom[i]);
			}
		}
		for (int i = 0; i < DLC; i++) {
			if (dataTo[i].length()==0) {
				dataTo[i] = "0";
			}
			if (dataTo[i].length()<2) {
				sb.append("0" + dataTo[i]);
			} else {
				sb.append(dataTo[i]);
			}
		}
		if (this.cbOrder.getSelectedItem()==CommonUtils.FuzzOrder.Left_Byte_First) {
			sb.append('g');
		} else {
			sb.append('G');
		}
		sb.append('p');
		try {
			int period = Integer.parseInt(this.tfPeriod.getText());
			sb.append(String.format("%X", period));
		} catch (StringIndexOutOfBoundsException e1) {
			return -1;
		} catch (NumberFormatException e2) {
			return -1;
		}
		if (this.chckbxUntil.isSelected()) {
			sb.append('u');
			String untildata7 = this.tfUntil7.getText();
			String untildata6 = this.tfUntil6.getText();
			String untildata5 = this.tfUntil5.getText();
			String untildata4 = this.tfUntil4.getText();
			String untildata3 = this.tfUntil3.getText();
			String untildata2 = this.tfUntil2.getText();
			String untildata1 = this.tfUntil1.getText();
			String untildata0 = this.tfUntil0.getText();
			String[] dataUntil = new String[]{untildata7,untildata6,untildata5,untildata4,untildata3,untildata2,untildata1,untildata0};
			for (int i = 0; i < 8; i++) {
				if (dataUntil[i].length()==0) {
					dataUntil[i] = "0";
				}
				if (dataUntil[i].length()<2) {
					sb.append("0" + dataUntil[i]);
				} else {
					sb.append(dataUntil[i]);
				}
			}
			sb.append('m');
			int mask = 0;
			if (this.um7.isSelected()) {
				mask |= (1<<0);
			}
			if (this.um6.isSelected()) {
				mask |= (1<<1);
			}
			if (this.um5.isSelected()) {
				mask |= (1<<2);
			}
			if (this.um4.isSelected()) {
				mask |= (1<<3);
			}
			if (this.um3.isSelected()) {
				mask |= (1<<4);
			}
			if (this.um2.isSelected()) {
				mask |= (1<<5);
			}
			if (this.um1.isSelected()) {
				mask |= (1<<6);
			}
			if (this.um0.isSelected()) {
				mask |= (1<<7);
			}
			
			sb.append(String.format("%02X", mask));
			
		}
		
		String fuzzConfig = sb.toString();
		System.out.println("fuzzing config:"+fuzzConfig);
		((MainFrame)this.parentwindow).log("Fuzzing Start...", MessageType.INFO);
		this.CANObj.setFuzzer(fuzzConfig);
		return 0;
	}
	
	public void settxtId(String strId) {
		this.txtId.setText(strId);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		int inputkey = e.getKeyChar();
		if (((JTextField)e.getComponent()).getText().length()>=2 && inputkey!=8) {
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

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange()==ItemEvent.SELECTED && this.chckbxUntil.isSelected())
		{
			if ((JCheckBox)e.getItem()==this.um7)
			{
				tfUntil7.setEnabled(true);
			} else if ((JCheckBox)e.getItem()==this.um6) {
				tfUntil6.setEnabled(true);
			} else if ((JCheckBox)e.getItem()==this.um5) {
				tfUntil5.setEnabled(true);
			} else if ((JCheckBox)e.getItem()==this.um4) {
				tfUntil4.setEnabled(true);
			} else if ((JCheckBox)e.getItem()==this.um3) {
				tfUntil3.setEnabled(true);
			} else if ((JCheckBox)e.getItem()==this.um2) {
				tfUntil2.setEnabled(true);
			} else if ((JCheckBox)e.getItem()==this.um1) {
				tfUntil1.setEnabled(true);
			} else if ((JCheckBox)e.getItem()==this.um0) {
				tfUntil0.setEnabled(true);
			}
		}
		if (e.getStateChange()==ItemEvent.DESELECTED && this.chckbxUntil.isSelected()) {
			if ((JCheckBox)e.getItem()==this.um7)
			{
				tfUntil7.setEnabled(false);
			} else if ((JCheckBox)e.getItem()==this.um6) {
				tfUntil6.setEnabled(false);
			} else if ((JCheckBox)e.getItem()==this.um5) {
				tfUntil5.setEnabled(false);
			} else if ((JCheckBox)e.getItem()==this.um4) {
				tfUntil4.setEnabled(false);
			} else if ((JCheckBox)e.getItem()==this.um3) {
				tfUntil3.setEnabled(false);
			} else if ((JCheckBox)e.getItem()==this.um2) {
				tfUntil2.setEnabled(false);
			} else if ((JCheckBox)e.getItem()==this.um1) {
				tfUntil1.setEnabled(false);
			} else if ((JCheckBox)e.getItem()==this.um0) {
				tfUntil0.setEnabled(false);
			}
		}
		
	}

	@Override
	public void receiveFuzzMessage(FuzzMessage msg) {
		String currentps = "";
		byte[] currentdata = msg.getData();
		for (int i=0;i<currentdata.length;i++) {
			currentps = currentps.concat(String.format("%02X", currentdata[i]));
		}
		this.txtProcess.setText(currentps);
	}

	@Override
	public void finishFuzz() {
		this.btnPause.setText("Pause");
		this.okButton.setText("Start");
		this.btnPause.setEnabled(false);
		((MainFrame)this.parentwindow).log("Fuzzing Finished...", MessageType.INFO);
	}
	
	

}
