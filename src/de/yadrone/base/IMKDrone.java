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
	
	/**
	 * Sets drone throttle to a minimum
	 */
	public void takeOff();
	
	/**
	 * @param speed range 0 to 127
	 */
	public void forward(int speed);
	/**
	 * @param speed range 0 to 127
	 */
	public void backward(int speed);
	/**
	 * @param speed range 0 to 127
	 */
	public void spinRight(int speed);
	/**
	 * @param speed range 0 to 127
	 */
	public void spinLeft(int speed);

	public void landing();
	
	/**
	 * @param speed range 0 to 127
	 */
	public void goRight(int speed);
	
	/**
	 * @param speed range 0 to 127
	 */
	public void goLeft(int speed);
	
	public void up(int value);
	public void down(int value);
	
	public void freeze();
	
	SerialNavManager getSerialNavManager();
	
	
}
