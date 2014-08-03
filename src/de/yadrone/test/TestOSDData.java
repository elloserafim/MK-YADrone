package de.yadrone.test;

import de.yadrone.base.datatypes.NaviData_t;
import de.yadrone.base.manager.SerialAbstractManager;
import de.yadrone.base.manager.SerialEventListener;
import de.yadrone.base.mkdrone.command.Encoder;
import de.yadrone.base.mkdrone.navdata.NCOSDListener;
import de.yadrone.base.mkdrone.navdata.SerialNavManager;

public class TestOSDData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SerialEventListener serialListener = new SerialEventListener();
		try {
			SerialNavManager manager;
			manager = new SerialNavManager("COM2", false, serialListener, null);
			serialListener.setInputStream(manager.getSerialPort()
					.getInputStream());
			Encoder encoder = new Encoder(manager.getSerialPort()
					.getOutputStream());
			manager.addOSDListener(new NCOSDListener() {

				@Override
				public void receivedOSDData(NaviData_t navData) {
					// TODO Auto-generated method stub
					System.out.println("Gas: " + navData.Gas.getValue());

				}
			});
			Thread t = new Thread(manager);
			t.start();
			while (!t.isAlive())
				;
			System.out.println("Sending request");
			encoder.sendCommand(SerialAbstractManager.NC_ADDRESS, 'o',
					new int[] { 10 });
			t.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
