package me.jarviswang.canomega.protocols;

import java.util.Arrays;

import javax.swing.JOptionPane;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import me.jarviswang.canomega.commons.CommonUtils;
import me.jarviswang.canomega.commons.CommonUtils.CANProtos;
import me.jarviswang.canomega.commons.CommonUtils.OpenMode;

public class CANProtocols implements SerialPortEventListener {
	
	private CANProtos subProto = null;
	public static StringBuilder incomingMessage = new StringBuilder();
	
	public CANProtocols() {
		CommonUtils.state = 0;
	}
	
	public int connect(String port,String Baudrate) {
		
		if (CommonUtils.serialPort==null) {
			CommonUtils.serialPort = new SerialPort(port);
		}
		
		try {
			CommonUtils.serialPort.openPort();
			CommonUtils.serialPort.setParams(Integer.valueOf(Baudrate).intValue(), 8, 1, 0);
			CommonUtils.serialPort.writeBytes("\rb\r".getBytes());
			Thread.sleep(100);
			CommonUtils.serialPort.purgePort(SerialPort.PURGE_TXCLEAR|SerialPort.PURGE_RXCLEAR);
			CommonUtils.serialPort.writeBytes("b\r".getBytes());
			int i;
			do {
				byte [] arrayOfByte = CommonUtils.serialPort.readBytes(1,1000);
				i = arrayOfByte[0];
			} while ((i != 13) && (i != 7));
			CommonUtils.firmwareVersion = CommonUtils.Transreceive("v").substring(1);
			CommonUtils.hardwareVersion = CommonUtils.Transreceive("V").substring(1);
			CommonUtils.serialNumber = CommonUtils.Transreceive("N").substring(1);
			CommonUtils.Transreceive("W2D00");
		} catch (SerialPortException localSerialPortException) {
			return 1; //Serial Port error;
		} catch (InterruptedException localInterruptedException) {
			return 3;
		} catch (SerialPortTimeoutException e) {
			return 2;
		}
		
		return 0;
	}
	
	public int openCANChannel(CANProtos Proto,OpenMode mode) {
		try {
			switch (Proto) 
			{
			case CAN500Kbps_11bits:
			case CAN500Kbps_29bits:
				CommonUtils.Transreceive("S6");
				break;
			case CAN250Kbps_11bits:
			case CAN250Kbps_29bits:
				CommonUtils.Transreceive("S5");
				break;
			default:
				CommonUtils.Transreceive("S6");
				break;
			}
			switch (mode) {
			case NORMAL:
				CommonUtils.Transreceive("O");
				break;
			case LISTENONLY:
				CommonUtils.Transreceive("L");
				break;
			case LOOPBACK:
				CommonUtils.Transreceive("l");
				break;
			default: 
				CommonUtils.Transreceive("O");
				break;
			}
			CommonUtils.serialPort.setEventsMask(1);
			CommonUtils.serialPort.addEventListener(this);
		} catch (SerialPortException localSerialPortException) {
	      return 1;
	    } catch (SerialPortTimeoutException localSerialPortTimeoutException) {
	      return 2;
	    }
		return 0;
		
	}
	
	public void closeCANChannel() {
		try {
			CommonUtils.serialPort.removeEventListener();
			CommonUtils.serialPort.writeBytes("C\r".getBytes());
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
		CommonUtils.firmwareVersion = null;
		CommonUtils.hardwareVersion = null;
		this.subProto = null;
		
	}
	
	public void disconnect() {
		try {
			CommonUtils.serialPort.closePort();
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
		CommonUtils.serialPort = null;
	}
	

	@Override
	public void serialEvent(SerialPortEvent event) {
		if (event.isRXCHAR() && (event.getEventValue()>0)) {
			try {
				byte[] arrayOfByte1 = CommonUtils.serialPort.readBytes();
				System.out.println(Arrays.toString(arrayOfByte1));
				for (int k:arrayOfByte1) {
					if ((k==13) && (incomingMessage.length()>0)) {
						String str = incomingMessage.toString();
						int m = str.charAt(0);
						if ((m == 116) || (m == 84) || (m == 114) || (m == 82)) {
							// TODO Build CAN Message
						} else if ((m == 122) || (m == 90)) {
							//TODO send next message
							incomingMessage.setLength(0);
						} else if (k == 7) {
							//TODO Retry send
						} else if (k == 13) {
							this.incomingMessage.append((char)k);
						}
					}
				}
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public CANProtos getDefaultProtocol() {
		return CommonUtils.CANProtos.CAN500Kbps_11bits;
	}
	
	public CANProtos getCurrentProto() {
		return this.subProto;
	}
	
	public void setCurrentProto(CANProtos proto) {
		this.subProto = proto;
	}
	
	
}
