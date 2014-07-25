package de.yadrone.base;

import de.yadrone.base.manager.SerialCommandManager;

public interface IMKDrone {
	public SerialCommandManager getSerialCommandManager();

	/**
	 * Initialises the SerialCommandManager for the drone.
	 */
	public void start();
	public void stop();
	
}
