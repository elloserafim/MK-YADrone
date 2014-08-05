package de.yadrone.base.manager;

import gnu.io.CommPortIdentifier;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.ConcurrentLinkedQueue;
import de.yadrone.base.mkdrone.command.ExternControlCommand;
import de.yadrone.base.mkdrone.command.FCCommand;
import de.yadrone.base.mkdrone.command.MKCommand;

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
		ExternControlCommand cmd = new ExternControlCommand(0, 0, 0, 15);
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

	// TODO wait if the queue is empty.
	@Override
	public void run() {
		System.out.println("Running SerialCommManager");
		FCCommand cmd;
		while(!doStop){
			cmd = queue.poll();
			if(cmd != null){
				//System.out.println("Sending command...");
				sendCommand(cmd);	
			}
//			System.out.println("SerialComm iteration");
		}
	}

	public void spinRight(int speed) {
		//TODO should the throttle argument be the current throttle?
		ExternControlCommand cmd = new ExternControlCommand(0, 0, speed, 15);
		queue.add(cmd);
	}
	
	public void spinLeft(int speed) {
		//TODO should the throttle argument be the current throttle?
		ExternControlCommand cmd = new ExternControlCommand(0, 0, -speed, 15);
		queue.add(cmd);
	}

	public void forward(int speed) {
		//TODO should the throttle argument be the current throttle?
		ExternControlCommand cmd = new ExternControlCommand(0, 0, -speed, 15);
		
	}

}
