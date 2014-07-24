package de.yadrone.base.manager;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;

import de.yadrone.base.mkdrone.command.Encoder;
import de.yadrone.base.mkdrone.navdata.SerialEventListener;

/**
 * @author Ello Oliveira
 * This abstract class contain attributes and methods common to both SerialCommandManager and SerialNavManager. 
 * Attributes and methods in this class are necessary for Serial Communication 
 */
public abstract class SerialAbstractManager implements Runnable {
	
	/*
	 * MK commands is sent to one of the Mikrokopter components. Here are their addresses:
	 * (Refer to http://mikrokopter.de/ucwiki/en/SerialProtocol )
	 */
    public static final int ANY_ADDRESS = 0;
    public static final int FC_ADDRESS = 1;
    public static final int NC_ADDRESS = 2;
    public static final int MK3MAG_ADDRESS = 3;
    public static final int MKOSD_ADDRESS = 4;
    public static final int BL_ADDRESS = 5;

//	protected Thread thread;
//	protected String portId; //e.g. "COM1"
	protected SerialPort serialPort;
	protected OutputStream outputStream;
	protected InputStream inputStream;
	protected Encoder enconder;
	protected boolean isUSB;
	
	protected static HashMap<String, CommPortIdentifier> portMap;
	
	public SerialAbstractManager(SerialPort serialPort, boolean isUSB, SerialEventListener serialListener) throws Exception {
		
	}
	// A merge of SerialComm constructor, getPorts() and initwritetoport() from "de.mylifesucks.oss.ncsimulator.
	// protocol.SerialComm.java" with some improvements.
	public SerialAbstractManager(String serialPort, boolean isUSB, SerialEventListener serialListener) throws Exception {
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
		if(serialPort == null) {
			serialPort = (String) portMap.keySet().toArray()[0];
		} else if(!portMap.keySet().contains(serialPort)) {
			// TODO: SerialPortNotFoundException
			throw new Exception("Serial port not found.");
		}
		this.serialPort = (SerialPort) portMap.get(serialPort).open("SimpleReadApp", 2000);
		inputStream = this.serialPort.getInputStream();
		this.serialPort.addEventListener(serialListener);
		this.serialPort.notifyOnDataAvailable(true);
		this.serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		outputStream = this.serialPort.getOutputStream();
		if(!isUSB) {
			this.serialPort.notifyOnOutputEmpty(true);
		}
		this.isUSB = isUSB;
	}
	
	public SerialPort getSerialPort() {
		return serialPort;
	}
	
	public boolean isUSB() {
		return isUSB;
	}
	
//	public void start() {
//		if (thread == null || thread.getState() == Thread.State.TERMINATED) {
//			String name = getClass().getSimpleName();
//			thread = new Thread(this, name);
//			thread.start();
//		}
//	}

}
