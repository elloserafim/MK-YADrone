package de.yadrone.base;

import de.yadrone.base.ardrone.command.CommandManager;
import de.yadrone.base.manager.SerialCommandManager;
import de.yadrone.base.mkdrone.command.ExternControlCommand;

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
			serialCommManager = new SerialCommandManager(false, "COM4");
		}
		return serialCommManager;
	}


	@Override
	public void start() {
		getSerialCommandManager();
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		serialCommManager.stop();
		
		
	}

}
