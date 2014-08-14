package de.yadrone.test;

import de.yadrone.base.IMKDrone;
import de.yadrone.base.MKDrone;
import de.yadrone.base.datatypes.NaviData_t;
import de.yadrone.base.manager.SerialAbstractManager;
import de.yadrone.base.mkdrone.command.Encoder;
import de.yadrone.base.mkdrone.navdata.NCOSDListener;

/**
 * @author Ello Oliveira
 * Class to test sending of commands
 *
 */
public class TestSerialCommandManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IMKDrone drone = null;
		try {
			drone = new MKDrone();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		drone.getSerialNavManager().addOSDListener(new NCOSDListener() {
			
			@Override
			public void receivedOSDData(NaviData_t navData) {
				System.out.println("Gas:" + navData.Gas.getValue());
				System.out.println("Heading:" + navData.Heading.getValue());
				System.out.println("Nick:" + navData.AngleNick.getValue());
				
			}
		});
		
		try{
			//We set the output stream to print the bytes on screen
			//drone.getSerialCommandManager().setOutputStream(System.out);
			drone.start();
			System.out.println("takeoff");
			drone.takeOff();
			
			drone.up(30);
			System.out.println("split left");
			drone.getSerialCommandManager().spinLeft(20).doFor(3000);
			System.out.println("freeze");
			drone.getSerialCommandManager().freeze().doFor(3000);
			System.out.println("land");
			drone.landing();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
//			drone.stop();
		}

	}

}
