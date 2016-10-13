package me.jarviswang.canomega.protocols;

import javax.swing.JOptionPane;

import jssc.SerialPort;
import jssc.SerialPortException;

public class CANProtocols {
	
	protected SerialPort serialPort;
	protected StringBuilder incomingMessage = new StringBuilder();
	private int status; //0 for unconnected; 1 for connected;
	protected String firmwareVersion;
	protected String hardwareVersion;
	protected String serialNumber;
	protected static final int TIMEOUT = 1000;
	
	public CANProtocols() {
		status = 0;
	}
	
	public int connect(String port,String Baudrate) {
		
		this.serialPort = new SerialPort(port);
		try {
			this.serialPort.openPort();
			this.serialPort.setParams(Integer.valueOf(Baudrate).intValue(), 8, 1, 0);
			this.serialPort.writeBytes("v\r".getBytes());
		} catch (SerialPortException localSerialPortException) {
			return 1; //Serial Port error;
		}
		
		
		return 0;
	}
}
