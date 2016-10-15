package me.jarviswang.canomega.models;

public class LogMessage {

	private CANMessage canmsg;
	private String message;
	private MessageType type;
	private long timestamp;
	
	public MessageType getType() {
		return this.type;
	}
	
	public CANMessage getCanmsg() {
		return this.canmsg;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public long getTimestamp() {
		return this.timestamp;
	}
	
	
	public LogMessage(CANMessage msg,String msgstr,MessageType type,long ts) {
		this.canmsg = msg;
		this.message = msgstr;
		this.type = type;
		this.timestamp = ts;
	}
	
	public static enum MessageType
	  {
	    INFO,  ERROR,  IN,  OUT;
	    
	    private MessageType() {}
	  }
}
