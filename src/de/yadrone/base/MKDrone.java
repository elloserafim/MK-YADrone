package de.yadrone.base;

import de.yadrone.base.manager.SerialCommandManager;
import de.yadrone.base.mkdrone.navdata.SerialEventListener;
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
		this(null);
	}
	
	public MKDrone(String serialPort) throws Exception {
		serialEventListener = new SerialEventListener();
		serialCommManager = new SerialCommandManager(serialEventListener);
		serialNavManager = new SerialNavManager(serialPort, false, serialEventListener, serialCommManager);
	}
	
	@Override
	public SerialCommandManager getSerialCommandManager() throws Exception {
		// [DPPB]: Personally, I do not like doing that. I prefer initializing
		// everything in the constructor; by doing so, you assure that it is
		// going to be initialized when you need it. There are cases where the
		// following is worth doing, but I do not think this is the case.
//		if(serialCommManager == null){
//			serialCommManager = new SerialCommandManager(serialEventListener);
//		}
		return serialCommManager;
	}
	
	@Override
	public SerialNavManager getSerialNavManager() {
		return serialNavManager;
	}

	@Override
	public void takeoff() {
		// TODO Auto-generated method stub
		
	}

}
