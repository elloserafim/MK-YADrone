package de.yadrone.test;

import de.yadrone.base.datatypes.s16Debug;
import de.yadrone.base.datatypes.str_DebugOut;
import de.yadrone.base.datatypes.u8;
import de.yadrone.base.manager.SerialAbstractManager;
import de.yadrone.base.manager.SerialCommandManager;
import de.yadrone.base.manager.SerialEventListener;
import de.yadrone.base.mkdrone.command.Encoder;
import de.yadrone.base.mkdrone.navdata.NCAnalogListener;
import de.yadrone.base.mkdrone.navdata.SerialNavManager;

public class TestSerialNavManager {
	
	public static void main(String[] args) {
		SerialEventListener serialListener = new SerialEventListener();
		try {
			SerialCommandManager commandMan = new SerialCommandManager("COM2", false, serialListener);
			SerialNavManager manager = new SerialNavManager(commandMan, serialListener);
			serialListener.setInputStream(manager.getSerialPort().getInputStream());
//			Encoder encoder = new Encoder(manager.getSerialPort().getOutputStream());
			manager.addNCAnalogListener(new NCAnalogListener() {
				
				@Override
				public void receivedAnalogData(str_DebugOut debug) {
					for (s16Debug analog : debug.Analog) {
						System.out.println("value: " + analog.getValue());
					}
				}
			});
			Thread t = new Thread(manager);
			t.start();
//			while(!t.isAlive());
//			encoder.sendCommand(SerialAbstractManager.NC_ADDRESS, 'd', new u8(100).getAsInt());
			t.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
