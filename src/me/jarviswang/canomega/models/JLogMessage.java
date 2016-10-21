package me.jarviswang.canomega.models;


public class JLogMessage {
	private JMessage jmsg;
	private JMessageType type;
	private long timestamp;
	
	public JMessageType getType() {
		return this.type;
	}
	
	public JMessage getKmsg() {
		return this.jmsg;
	}
	
	public long getTimestamp() {
		return this.timestamp;
	}
	
	
	public JLogMessage(JMessage msg,JMessageType type,long ts) {
		this.jmsg = msg;
		this.type = type;
		this.timestamp = ts;
	}
	
	public static enum JMessageType {
		IN,  OUT;
	    private JMessageType() {}
	}
}
