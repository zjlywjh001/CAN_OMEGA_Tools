package me.jarviswang.canomega.commons;

import me.jarviswang.canomega.models.FuzzMessage;

public interface FuzzMessageListener {
	public abstract void receiveFuzzMessage(FuzzMessage msg);
	
	public abstract void finishFuzz();
}
