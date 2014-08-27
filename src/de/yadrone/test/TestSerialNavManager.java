package de.yadrone.test;

import de.yadrone.base.datatypes.MagicPacket;
import de.yadrone.base.datatypes.s16Debug;
import de.yadrone.base.datatypes.s8;
import de.yadrone.base.datatypes.str_DebugOut;
import de.yadrone.base.datatypes.u8;
import de.yadrone.base.manager.SerialAbstractManager;
import de.yadrone.base.manager.SerialCommandManager;
import de.yadrone.base.manager.SerialEventListener;
import de.yadrone.base.mkdrone.command.Encoder;
import de.yadrone.base.mkdrone.flightdata.FlightInfo;
import de.yadrone.base.mkdrone.navdata.NCAnalogListener;
import de.yadrone.base.mkdrone.navdata.SerialNavManager;

public class TestSerialNavManager {
	
	public static void main(String[] args) {
		SerialEventListener serialListener = new SerialEventListener();
		try {
			SerialCommandManager cmdManager = new SerialCommandManager("COM2", false, serialListener);
			SerialNavManager navManager = new SerialNavManager(cmdManager, serialListener);
			serialListener.setInputStream(cmdManager.getSerialPort().getInputStream());
//			Encoder encoder = new Encoder(cmdManager.getSerialPort().getOutputStream());
//			navManager.addNCAnalogListener(new NCAnalogListener() {
//				
//				@Override
//				public void receivedAnalogData(str_DebugOut debug) {
//					for (s16Debug analog : debug.Analog) {
//						System.out.println("value: " + analog.getValue());
//					}
//				}
//			});
			cmdManager.start();
			navManager.start();
//			navManager.addWayPoint(51378423, -23458060, (int)FlightInfo.naviData.Altimeter.getValue());
//			navManager.addWayPoint(59999999, -29999999, (int)FlightInfo.naviData.Altimeter.getValue());
//			encoder.sendCommand(SerialAbstractManager.NC_ADDRESS, 'u', new int[] {2});
//			encoder.sendCommand(SerialAbstractManager.NC_ADDRESS, 'u', new MagicPacket().getAsInt());
//			encoder.sendCommand(SerialAbstractManager.NC_ADDRESS, 'u', new int[] { 27, 27, 85, 170, 0 });
//			encoder.sendCommand(SerialAbstractManager.NC_ADDRESS, 'u', new int[] { (byte) 0x1B, (byte) 0x1B, (byte) 0x55, (byte) 0xAA, (byte) 0x00 });
//			encoder.sendCommand(SerialAbstractManager.NC_ADDRESS, 'd', new u8(100).getAsInt());
			navManager.stop();
			cmdManager.stop();
			navManager.join();
			cmdManager.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
