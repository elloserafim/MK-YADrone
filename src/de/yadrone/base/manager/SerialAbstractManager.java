package de.yadrone.base.manager;

import gnu.io.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;

import de.yadrone.base.mkdrone.command.Encoder;

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

	protected Thread thread;
	protected String portId; //e.g. "COM1"
	protected SerialPort serialPort;
	OutputStream outputStream;
	InputStream inputStream;
	Encoder enconder;
	
	public void start() {
		if (thread == null || thread.getState() == Thread.State.TERMINATED) {
			String name = getClass().getSimpleName();
			thread = new Thread(this, name);
			thread.start();
		}
	}

}
