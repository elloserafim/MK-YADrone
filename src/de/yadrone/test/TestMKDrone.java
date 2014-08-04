package de.yadrone.test;

import de.yadrone.base.MKDrone;
import de.yadrone.base.datatypes.s16Debug;
import de.yadrone.base.datatypes.str_DebugOut;
import de.yadrone.base.manager.SerialAbstractManager;
import de.yadrone.base.mkdrone.navdata.FCAnalogListener;
import de.yadrone.base.mkdrone.navdata.NCAnalogListener;

public class TestMKDrone {
	
	public static void main(String[] args) {
		try {
			MKDrone drone = new MKDrone("COM2", false);
//			drone.getSerialNavManager().addNCAnalogListener(new NCAnalogListener() {
//				
//				@Override
//				public void receivedAnalogData(str_DebugOut debug) {
//					for(s16Debug analog : debug.Analog) {
//						System.out.println(analog.getValue());
//					}
//				}
//			});
			drone.getSerialNavManager().redirectUART("FC");
//			drone.getSerialNavManager().addFCAnalogListener(new FCAnalogListener() {
//				
//				@Override
//				public void receivedAnalogData(str_DebugOut debug) {
//					for(s16Debug analog : debug.Analog) {
//						System.out.println(analog.getValue());
//					}
//				}
//			});
//			drone.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
