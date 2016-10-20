package me.jarviswang.canomega.models;


public class KLogMessage {
	private KMessage kmsg;
	private KMessageType type;
	private long timestamp;
	
	public KMessageType getType() {
		return this.type;
	}
	
	public KMessage getKmsg() {
		return this.kmsg;
	}
	
	public long getTimestamp() {
		return this.timestamp;
	}
	
	
	public KLogMessage(KMessage msg,KMessageType type,long ts) {
		this.kmsg = msg;
		this.type = type;
		this.timestamp = ts;
	}
	
	public static enum KMessageType {
		IN,  OUT;
	    private KMessageType() {}
	}
}
