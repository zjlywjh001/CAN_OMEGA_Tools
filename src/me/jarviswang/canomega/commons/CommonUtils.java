package me.jarviswang.canomega.commons;

import java.util.Arrays;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

public class CommonUtils {
	
	public static String version="1.1";
	public static int state = 0; //connect state 0:disconnected 1:CAN connected 2:K-Line Connected
	//3 SAE J1850 Connected
	public static int protoSelected = 0;
	public static SerialPort serialPort = null;
	public static String firmwareVersion;
	public static String hardwareVersion;
	public static String serialNumber;
	public static final int TIMEOUT = 1000;
	
	public static enum OpenMode {
		NORMAL,  LISTENONLY,  LOOPBACK;
	    private OpenMode() {}
	}
	    
	public static enum CANProtos {
		CAN500Kbps_11bits,CAN500Kbps_29bits,CAN250Kbps_11bits,CAN250Kbps_29bits,CAN125Kbps_11bits,CAN125Kbps_29bits;
	    private CANProtos() {}
	}
	
	public static enum KProtos {
		KWP2000_Fast_Init,KWP2000_5Baud_Init,ISO9141_5Baud_Init;
	    private KProtos() {}
	}
	
	public static enum FuzzOrder {
		Left_Byte_First, Right_Byte_First;
	    private FuzzOrder() {}
	}
	
	public static enum ResistorState {
		Disabled, _120Ω;
	    private ResistorState() {}
	}
	
	public static enum JModes {
		VPW,PWM;
		private JModes() {}
	}
	
	public static String Transreceive(String data) throws SerialPortException, SerialPortTimeoutException {
		if (serialPort==null) {
			return null;
		}
		String str = data + "\r";
		serialPort.writeBytes(str.getBytes());
		return GetResponse();
	}
	
	public static String GetResponse() throws SerialPortException, SerialPortTimeoutException {
		if (serialPort==null) {
			return null;
		}
		StringBuilder localStringBuilder = new StringBuilder();
		for (;;) {
			byte[] arrayOfByte = serialPort.readBytes(1,1000);
			if (arrayOfByte[0] == 13) {
				return localStringBuilder.toString();
			}
			if (arrayOfByte[0] == 7) {
				throw new SerialPortException(serialPort.getPortName(), "transmit", "BELL signal");
			}
			localStringBuilder.append((char)arrayOfByte[0]);
		}
	}
	
	public static int J1850_CRC(int[] buf) {
		int poly,crc_reg;
		
		crc_reg = 0xFF;
		for (int i = 0; i < buf.length; i++) {
			for (int j = 0; j < 8; j++) {
				if ((buf[i]&(1<<(7-j)))!=0) {
					if ((crc_reg & 0x80)!=0) {
						poly = 1;
					} else {
						poly = 0x1c;
					}
					crc_reg = (((crc_reg<<1)|1)^poly)&0xFF;
				} else {
					poly = 0;
					if ((crc_reg & 0x80)!=0) {
						poly = 0x1d;
					}
					crc_reg = ((crc_reg << 1)^poly)&0xFF;
				}
			}
		}
		
		return (~crc_reg)&0xFF;	// Return CRC
	}
	
	public static int UpdateCRC16(int crc_in, int bt) {  //CRC16 for ymodem
		int crc = crc_in;
		int in = bt | 0x100;
		do{
			crc = crc <<1;
			in = in <<1;
			if ((in&0x100)!=0) crc = crc+1;
			if ((crc & 0x10000)!=0) crc = crc ^ 0x1021;
			
		} while ((in&0x10000) == 0);
		return crc & 0xFFFF;
	}
	
	public static int Cal_CRC16(byte[] p_data, int size) {
		int crc = 0;
		
		for (int i=0; i<size;i++) {
			crc = UpdateCRC16(crc, p_data[i]&0x0FF);
		}
		
		crc = UpdateCRC16(crc, 0);
		crc = UpdateCRC16(crc, 0);
		
		return crc&0xFFFF;
	}
	
	
	//Cut data into 1024-byte Packet
	public static byte[] Getn_Packet(final byte data[],final int num){  //num>=1;
		byte[] frame_head = new byte[]{0x02,0x00,(byte)0xFF};
		frame_head[1] = (byte)(num&0x0FF);
		frame_head[2] = (byte)(frame_head[1]^0xFF);
		//String str= CommonUtil.backCMD2String(frame_head);
		//String[] cmds = str.split("-");
		byte[] pdata = new byte[1024];
		System.arraycopy(data, (num-1)*1024, pdata, 0, 1024);
		int crc1 = Cal_CRC16(pdata, pdata.length);
		byte[] crcb = new byte[]{(byte)(crc1>>8),(byte)(crc1&0x0FF)};
		byte[] packet = new byte[frame_head.length+1024+2];
		System.arraycopy(frame_head, 0, packet, 0, frame_head.length);
		System.arraycopy(pdata, 0, packet, frame_head.length, pdata.length);
		System.arraycopy(crcb, 0, packet, frame_head.length+pdata.length, 2);
		return packet;
	}
	
}
