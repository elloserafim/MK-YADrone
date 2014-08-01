package de.yadrone.base.mkdrone.navdata;

import java.util.ArrayList;
import java.util.Observable;

import de.yadrone.base.datatypes.str_DebugOut;
import de.yadrone.base.manager.SerialAbstractManager;
import de.yadrone.base.manager.SerialCommandManager;
import de.yadrone.base.manager.SerialEventListener;

public class SerialNavManager extends SerialAbstractManager {

	private SerialCommandManager manager;
	
	private ArrayList<NCAnalogListener> ncAnalogListener;
	
	private Object data = null;
	
	public SerialNavManager(SerialCommandManager manager, SerialEventListener serialListener) throws Exception {
		super(manager.getSerialPort(), manager.isUSB(), serialListener);
	}
	
	public SerialNavManager(String serialPort, boolean isUSB,
			SerialEventListener serialListener, SerialCommandManager manager) throws Exception {
		super(serialPort, isUSB, serialListener);
		this.manager = manager;
		if(serialListener.getInputStream() == null) {
			serialListener.setInputStream(this.serialPort.getInputStream());
		}
	}
	
	public void addNCAnalogListener(NCAnalogListener listener) {
		if(ncAnalogListener == null) {
			ncAnalogListener = new ArrayList<NCAnalogListener>();
		}
		ncAnalogListener.add(listener);
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				synchronized (this) {
					while (data == null) {
						this.wait();
					}
					if(data instanceof str_DebugOut) {
						if(!ncAnalogListener.isEmpty()) {
							for (NCAnalogListener listener : ncAnalogListener) {
								listener.receivedAnalogData((str_DebugOut) data);
							}
						}
					}
					data = null;
					this.notify();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			data = null;
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		try {
			synchronized (this) {
				while (data != null) {
					this.wait();
				}
				data = arg;
				this.notify();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		if(arg instanceof str_DebugOut) {
//			if(!ncAnalogListener.isEmpty()) {
//				for (NCAnalogListener listener : ncAnalogListener) {
//					listener.receivedAnalogData((str_DebugOut) arg);
//				}
//			}
//		}
	}

}
