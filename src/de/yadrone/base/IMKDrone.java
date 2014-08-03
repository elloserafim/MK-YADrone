package de.yadrone.base;

import de.yadrone.base.manager.SerialCommandManager;
import de.yadrone.base.mkdrone.navdata.SerialNavManager;

public interface IMKDrone {
	public SerialCommandManager getSerialCommandManager() throws Exception;
	
	/**
	 * Initialises the SerialCommandManager for the drone and the thread that sends commands.
	 */
	public void start();
	public void stop();
	
	public void takeOff();
	
//	public void forward();
//	public void backward();
	public void spinRight();
	public void spinLeft();

	SerialNavManager getSerialNavManager();
	
	
}
