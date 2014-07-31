package de.yadrone.base;

import de.yadrone.base.manager.SerialCommandManager;
import de.yadrone.base.manager.SerialEventListener;
import de.yadrone.base.mkdrone.navdata.SerialNavManager;

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
		this(null, true);
	}
	
	public MKDrone(String serialPort, boolean isUSB) throws Exception {
		serialEventListener = new SerialEventListener();
		if(serialPort == null) {
			serialCommManager = new SerialCommandManager(isUSB, serialEventListener);
		} else {
			serialCommManager = new SerialCommandManager(serialPort, isUSB, serialEventListener);
		}
		serialNavManager = new SerialNavManager(serialCommManager);
	}
	
	@Override
	public SerialCommandManager getSerialCommandManager() throws Exception {
		return serialCommManager;
	}
	
	@Override
	public SerialNavManager getSerialNavManager() {
		return serialNavManager;
	}


	public void start() {
		try {
			getSerialCommandManager().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		serialCommManager.stop();
	}

	@Override
	public void takeOff() {
		// TODO Auto-generated method stub
		if(serialCommManager !=null){
			serialCommManager.takeoff();
		}
		
	}

}
