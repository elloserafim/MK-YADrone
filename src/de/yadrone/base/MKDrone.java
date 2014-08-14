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
	
	private static SerialEventListener serialListener;

	public MKDrone() throws Exception {
		this(null, false);
	}
	
	public MKDrone(String serialPort, boolean isUSB) throws Exception {
		serialListener = new SerialEventListener();
		if(serialPort == null) {
			serialCommManager = new SerialCommandManager(isUSB, serialListener);
		} else {
			serialCommManager = new SerialCommandManager(serialPort, isUSB, serialListener);
		}
		serialNavManager = new SerialNavManager(serialCommManager, serialListener);
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
			serialCommManager.start();
			serialNavManager.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		serialCommManager.stop();
	}

	@Override
	public void takeOff() {
		if(serialCommManager != null){
			serialCommManager.takeoff();
		}
	}

	@Override
	public void spinRight(int speed) {
		if(serialCommManager != null)
			serialCommManager.spinRight(speed);
		
	}

	@Override
	public void spinLeft(int speed) {
		if(serialCommManager != null)
			serialCommManager.spinLeft(speed);
		
	}

	@Override
	public void forward(int speed) {
		if(serialCommManager != null)
			serialCommManager.forward(speed);
		
	}

	@Override
	public void backward(int speed) {
		if(serialCommManager != null)
			serialCommManager.backward(speed);
		
	}

	@Override
	public void landing() {
		if(serialCommManager != null)
			serialCommManager.landing();
	}

	@Override
	public void freeze() {
		if(serialCommManager != null)
			serialCommManager.freeze();
		
	}

	@Override
	public void goRight(int speed) {
		if(serialCommManager != null)
			serialCommManager.roll(-speed);
		
	}

	@Override
	public void goLeft(int speed) {
		if(serialCommManager != null)
			serialCommManager.roll(speed);
		
	}

	@Override
	public void up(int value) {
		if(serialCommManager != null)
			serialCommManager.moveVertically(value);
		
	}

	@Override
	public void down(int value) {
		if(serialCommManager != null)
			serialCommManager.moveVertically(-value);
		
	}

}
