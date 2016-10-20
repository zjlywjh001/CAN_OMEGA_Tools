package me.jarviswang.canomega.models;

public class KMessage {
	
	protected byte[] data;
	
	public byte[] getData() {
		return this.data;
	}
	
	public void setData(byte[] dataArray) {
		this.data = dataArray;
	}
	
	public KMessage(byte[] packetdata) {
		
		this.data = packetdata;
	}
	
	
	public KMessage(String dataString) {
		String rawdata = dataString.substring(1);
		if (rawdata.length()%2!=0) {
			rawdata = "0" + rawdata;
		}
		int datalen = rawdata.length()/2;
		this.data = new byte[datalen];
		for (int i = 0 ; i < datalen;i=i+2) {
			try {
				this.data[i] = (byte) Integer.parseInt(rawdata.substring(i,i+1),16);
			} catch (StringIndexOutOfBoundsException e1) {
				this.data[i] = 0;
			} catch (NumberFormatException e2) {
				this.data[i] = 0;
			}
		}
	}
	
	public String toString() {
		String str = "";
		for (int i = 0; i < this.data.length; i++) {
			str += String.format("%02X", this.data[i]);
		}
		return str;
	}
}
