package me.jarviswang.canomega.models;

public class FuzzMessage extends CANMessage {
	
	private boolean isFuzzprocess;

	public FuzzMessage(String dataString) {
		super(dataString);
		// TODO Auto-generated constructor stub
		this.isFuzzprocess = true;
	}
	
	public boolean isFuzzPorcess() {
		return this.isFuzzprocess;
	}
	
	
	

}
