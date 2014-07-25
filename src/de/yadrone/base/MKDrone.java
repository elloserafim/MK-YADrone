package de.yadrone.base;

import de.yadrone.base.ardrone.command.CommandManager;
import de.yadrone.base.manager.SerialCommandManager;
import de.yadrone.base.mkdrone.navdata.SerialEventListener;
import de.yadrone.base.mkdrone.navdata.SerialNavManager;
import de.yadrone.base.mkdrone.command.ExternControlCommand;

/**
 * @author Ello Oliveira
 * The Mikrokopter Drone
 */
public class MKDrone implements IMKDrone {
	
	//Managers
	private SerialCommandManager serialCommManager;
	private SerialNavManager serialNavManager;
	
	private static SerialEventListener serialEventListener;

	public MKDrone() throws Exception {
		this(null);
	}
	
	public MKDrone(String serialPort) throws Exception {
		serialEventListener = new SerialEventListener();
		serialCommManager = new SerialCommandManager(serialEventListener);
		serialNavManager = new SerialNavManager(serialPort, false, serialEventListener, serialCommManager);
	}
	
	@Override
	public SerialCommandManager getSerialCommandManager() throws Exception {
		return serialCommManager;
	}
	
	@Override
	public SerialNavManager getSerialNavManager() {
		return serialNavManager;
	}


	@Override
	public void start() {
		try {
			getSerialCommandManager();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		serialCommManager.stop();
		
		
	}

}
