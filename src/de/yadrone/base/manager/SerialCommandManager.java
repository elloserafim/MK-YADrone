package de.yadrone.base.manager;

import de.yadrone.base.mkdrone.navdata.SerialEventListener;

public class SerialCommandManager extends SerialAbstractManager {

	public SerialCommandManager(SerialEventListener serialListener) throws Exception {
		this(null, false, serialListener);
	}
	
	public SerialCommandManager(String serialPort, boolean isUSB,
			SerialEventListener serialListener) throws Exception {
		super(serialPort, isUSB, serialListener);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
