package me.jarviswang.canomega.commons;

public class CommonUtils {
	
	public static String version="0.1";
	
	public static enum OpenMode
	  {
	    NORMAL,  LISTENONLY,  LOOPBACK;
	    private OpenMode() {}
	  }
	
	
	public static enum CANProtocols
	  {
		  CAN500Kbps_11bits,CAN500Kbps_29bits,CAN250Kbps_11bits,  CAN250Kbps_29bits;
	    private CANProtocols() {}
	  }
}
