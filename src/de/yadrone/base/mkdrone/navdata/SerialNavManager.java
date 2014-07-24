package de.yadrone.base.mkdrone.navdata;

import java.util.ArrayList;

import de.yadrone.base.manager.SerialAbstractManager;
import de.yadrone.base.manager.SerialCommandManager;

public class SerialNavManager extends SerialAbstractManager {

	private SerialCommandManager manager; 
	
	private ArrayList<NCAnalogListener> ncAnalogListener;
	
//	public SerialNavManager(SerialEventListener serialListener, SerialCommandManager manager) {
//		this(manager.getSerialPort(), manager.isUSB(), serialListener, manager);
//	}
	
	public SerialNavManager(String serialPort, boolean isUSB,
			SerialEventListener serialListener, SerialCommandManager manager) throws Exception {
		super(serialPort, isUSB, serialListener);
		this.manager = manager;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
