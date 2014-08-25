package de.yadrone.base.manager;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import de.yadrone.base.ardrone.command.CommandManager;
import de.yadrone.base.exception.CommandException;
import de.yadrone.base.mkdrone.command.Encoder;
import de.yadrone.base.mkdrone.command.ExternControlCommand;
import de.yadrone.base.mkdrone.command.FCCommand;
import de.yadrone.base.mkdrone.command.MKCommand;
import de.yadrone.base.mkdrone.command.VersionRequestCommand;
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
	
	protected SerialPort serialPort;
	/** Encoder to send serial messages. */
	protected Encoder encoder;
	/** Flag to indicate the connection is via USB. */
	protected boolean isUSB;
	/**
	 *  This variable stores information about the selected port
	 */
	static CommPortIdentifier portId;
    static HashMap<String, CommPortIdentifier> portMap;
	private ConcurrentLinkedQueue<MKCommand> queue;
	
	public SerialCommandManager(boolean isUSB, SerialEventListener serialListener)
			throws Exception {
		this(null, isUSB, serialListener);
	}

	public SerialCommandManager(String serialPortName, boolean isUSB,
			SerialEventListener serialListener) throws Exception {
		//Initialise portMap:
		if(portMap == null) {
			portMap = new HashMap<String, CommPortIdentifier>();
	        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
	        CommPortIdentifier portId;
	        while (portList.hasMoreElements()) {
	            portId = (CommPortIdentifier) portList.nextElement();
	            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
	                portMap.put(portId.getName(), portId);
	            }
	        }
		}
        if(portMap.isEmpty()) {
        	// TODO: SerialPortNotFoundException
        	throw new Exception("No serial ports were found.");
        }
        
        // Search a port with the name passed by argument serialPortName.
		if(serialPortName == null) {
			int i = 0;
			serialPortName = (String) portMap.keySet().toArray()[0];
			CommPortIdentifier pId = portMap.get(serialPortName);
			while(pId.isCurrentlyOwned() && i< portMap.size()){
				serialPortName = (String) portMap.keySet().toArray()[++i];
				pId = portMap.get(serialPortName);
			}
			if(pId.isCurrentlyOwned() && i>= portMap.size()){
				throw new Exception("All Serial ports are in use.");
			}
		} else if(!portMap.keySet().contains(serialPortName)) {
			// TODO: SerialPortNotFoundException
			throw new Exception("Serial port not found.");
		}
		
		// Open the port and set its parameters:
		System.out.println("Connecting to port " + serialPortName);
		this.serialPort = (SerialPort) portMap.get(serialPortName).open("SimpleReadApp", 2000);
		
		this.serialPort.addEventListener(serialListener);
		this.serialPort.notifyOnDataAvailable(true);
		this.serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		if(!isUSB) {
			this.serialPort.notifyOnOutputEmpty(true);
		}
		this.isUSB = isUSB;
		encoder = new Encoder(this.serialPort.getOutputStream());
		serialListener.addObserver(this);
		//Initialise the queue
		queue = new ConcurrentLinkedQueue<MKCommand>();
		//Initialise the input Stream to receive return of commands
		if(serialListener.getInputStream() == null) {
			serialListener.setInputStream(this.serialPort.getInputStream());
		}
	}
	
	public SerialPort getSerialPort() {
		return serialPort;
	}
	
	public boolean isUSB() {
		return isUSB;
	}
	
	/**
	 * Stops the command sending thread and closes the connection
	 */
	public boolean stop(){
		if(!doStop) {
			doStop = true;
			serialPort.close();
			return true;
		}
		return false;
	}

	public void takeoff() {
		ExternControlCommand cmd = new ExternControlCommand(0, 0, 0, 15, true);
		queue.add(cmd);
	}
	
	public boolean queueCommand(MKCommand cmd) {
		return queue.add(cmd);
	}

	private void sendCommand(MKCommand cmd) {
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
	@Override
	public void run() {
		System.out.println("Running SerialCommManager");
		MKCommand cmdToSend = null;
		MKCommand newCmd = null;
		MKCommand stickyCmd = null;
		
		//Request for OSD-data
//		encoder.sendCommand(SerialAbstractManager.NC_ADDRESS, 'o',
//		new int[] { 10 });
		
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
				System.out.println("new command in the queue");
				cmdToSend = newCmd;
				if(newCmd instanceof FCCommand && ((FCCommand) newCmd).isSticky()) //If new command is sticky, replace previous sticky command
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

	public SerialCommandManager moveVertically(int value) {
		long throttle = FlightInfo.naviData.Gas.getValue();
		ExternControlCommand cmd = new ExternControlCommand(0, 0, 0, (int) throttle+value, true);
		cmd.setTitle("move vertically");
		queue.add(cmd);
		return this;
		
	}

	public void requestFCVersion() {
		VersionRequestCommand cmd = new VersionRequestCommand(FC_ADDRESS);
		queue.add(cmd);
		
	}


}
