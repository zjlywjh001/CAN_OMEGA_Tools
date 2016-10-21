package me.jarviswang.canomega.commons;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

public class CommonUtils {
	
	public static String version="0.9-Alpha";
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
		CAN500Kbps_11bits,CAN500Kbps_29bits,CAN250Kbps_11bits,  CAN250Kbps_29bits;
	    private CANProtos() {}
	}
	
	public static enum KProtos {
		KWP2000_Fast_Init;
	    private KProtos() {}
	}
	
	public static enum FuzzOrder {
		Left_Byte_First, Right_Byte_First;
	    private FuzzOrder() {}
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
	
}
