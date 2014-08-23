package de.yadrone.base.manager;

import gnu.io.CommPortIdentifier;

import java.util.HashMap;
import java.util.Observer;

/**
 * This abstract class contain attributes and methods common to both SerialCommandManager and SerialNavManager. 
 * @author Ello Oliveira and Diogo Branco
 */
public abstract class SerialAbstractManager implements Runnable, Observer {
	
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

	/** Flag to stop the thread. */
	protected boolean doStop;
	/** Thread which runs the manager. */
	private Thread thread;
	
	/**
	 * A mapping of the serial ports and their names 
	 */
	protected static HashMap<String, CommPortIdentifier> portMap;
	
	public SerialAbstractManager() {
		doStop = true;
	}
	
	/**
	 * Starts the manager.
	 * @return True if the manager started, false if it was already started
	 * when this function was called.
	 */
	public boolean start() {
		if (thread == null || thread.getState() == Thread.State.TERMINATED) {
			doStop = false;
			thread = new Thread(this, getClass().getSimpleName());
			thread.start();
			return true;
		}
		return false;
	}
	
	/**
	 * Waits for this manager to die.
	 * @throws InterruptedException if any thread has interrupted the current
	 * thread. The interrupted status of the current thread is cleared when
	 * this exception is thrown.
	 */
	public void join() throws InterruptedException {
		if(thread != null) {
			thread.join();
		}
	}

}
