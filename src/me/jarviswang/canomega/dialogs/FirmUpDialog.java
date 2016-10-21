package me.jarviswang.canomega.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import me.jarviswang.canomega.commons.CommonUtils;
import me.jarviswang.canomega.protocols.CANProtocols;

import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Dialog.ModalityType;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class FirmUpDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JProgressBar progressBar;
	private JLabel proclable;
	private int count;
	private CANProtocols CANObj;
	private JFrame Parent;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			FirmUpDialog dialog = new FirmUpDialog(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public FirmUpDialog(JFrame parent) {
		this.Parent = parent;
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setTitle("Firmware Updating");
		setBounds(100, 100, 401, 169);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		{
			progressBar = new JProgressBar();
			contentPanel.add(progressBar, "3, 3, fill, default");
		}
		
		proclable = new JLabel("0%");
		contentPanel.add(proclable, "5, 3");
	}
	
	public void setCANObj(CANProtocols obj) {
		this.CANObj = obj;
	}
	
	public void finishDialog(int errnum) {
		switch (errnum) {
		case 0:
			JOptionPane.showMessageDialog(null, "Firmware Updated successfully.Please reconnect your device.","Info", JOptionPane.INFORMATION_MESSAGE);
			break;
		case 1:
			JOptionPane.showMessageDialog(null, "Not Connected!","Error", JOptionPane.ERROR_MESSAGE);
			break;
		case 2:
			JOptionPane.showMessageDialog(null, "Firmware Update failed.Please reset your device and quickly try again.","Error", JOptionPane.ERROR_MESSAGE);
			break;
		}
		this.CANObj.ResumeEventListener();
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				FirmUpDialog.this.dispose();
			}
        }); 
	}
	
	public void doFirmwareUpdate(final byte[] data) {
		if (!CommonUtils.serialPort.isOpened()) {
			FirmUpDialog.this.finishDialog(1);
		}
		this.CANObj.PauseEventListener();
		new Thread(new Runnable() {
			@Override
			public void run() {
				count = 0;
				try {
					CommonUtils.serialPort.writeBytes("U\r".getBytes());
					Thread.sleep(100);
					CommonUtils.serialPort.writeBytes("U".getBytes());
					byte[] arrayOfByte = CommonUtils.serialPort.readBytes(1,2000);
					System.out.println(Arrays.toString(arrayOfByte));
					if (arrayOfByte[0]!=0x43) {
						FirmUpDialog.this.finishDialog(2);
					}
					byte[] frame_head = new byte[]{0x02,0x00,(byte)0xFF};
					String pdata = "update.bin"+" "+String.valueOf(data.length);
					byte[] first_body = null;
        			first_body = pdata.getBytes("ASCII");
        			int paddinglen = pdata.length();
        			byte[] pad = new byte[1024-paddinglen];
        			byte[] body_data = new byte[1024];
        			System.arraycopy(first_body, 0, body_data, 0, first_body.length);
        			System.arraycopy(pad, 0, body_data, first_body.length, pad.length);
        			int crc = CommonUtils.Cal_CRC16(body_data, body_data.length);
        			byte[] crcb = new byte[]{(byte)(crc>>8),(byte)(crc&0xFF)};
        			byte[] packet = new byte[frame_head.length+1024+2];
        			System.arraycopy(frame_head, 0, packet, 0, frame_head.length);
        			System.arraycopy(body_data, 0, packet, frame_head.length, body_data.length);
        			System.arraycopy(crcb, 0, packet, frame_head.length+body_data.length, 2);
        			
        			do {
        				CommonUtils.serialPort.writeBytes(packet);
        				System.out.println("Header sent");
        				arrayOfByte = CommonUtils.serialPort.readBytes(1,10000);
        				System.out.println(Arrays.toString(arrayOfByte));
        				if (arrayOfByte[0]==0x18) {
        					FirmUpDialog.this.finishDialog(2);
        				}
        			} while (arrayOfByte[0]!=0x06);
        			
        			arrayOfByte = CommonUtils.serialPort.readBytes(1,3000);
    				System.out.println(Arrays.toString(arrayOfByte));
    				if (arrayOfByte[0]!=0x43) {
    					FirmUpDialog.this.finishDialog(2);
    				}
        			
        			int frames = data.length/1024;
        			int now_frame = 0;
        			int i = 0;
        			arrayOfByte[0] = 0;
        			while (now_frame<frames) {
        				do {
        					if (arrayOfByte[0] == 0x15) {
        						System.out.println("Frame "+String.valueOf(now_frame+1)+ " is NAK,sent again");
        					}
        					if (arrayOfByte[0] == 0x43) {
        						System.out.println("Frame "+String.valueOf(now_frame+1)+ " timeout sent again");
        					}
        					if (arrayOfByte[0] == 0x06) {
        						System.out.println("Frame "+String.valueOf(now_frame)+ " is ACK");
        					}
        					now_frame++;
        					
        					packet = CommonUtils.Getn_Packet(data, now_frame);
        					arrayOfByte[0] = 0;
        					CommonUtils.serialPort.writeBytes(packet);
        					System.out.println("Frame "+now_frame+" sent");
        					arrayOfByte = CommonUtils.serialPort.readBytes(1,1000);
            				if (arrayOfByte[0]==0x43) {
            					if (now_frame>=1) {
            						now_frame = now_frame - 1;
            						System.out.println("Frame "+String.valueOf(now_frame+1)+ "timeout");
            					} else {
            						FirmUpDialog.this.finishDialog(2);
            					}
            				}
            				if (arrayOfByte[0]==0x15) {
            					if (now_frame>=2) {
            						now_frame = now_frame - 2;
            						System.out.println("Frame "+String.valueOf(now_frame-1)+ "is NAK");
            					} else {
            						FirmUpDialog.this.finishDialog(2);
            					}
            				}
            				if (arrayOfByte[0]==0x18) {
            					FirmUpDialog.this.finishDialog(2);
            				}
            				
            				final int updateprogress = now_frame*100/frames;
            				SwingUtilities.invokeLater(new Runnable(){
            					@Override
            					public void run() {
            						FirmUpDialog.this.progressBar.setValue(updateprogress);
            						FirmUpDialog.this.proclable.setText(updateprogress+"%");
            					}
            		        });
        				} while (arrayOfByte[0]!=0x06);
        			}
        			
        			System.out.println("send EOT..");
        			
        			do {
        				byte[] b = new byte[]{0x04};
        				CommonUtils.serialPort.writeBytes(b);
	        			arrayOfByte = CommonUtils.serialPort.readBytes(1,1000);
	        			System.out.println(Arrays.toString(arrayOfByte));
        			}  while (arrayOfByte[0]!=0x06);
        			
        			arrayOfByte = CommonUtils.serialPort.readBytes(1,3000);
    				System.out.println(Arrays.toString(arrayOfByte));
    				if (arrayOfByte[0]!=0x43) {
    					FirmUpDialog.this.finishDialog(2);
    				}
        			
        			do {
        				frame_head = new byte[]{0x02,0x00,(byte)0xFF};
	        			body_data = new byte[1024];
	        			crc = CommonUtils.Cal_CRC16(body_data, body_data.length);
	        			crcb = new byte[]{(byte)(crc>>8),(byte)(crc&0xFF)};
	        			packet = new byte[frame_head.length+1024+2];
	        			System.arraycopy(frame_head, 0, packet, 0, frame_head.length);
	        			System.arraycopy(body_data, 0, packet, frame_head.length, body_data.length);
	        			System.arraycopy(crcb, 0, packet, frame_head.length+body_data.length, 2);
	        			
	        			CommonUtils.serialPort.writeBytes(packet);
	        			arrayOfByte = CommonUtils.serialPort.readBytes(1,1000);
        			}  while (arrayOfByte[0]!=0x06);
        			
        			System.out.println("All Done.");
				} catch (SerialPortException | InterruptedException | SerialPortTimeoutException | UnsupportedEncodingException e) {
					FirmUpDialog.this.finishDialog(2);
					return ;
				}
				
				FirmUpDialog.this.finishDialog(0);
				
			}
			
		}).start();
	}

}
