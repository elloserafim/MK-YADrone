package de.yadrone.apps.tutorial;

import de.yadrone.base.MKDrone;

public class TutorialSerialCommander {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MKDrone drone = null;
		drone = new MKDrone();
		
		try{
		drone.start();
	//	drone.getSerialCommandManager().setOutputStream(System.out);
		drone.getSerialCommandManager().takeoff();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			drone.stop();
		}

	}

}
