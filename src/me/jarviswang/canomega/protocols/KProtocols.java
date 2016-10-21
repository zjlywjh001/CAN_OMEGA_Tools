package me.jarviswang.canomega.protocols;

import java.util.Arrays;

import jssc.SerialPortException;
import me.jarviswang.canomega.commons.CommonUtils;
import me.jarviswang.canomega.commons.KLineMessageListener;
import me.jarviswang.canomega.commons.CommonUtils.KProtos;

public class KProtocols implements KLineMessageListener {
	
	private KProtos cProto = null;
	private boolean activated;
	private StringBuilder incomingMessage = new StringBuilder();
	
	public KProtocols() {
		this.cProto = null;
		this.activated = false;
	}
	
	public int ActiveKLine(KProtos proto) {
		
		try {
			switch (proto) {
			case KWP2000_Fast_Init:
				CommonUtils.serialPort.writeBytes("kak\r".getBytes());
				Thread.sleep(100);
				break;
			}
		} catch (InterruptedException e) {
			return -1;
		} catch (SerialPortException e2) {
			return -2;
		}
		
		if (!activated) {
			return 1;
		}
		
		return 0;
	}
	
	public void DeActiveKLine() {
		try {
			CommonUtils.serialPort.writeBytes("kd\r".getBytes());
		} catch (SerialPortException e2) {
			
		}
		return ;
	}
	
	public void SendKLineMessage(String msg) {
		try {
			CommonUtils.serialPort.writeBytes((msg + "\r").getBytes());
		} catch (SerialPortException e) {
			
		}
		return ;
	}
	
	

	@Override
	public void receiveKLineMessage(String msg) {
		System.out.println(Arrays.toString(msg.getBytes()));
		if (msg.charAt(0)=='o') {
			this.activated = true;
		} else if (msg.charAt(0)=='k') {
			
		}
		
	}

}
