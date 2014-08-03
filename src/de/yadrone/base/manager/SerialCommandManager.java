package de.yadrone.base.manager;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

import de.yadrone.base.mkdrone.command.Encoder;
import de.yadrone.base.mkdrone.command.ExternControlCommand;
import de.yadrone.base.mkdrone.command.FCCommand;

/**
 * The manager for Serial Communication with MKDrone
 * @author Ello Oliveira and Diogo Branco
 *
 */

// [DPPB]: This class should be placed under de.yadrone.base.mkdrone.command, in accordance
// with YADrone's design.
public class SerialCommandManager extends SerialAbstractManager implements Runnable {
	
	static CommPortIdentifier portId;
    static HashMap<String, CommPortIdentifier> portMap;
	protected Thread thread = null;
	private ConcurrentLinkedQueue<FCCommand> queue;
	protected boolean doStop = false;
	
	public SerialCommandManager(boolean isUSB, SerialEventListener serialListener)
			throws Exception {
		this(null, isUSB, serialListener);
	}

	public SerialCommandManager(String serialPort, boolean isUSB,
			SerialEventListener serialListener) throws Exception {
		super(serialPort, isUSB, serialListener);
		queue = new ConcurrentLinkedQueue<FCCommand>();
		if(serialListener.getInputStream() == null) {
			serialListener.setInputStream(this.serialPort.getInputStream());
		}
	}
    
    public void setOutputStream(OutputStream out){
    	outputStream = out;
    	this.encoder.setWriter(out);
    }
	
	
	public void stop(){
		doStop = true;
		serialPort.close();
	}

	public void takeoff() {
		// TODO Auto-generated method stub
		ExternControlCommand cmd = new ExternControlCommand(0, 0, 0, 15);
		queue.add(cmd);
//		sendCommand(cmd);
	}

	private void sendCommand(FCCommand cmd) {
		// TODO Auto-generated method stub
		int[]params = cmd.getParam().getAsInt();
		this.encoder.sendCommandNoCheck((byte)cmd.getAddress(), cmd.getId(), params);
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	public void start() {
		// TODO Auto-generated method stub
		Thread thread = new Thread(this, "SerialCommManager");
		thread.start();
	}

	@Override
	public void run() {
		System.out.println("Running SerialCommManager");
		FCCommand cmd;
		while(!doStop){
			cmd = queue.poll();
			if(cmd != null){
				sendCommand(cmd);
			}
			System.out.println("SerialComm iteration");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
