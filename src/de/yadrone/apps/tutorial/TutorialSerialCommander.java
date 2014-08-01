package de.yadrone.apps.tutorial;

import de.yadrone.base.MKDrone;

/**
 * @author Ello Oliveira
 * Class to test sending of commands
 *
 */
public class TutorialSerialCommander {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MKDrone drone = null;
		try {
			drone = new MKDrone();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try{
			drone.getSerialCommandManager().setOutputStream(System.out);
			drone.start();
			
			drone.getSerialCommandManager().takeoff();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//drone.stop();
		}

	}

}
