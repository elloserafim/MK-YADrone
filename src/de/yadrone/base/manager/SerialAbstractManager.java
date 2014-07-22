package de.yadrone.base.manager;

import gnu.io.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class SerialAbstractManager implements Runnable {
    public static final int ANY_ADDRESS = 0;
    public static final int FC_ADDRESS = 1;
    public static final int NC_ADDRESS = 2;
    public static final int MK3MAG_ADDRESS = 3;
    public static final int MKOSD_ADDRESS = 4;
    public static final int BL_ADDRESS = 5;

	protected Thread thread;
	protected String port;
	protected SerialPort serialPort;
	static OutputStream outputStream;
	static InputStream inputStream;
	
	public static OutputStream getOutputStream() {
		return outputStream;
	}
	public static InputStream getInputStream() {
		return inputStream;
	}

	public void start() {
		if (thread == null || thread.getState() == Thread.State.TERMINATED) {
			String name = getClass().getSimpleName();
			thread = new Thread(this, name);
			thread.start();
		}
	}

}
