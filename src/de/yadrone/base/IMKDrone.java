package de.yadrone.base;

import de.yadrone.base.manager.SerialCommandManager;

public interface IMKDrone {
	public SerialCommandManager getSerialCommandManager();

	public void takeoff();
}
