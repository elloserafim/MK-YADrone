package de.yadrone.base.manager;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;

import de.yadrone.base.mkdrone.command.Encoder;
import de.yadrone.base.mkdrone.command.ExternControlCommand;
import de.yadrone.base.mkdrone.command.FCCommand;

/**
 * The manager for Serial Communication with MKDrone
 * @author Ello Oliveira 
 *
 */

// [DPPB]: This class should be placed under de.yadrone.base.mkdrone.command, in accordance
// with YADrone's design.
public class SerialCommandManager extends SerialAbstractManager implements Runnable {
	
	static CommPortIdentifier portId;
    static HashMap<String, CommPortIdentifier> portMap;
    private boolean isUSB;
	protected Thread thread = null;
	
	public SerialCommandManager(SerialEventListener serialListener) throws Exception {
		 this(null, false, serialListener);
	}
	public SerialCommandManager(String serialPort, boolean isUSB, 
			SerialEventListener serialListener) throws Exception {
		 	super(serialPort, isUSB, serialListener);
		 }


    
    public void setOutputStream(OutputStream out){
    	outputStream = out;
    }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public void takeoff() {
		// TODO Auto-generated method stub
		ExternControlCommand cmd = new ExternControlCommand(0, 0, 0, 15);
		sendCommand(cmd);
	}

	private void sendCommand(FCCommand cmd) {
		// TODO Auto-generated method stub
		int[]params = cmd.getParam().getAsInt();
		this.enconder.sendCommandNoCheck((byte)cmd.getAddress(), cmd.getId(), params);
	}

	public void stop() {
		// TODO Auto-generated method stub
		serialPort.close();
		
	}

}
