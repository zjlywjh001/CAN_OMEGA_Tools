package me.jarviswang.canomega.models;

public class MonitorMessage {
	
	private LogMessage lastLogMessage;
	private LogMessage diffmsg;
	private long period;
	private long count;
	
	public LogMessage getLastLogMessage() {
		return this.lastLogMessage;
	}
	
	public void setLastLogMessage(LogMessage lastmsg) {
		this.lastLogMessage = lastmsg;
	}
	
	public void setPeriod(long per) {
		this.period = per;
	}
	
	public long getPeriod() {
		return this.period;
	}
	
	public void increaseCount() {
		this.count += 1L;
	}
	
	public long getCount() {
		return this.count;
	}
	
	public MonitorMessage(LogMessage msg) {
		this.lastLogMessage = msg;
		this.diffmsg = null;
		this.count = 1L;
		this.period = 0L;
	}
	
	public LogMessage getMsgforDiff() {
		return this.diffmsg;
	}
	
	public void setMsgforDiff(LogMessage diff) {
		
		this.diffmsg = diff;
	}

}
