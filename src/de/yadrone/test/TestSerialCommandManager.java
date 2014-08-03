package de.yadrone.test;

import de.yadrone.base.IMKDrone;
import de.yadrone.base.MKDrone;

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
		
		try{
			//We set the output stream to print the bytes on screen
			//drone.getSerialCommandManager().setOutputStream(System.out);
			drone.start();
			
			drone.getSerialCommandManager().takeoff();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//drone.stop();
		}

	}

}
