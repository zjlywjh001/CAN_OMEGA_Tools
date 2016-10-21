package me.jarviswang.canomega.protocols;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import me.jarviswang.canomega.commons.CANMessageListener;
import me.jarviswang.canomega.commons.CommonUtils;
import me.jarviswang.canomega.commons.CommonUtils.CANProtos;
import me.jarviswang.canomega.commons.CommonUtils.OpenMode;
import me.jarviswang.canomega.commons.FuzzMessageListener;
import me.jarviswang.canomega.commons.JMessageListener;
import me.jarviswang.canomega.commons.KLineMessageListener;
import me.jarviswang.canomega.models.CANMessage;
import me.jarviswang.canomega.models.FuzzMessage;

public class CANProtocols implements SerialPortEventListener {
	
	private CANProtos subProto = null;
	private StringBuilder incomingMessage = new StringBuilder();
	protected ArrayList<CANMessageListener> listeners = new ArrayList<CANMessageListener>();
	protected LinkedList<CANMessage> TXFIFO = new LinkedList<CANMessage>();
	protected List<FuzzMessageListener> fuzzlisteners = (List<FuzzMessageListener>) Collections.synchronizedList(new ArrayList<FuzzMessageListener>());
	protected List<KLineMessageListener> klisteners = (List<KLineMessageListener>) Collections.synchronizedList(new ArrayList<KLineMessageListener>());
	protected List<JMessageListener> jlisteners = (List<JMessageListener>) Collections.synchronizedList(new ArrayList<JMessageListener>());

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
			this.subProto = Proto;
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
		} catch (SerialPortException e) {
			return 1;
	    } catch (SerialPortTimeoutException localSerialPortTimeoutException) {
	    	return 2;
	    }
		return 0;
		
	}
	
	public int closeCANChannel() {
		int result = 0;
		try {
			CommonUtils.serialPort.writeBytes("C\r".getBytes());
			Thread.sleep(100);
			CommonUtils.serialPort.removeEventListener();
			
		} catch (SerialPortException e) {
			result = 1;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CommonUtils.firmwareVersion = null;
		CommonUtils.hardwareVersion = null;
		this.subProto = null;
		return result;
		
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
					if ((k==13) && (this.incomingMessage.length()>0)) {
						String str = this.incomingMessage.toString();
						int m = str.charAt(0);
						if ((m == 116) || (m == 84) || (m == 114) || (m == 82)) {
							CANMessage msg = new CANMessage(str);
							Iterator It = this.listeners.iterator();
							while (It.hasNext()) {
								CANMessageListener msglistener = (CANMessageListener)It.next();
								msglistener.receiveCANMessage(msg);
							}
						} else if ((m == 122) || (m == 90)) {
							this.TXFIFO.removeFirst();
							this.sendFirstTXFIFIOMessage();
						} else if (m==103 || m==71) {
							FuzzMessage msg = new FuzzMessage(str);
							Iterator It = this.fuzzlisteners.iterator();
							while (It.hasNext()) {
								FuzzMessageListener msglistener = (FuzzMessageListener)It.next();
								msglistener.receiveFuzzMessage(msg);
							}
						} else if (m==117) {
							Iterator It = this.fuzzlisteners.iterator();
							while (It.hasNext()) {
								FuzzMessageListener msglistener = (FuzzMessageListener)It.next();
								msglistener.finishFuzz();
							}
						} else if (m==111 || m==107) {
							Iterator It = this.klisteners.iterator();
							while (It.hasNext()) {
								KLineMessageListener msglistener = (KLineMessageListener)It.next();
								msglistener.receiveKLineMessage(str);
							}
						} else if (m==106) {
							Iterator It = this.jlisteners.iterator();
							while (It.hasNext()) {
								JMessageListener msglistener = (JMessageListener)It.next();
								msglistener.receiveJMessage(str);
							}
						}
						this.incomingMessage.setLength(0);
					} else if (k == 7) {
						this.sendFirstTXFIFIOMessage();
					} else if (k != 13) {
						this.incomingMessage.append((char)k);
					}
				}
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	protected int sendFirstTXFIFIOMessage() {
		if (this.TXFIFO.size() == 0 || CommonUtils.serialPort==null) {
			return 1;
		} 
		CANMessage msg = this.TXFIFO.getFirst();
		try {
			CommonUtils.serialPort.writeBytes((msg.toString()+"\r").getBytes());
		} catch (SerialPortException e) {
			return 2;
		}
		return 0;
	}
	
	public int send(CANMessage paramCANMessage) {
		this.TXFIFO.add(paramCANMessage);
		if (this.TXFIFO.size() > 1) {
			return 0;
		}
		return sendFirstTXFIFIOMessage();
	}
	
	public int setFuzzer(String fuzzconfstr) {
		try {
			CommonUtils.serialPort.writeBytes((fuzzconfstr+"\r").getBytes());
		} catch (SerialPortException e) {
			return -1;
		}
		return 0;
	}
	
	public int PauseFuzzing() {
		try {
			CommonUtils.serialPort.writeBytes("fz\r".getBytes());
		} catch (SerialPortException e) {
			return -1;
		}
		return 0;
	}
	
	public int ResumeFuzzing() {
		try {
			CommonUtils.serialPort.writeBytes("fr\r".getBytes());
		} catch (SerialPortException e) {
			return -1;
		}
		return 0;
	}
	
	public int StopFuzzing() {
		try {
			CommonUtils.serialPort.writeBytes("fP\r".getBytes());
		} catch (SerialPortException e) {
			return -1;
		}
		return 0;
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
	
	public boolean changeConnectMode(CANProtos newProto,OpenMode newMode) {
		int res = this.closeCANChannel();
		if (res==0) {
			int ret = this.openCANChannel(newProto, newMode);
			return (ret==0);
		} else {
			return false;
		}
	}
	
	public void PauseEventListener() {
		try {
			CommonUtils.serialPort.removeEventListener();
		} catch (SerialPortException e) {
			return ;
		}
	}
	
	public void ResumeEventListener() {
		try {
			CommonUtils.serialPort.addEventListener(this);
		} catch (SerialPortException e) {
			return ;
		}
	}
	
	
	
	
	
	
	public void addMessageListener(CANMessageListener paramCANMessageListener) {
		this.listeners.add(paramCANMessageListener);
    }
	  
	public void removeMessageListener(CANMessageListener paramCANMessageListener) {
		this.listeners.remove(paramCANMessageListener);
  	}
	
	public void addFuzzMessageListener(FuzzMessageListener l) {
		this.fuzzlisteners.add(l);
    }
	  
	public void removeFuzzMessageListener(FuzzMessageListener l) {
		this.fuzzlisteners.remove(l);
  	}
	
	public void addKLineMessageListener(KLineMessageListener l) {
		this.klisteners.add(l);
    }
	  
	public void removeKLineMessageListener(KLineMessageListener l) {
		this.klisteners.remove(l);
  	}
	
	public void addJMessageListener(JMessageListener l) {
		this.jlisteners.add(l);
    }
	  
	public void removeJMessageListener(JMessageListener l) {
		this.jlisteners.remove(l);
  	}

	
}
