package de.yadrone.base.mkdrone.navdata;

import java.util.ArrayList;
import java.util.Observable;

import de.yadrone.base.datatypes.NaviData_t;
import de.yadrone.base.datatypes.str_DebugOut;
import de.yadrone.base.manager.SerialAbstractManager;
import de.yadrone.base.manager.SerialCommandManager;
import de.yadrone.base.manager.SerialEventListener;
import de.yadrone.base.mkdrone.command.DebugRequestCommand;
import de.yadrone.base.mkdrone.command.RedirectUARTCommand;

public class SerialNavManager extends SerialAbstractManager {

	private SerialCommandManager manager;
	
	private ArrayList<NCAnalogListener> ncAnalogListeners;
	private ArrayList<FCAnalogListener> fcAnalogListeners;
	private ArrayList<NCOSDListener> ncOSDListeners;
	
	private Object data = null;
	
	public SerialNavManager(SerialCommandManager manager, SerialEventListener serialListener) throws Exception {
		super(manager.getSerialPort(), manager.isUSB(), serialListener);
		this.manager = manager;
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
		if(ncAnalogListeners == null) {
			ncAnalogListeners = new ArrayList<NCAnalogListener>();
		}
		ncAnalogListeners.add(listener);
		manager.sendCommand(new DebugRequestCommand(NC_ADDRESS, 100));
	}
	
	public void addFCAnalogListener(FCAnalogListener listener) {
		if(fcAnalogListeners == null) {
			fcAnalogListeners = new ArrayList<FCAnalogListener>();
		}
		fcAnalogListeners.add(listener);
		manager.sendCommand(new DebugRequestCommand(FC_ADDRESS, 100));
	}
	
	public void addOSDListener(NCOSDListener listener) {
		if(ncOSDListeners == null){
			ncOSDListeners = new ArrayList<NCOSDListener>();
		}
		ncOSDListeners.add(listener);
	}
	
	// TODO address for NC
	public void redirectUART(String address) {
		long value = -1; // default is NC
		if(address.equals("FC")) {
			value = 0;
		} else if(address.equals("MK3MAG")) {
			value = 1;
		} else if(address.equals("MKGPS")) {
			value = 2;
		}
		manager.sendCommand(new RedirectUARTCommand(value));
	}
	
	@Override
	public void run() {
		while (!doStop) {
			synchronized (this) {
				try {
					while (data == null) {
						this.wait();
					}
					if (data instanceof str_DebugOut) {
						if (ncAnalogListeners != null
								&& !ncAnalogListeners.isEmpty()) {
							for (NCAnalogListener listener : ncAnalogListeners) {
								listener.receivedAnalogData((str_DebugOut) data);
							}
						} else if (fcAnalogListeners != null
								&& !fcAnalogListeners.isEmpty()) {
							for (FCAnalogListener listener : fcAnalogListeners) {
								listener.receivedAnalogData((str_DebugOut) data);
							}
						}
					} else if (data instanceof NaviData_t) {
						if (ncOSDListeners != null && !ncOSDListeners.isEmpty()) {
							for (NCOSDListener listener : ncOSDListeners) {
								listener.receivedOSDData((NaviData_t) data);
							}
						}
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					data = null;
					this.notify();
				}
			}
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
	}

}
