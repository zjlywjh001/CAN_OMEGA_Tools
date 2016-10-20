package me.jarviswang.canomega.models;

import java.util.Arrays;

public class CANMessage {
	protected int id;
	protected byte[] data;
	protected boolean extended = false;
	protected boolean rtr;
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		if (id > 0x1FFFFFFF) {
			id = 0x1FFFFFFF;
		}
		if (id > 0x7FF) {
			this.extended = true;
		}
		this.id=id;
	}
	
	public byte[] getData() {
		return this.data;
	}
	
	public void setData(byte[] dataArray) {
		this.data = dataArray;
	}
	
	public boolean isExtended()
	{
		return this.extended;
	}
	
	public boolean isRtr() {
		return this.rtr;
	}
	
	public CANMessage(int packetid,byte[] packetdata) {
		this.data = packetdata;
		this.extended = false;
		this.setId(packetid);
		this.rtr = false;
	}
	
	public CANMessage(int packetid, byte[] packetdata,boolean isextended,boolean isrtr) {
		this.setId(packetid);
		this.data = packetdata;
		this.extended = isextended;
		this.rtr = isrtr;
	}
	
	public CANMessage(String dataString) {
		this.rtr = false;
		int i = 1;
		int j;
		if (dataString.length()>0) {
			j = dataString.charAt(0);
		} else {
			j = 116;
		}
		switch (j) {
		case 114:
			this.rtr = true;
		case 103:
		case 116:
		default:
			try {
				this.id = Integer.parseInt(dataString.substring(i,i+3),16);
			} catch (StringIndexOutOfBoundsException e1) {
				this.id = 0;
			} catch (NumberFormatException e2) {
				this.id = 0;
			}
			this.extended = false;
			i += 3;
			break;
		case 82:
			this.rtr = true;
		case 71:
		case 84:
			try {
				this.id = Integer.parseInt(dataString.substring(i,i+8),16);
			} catch (StringIndexOutOfBoundsException e1) {
				this.id = 0;
			} catch (NumberFormatException e2) {
				this.id = 0;
			}
			this.extended = true;
			i += 8;
		}
		int k;
		try {
			k = Integer.parseInt(dataString.substring(i,i+1),16);
			if (k > 8) {
				k = 8;
			}
		} catch (StringIndexOutOfBoundsException e1) {
			k = 0;
		} catch (NumberFormatException e2) {
			k = 0;
		}
		i++;
		this.data = new byte[k];
		if (!this.rtr) {
			for (int m = 0; m < k; m++) {
				try {
					this.data[m] = (byte)Integer.parseInt(dataString.substring(i,i+2),16);
				} catch (StringIndexOutOfBoundsException e1) {
					this.data[m] = 0;
				} catch (NumberFormatException e2) {
					this.data[m] = 0;
				}
				i += 2;
			}
		}
	}
	
	public String toString() {
		String str;
		if (this.extended) {
			if (this.rtr) {
				str = "R";
			} else {
				str = "T";
			}
			str += String.format("%08X", this.id);
		} else {
			if (this.rtr) {
				str = "r";
			} else {
				str = "t";
			}
			str += String.format("%03X", this.id);
		}
		
		str += String.format("%01X", this.data.length);
		if (!this.rtr) {
			for (int i = 0; i < this.data.length; i++) {
				str += String.format("%02X", this.data[i]);
			}
		}
		return str;
	}

}
