package de.yadrone.base;

import de.yadrone.base.manager.SerialCommandManager;
import de.yadrone.base.mkdrone.navdata.SerialNavManager;

public interface IMKDrone {
	public SerialCommandManager getSerialCommandManager() throws Exception;

	public SerialNavManager getSerialNavManager();
	
	public void takeoff();
	
}
