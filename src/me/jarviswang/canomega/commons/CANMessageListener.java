package me.jarviswang.canomega.commons;

import me.jarviswang.canomega.models.CANMessage;

public abstract interface CANMessageListener {
	public abstract void receiveCANMessage(CANMessage msg);
}
