package de.yadrone.base.manager;

import gnu.io.CommPortIdentifier;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import de.yadrone.base.ardrone.command.CommandManager;
import de.yadrone.base.exception.CommandException;
import de.yadrone.base.mkdrone.command.ExternControlCommand;
import de.yadrone.base.mkdrone.command.FCCommand;
import de.yadrone.base.mkdrone.command.MKCommand;
import de.yadrone.base.mkdrone.flightdata.FlightInfo;

/**
 * The manager for Serial Communication with MKDrone
 * It runs as a thread and puts the commands in a queue
 * @author Ello Oliveira and Diogo Branco
 *
 */

// [DPPB]: This class should be placed under de.yadrone.base.mkdrone.command, in accordance
// with YADrone's design.
public class SerialCommandManager extends SerialAbstractManager implements Runnable {
	
	/**
	 *  This variable stores information about the selected port
	 */
	static CommPortIdentifier portId;
    static HashMap<String, CommPortIdentifier> portMap;
	protected Thread thread = null;
	private ConcurrentLinkedQueue<FCCommand> queue;
	
	public SerialCommandManager(boolean isUSB, SerialEventListener serialListener)
			throws Exception {
		this(null, isUSB, serialListener);
	}

	public SerialCommandManager(String serialPort, boolean isUSB,
			SerialEventListener serialListener) throws Exception {
		super(serialPort, isUSB, serialListener);
		//Initialise the queue
		queue = new ConcurrentLinkedQueue<FCCommand>();
		//Initialise the input Stream to receive return of commands
		if(serialListener.getInputStream() == null) {
			serialListener.setInputStream(this.serialPort.getInputStream());
		}
	}
    
    /**
     * Sets the stream to which commands are send
     * @param out The output stream
     */
    public void setOutputStream(OutputStream out){
    	outputStream = out;
    	this.encoder.setWriter(out);
    }
	
	
	/**
	 * Stops the command sending thread and closes the connection
	 */
	public void stop(){
		doStop = true;
		// serialPort is a shared resource; needs checking before closing.
//		serialPort.close();
	}

	public void takeoff() {
		ExternControlCommand cmd = new ExternControlCommand(0, 0, 0, 15, true);
		queue.add(cmd);
	}

	public void sendCommand(MKCommand cmd) {
		int[]params = cmd.getParam().getAsInt();
		this.encoder.sendCommandNoCheck((byte)cmd.getAddress(), cmd.getId(), params);
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * <li> Sticky commands need to be send continuously.
	 * <li> New sticky commands replace previous one
	 * <li> 
	 * @see java.lang.Runnable#run()
	 */
	// TODO wait if the queue is empty.
	@Override
	public void run() {
		System.out.println("Running SerialCommManager");
		FCCommand cmdToSend = null;
		FCCommand newCmd = null;
		FCCommand stickyCmd = null;
		
		//Command-send loop
		while(!doStop){
			newCmd = queue.poll();
			
			if(newCmd == null){
//				no new command to send. Resend sticky command, if there is one.
				if(stickyCmd != null)
					cmdToSend = stickyCmd;
			}
			else{
//				A new commmand to send
				System.out.println("new command arriving");
				cmdToSend = newCmd;
				if(newCmd.isSticky()) //If new command is sticky, replace previous sticky command
					stickyCmd = newCmd;
				else
					stickyCmd = null;
			}
			if(cmdToSend != null)
			{
//				System.out.println("Sending command " + cmdToSend.getTitle());
				sendCommand(cmdToSend);
			}
			cmdToSend = null;
		}

//		while(!doStop){
//			cmd = queue.poll();
//			if(cmd != null){
//				//System.out.println("Sending command...");
//				sendCommand(cmd);	
//			}
////			System.out.println("SerialComm iteration");
//		}
	}

	public SerialCommandManager spinRight(int speed) {
		//TODO should the throttle argument be the current throttle?
		long throttle = FlightInfo.naviData.Gas.getValue();
		ExternControlCommand cmd = new ExternControlCommand(0, 0, speed, (int)throttle, true );
		cmd.setTitle("spin right");
		queue.add(cmd);
		return this;
	}
	
	public SerialCommandManager spinLeft(int speed) {
		//TODO should the throttle argument be the current throttle?
		long throttle = FlightInfo.naviData.Gas.getValue();
		ExternControlCommand cmd = new ExternControlCommand(0, 0, -speed, (int) throttle, true);
		cmd.setTitle("spin left");
		queue.add(cmd);
		return this;
	}

	public SerialCommandManager forward(int speed) {
		//TODO should the throttle argument be the current throttle?
		long throttle = FlightInfo.naviData.Gas.getValue();
		ExternControlCommand cmd = new ExternControlCommand(0, 0, speed, (int) throttle, true);
		cmd.setTitle("forward");
		queue.add(cmd);
		return this;
	}
	
	public SerialCommandManager backward(int speed) {
		long throttle = FlightInfo.naviData.Gas.getValue();
		ExternControlCommand cmd = new ExternControlCommand(0, 0, -speed, (int) throttle, true);
		cmd.setTitle("backward");
		queue.add(cmd);
		return this;
	}

	public SerialCommandManager landing() {
		// TODO Test if drone falls abruptly or softly descends
		ExternControlCommand cmd = new ExternControlCommand(0, 0, 0, 0, false);
		cmd.setTitle("landing");
		queue.add(cmd);
		return this;
	}

	public SerialCommandManager freeze() {
		long throttle = FlightInfo.naviData.Gas.getValue();
		ExternControlCommand cmd = new ExternControlCommand(0, 0, 0, (int) throttle, true);
		cmd.setTitle("freeze");
		queue.add(cmd);
		return this;
	}

	/**
	 * Move the drone left or right
	 * @param speed value between -128 and 127.
	 * We assume positive values to the left and negative to the right
	 */
	public SerialCommandManager roll(int speed) {
		long throttle = FlightInfo.naviData.Gas.getValue();
		ExternControlCommand cmd = new ExternControlCommand(0, speed, 0, (int) throttle, true);
		cmd.setTitle("roll");
		queue.add(cmd);
		return this;
	}

	/**
	 * Wait (sleep) for specified amount of time (same semantics as after() and waitFor() - 
	 * blocks the calling thread).
	 * This way commands can be executed for a certain amount of time, e.g. fly forward for 2000 ms, then turn right.
	 * @param millis  Number of milliseconds to wait
	 */
	public SerialCommandManager doFor(long millis)
	{
		return waitFor(millis);
	}
	
	/**
	 * Wait (sleep) for specified amount of time (same semantics as doFor() and waitFor() - blocks the calling thread).
	 * This way commands can be executed for a certain amount of time, e.g. fly forward for 2000 ms, then turn right.
	 * @param millis  Number of milliseconds to wait
	 */
	public SerialCommandManager after(long millis)
	{
		return waitFor(millis);
	}

	private SerialCommandManager waitFor(long millis) {
		try
		{
			Thread.sleep(millis);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		return this;
	}


}
