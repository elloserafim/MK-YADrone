package de.yadrone.test;

import de.yadrone.base.datatypes.NaviData_t;
import de.yadrone.base.manager.SerialAbstractManager;
import de.yadrone.base.manager.SerialCommandManager;
import de.yadrone.base.manager.SerialEventListener;
import de.yadrone.base.mkdrone.command.Encoder;
import de.yadrone.base.mkdrone.navdata.NCOSDListener;
import de.yadrone.base.mkdrone.navdata.SerialNavManager;

public class TestOSDData {

	public static void main(String[] args) {
		SerialEventListener serialListener = new SerialEventListener();
		try {
			SerialCommandManager cmdManager = new SerialCommandManager("COM2", false, serialListener);
			SerialNavManager navManager = new SerialNavManager(cmdManager, serialListener);
			serialListener.setInputStream(cmdManager.getSerialPort()
					.getInputStream());
			Encoder encoder = new Encoder(cmdManager.getSerialPort()
					.getOutputStream());
			navManager.addOSDListener(new NCOSDListener() {

				@Override
				public void receivedOSDData(NaviData_t navData) {
					System.out.println("Gas: " + navData.Gas.getValue());

				}
			});
			Thread t = new Thread(navManager);
			t.start();
//			while (!t.isAlive());
			System.out.println("Sending request");
			encoder.sendCommand(SerialAbstractManager.NC_ADDRESS, 'o',
					new int[] { 10 });
			t.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
