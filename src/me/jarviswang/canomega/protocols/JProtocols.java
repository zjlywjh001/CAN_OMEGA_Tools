package me.jarviswang.canomega.protocols;

import jssc.SerialPortException;
import me.jarviswang.canomega.commons.CommonUtils;

public class JProtocols {
	
	public static void SendJ1850Message(String msg) {
		try {
			CommonUtils.serialPort.writeBytes((msg + "\r").getBytes());
		} catch (SerialPortException e) {
			
		}
		return ;
	}

}
