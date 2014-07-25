package de.yadrone.base.mkdrone.navdata;

import java.util.ArrayList;

import de.yadrone.base.manager.SerialAbstractManager;
import de.yadrone.base.manager.SerialCommandManager;
import de.yadrone.base.manager.SerialEventListener;

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
		try {
			while (true) {
				synchronized (this) {
					while (buffer == null) {
						this.wait();
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			buffer = null;
		}
	}

}
