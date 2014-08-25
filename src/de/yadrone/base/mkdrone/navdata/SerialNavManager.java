package de.yadrone.base.mkdrone.navdata;

import java.util.ArrayList;
import java.util.Observable;

import de.yadrone.base.datatypes.NaviData_t;
import de.yadrone.base.datatypes.str_DebugOut;
import de.yadrone.base.datatypes.str_VersionInfo;
import de.yadrone.base.manager.SerialAbstractManager;
import de.yadrone.base.manager.SerialCommandManager;
import de.yadrone.base.manager.SerialEventListener;
import de.yadrone.base.manager.VersionListener;
import de.yadrone.base.mkdrone.command.DebugRequestCommand;
import de.yadrone.base.mkdrone.command.RedirectUARTCommand;

public class SerialNavManager extends SerialAbstractManager {

	private SerialCommandManager manager;
	
	private ArrayList<NCAnalogListener> ncAnalogListeners;
	private ArrayList<FCAnalogListener> fcAnalogListeners;
	private ArrayList<NCOSDListener> ncOSDListeners;
	private ArrayList<VersionListener> versionListeners;
	
	private Object data = null;
	
	/**
	 * Constructor for SerialNavManager. It uses the {@link SerialCommandManager}
	 * to queue some commands it needs to send, depending on the listeners added
	 * later. It registers as an observer of the {@link SerialEventListener},
	 * receiving then the data that arrives from said listener.
	 * @param manager manager to queue messages 
	 * @param serialListener serial event listener to receive data from
	 */
	public SerialNavManager(SerialCommandManager manager, SerialEventListener serialListener) {
		serialListener.addObserver(this);
		this.manager = manager;
	}
	
	/**
	 * Adds a NCAnalog listener to this manager.
	 * @param listener A defined {@link NCAnalogListener} interface.
	 */
	public void addNCAnalogListener(NCAnalogListener listener) {
		if(ncAnalogListeners == null) {
			ncAnalogListeners = new ArrayList<NCAnalogListener>();
		}
		ncAnalogListeners.add(listener);
		manager.queueCommand(new DebugRequestCommand(NC_ADDRESS, 100));
	}
	
	/**
	 * Adds a FCAnalog listener to this manager.
	 * @param listener A defined {@link FCAnalogListener} interface.
	 */
	public void addFCAnalogListener(FCAnalogListener listener) {
		if(fcAnalogListeners == null) {
			fcAnalogListeners = new ArrayList<FCAnalogListener>();
		}
		fcAnalogListeners.add(listener);
		manager.queueCommand(new DebugRequestCommand(FC_ADDRESS, 100));
	}
	
	/**
	 * Adds an OSD listener to this manager.
	 * @param listener A defined {@link NCOSDListener} interface.
	 */
	public void addOSDListener(NCOSDListener listener) {
		if(ncOSDListeners == null){
			ncOSDListeners = new ArrayList<NCOSDListener>();
		}
		ncOSDListeners.add(listener);
	}
	
	public void addVersionListener(VersionListener listener){
		if(versionListeners == null){
			versionListeners = new ArrayList<VersionListener>();
		}
		versionListeners.add(listener);
	}

	/**
	 * Queues a "Redirect UART" message to the {@link SerialCommandManager}.
	 * 
	 * @param address
	 *            The available addresses are: FC, MK3MAG, MKGPS and NC.
	 */
	public void redirectUART(String address) {
		long value = -1;
		if(address.equals("FC")) {
			value = 0;
		} else if(address.equals("MK3MAG")) {
			value = 1;
		} else if(address.equals("MKGPS")) {
			value = 2;
		} else if(address.equals("NC")) {
			value = 4;
		} else {
			return;
		}
		manager.queueCommand(new RedirectUARTCommand(value));
	}
	
	/**
	 * Main method for this manager. This is where it gets the interpreted
	 * data and passes them to the proper listeners.
	 */
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
					else if(data instanceof str_VersionInfo){
						if(versionListeners !=null && !versionListeners.isEmpty() ){
							for(VersionListener listener : versionListeners){
								listener.receivedVersionInfo((str_VersionInfo)data);
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

	/**
	 * Update method for this Observer. The {@link SerialEventListener}
	 * sends the received and interpreted data through this method, since
	 * this manager observes the SerialEventListener passed as argument
	 * in the constructor.
	 */
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
	
	/**
	 * Stops the data processing thread.
	 * @return True if the manager stopped, false if the manager was not
	 * running when this function was called. 
	 */
	public boolean stop() {
		if(!doStop) {
			doStop = true;
			return true;
		}
		return false;
	}

}
