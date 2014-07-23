package de.yadrone.base;

import de.yadrone.base.manager.SerialCommandManager;

/**
 * @author Ello Oliveira
 * The Mikrokopter Drone
 */
public class MKDrone implements IMKDrone {
	
	//Managers
	private SerialCommandManager serialCommManager = null;

	@Override
	public SerialCommandManager getSerialCommandManager() {
		if(serialCommManager == null){
			serialCommManager = new SerialCommandManager();
		}
		return serialCommManager;
	}

	@Override
	public void takeoff() {
		// TODO Auto-generated method stub
		
	}

}
