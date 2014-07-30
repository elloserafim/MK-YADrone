package de.yadrone.test;

import java.io.IOException;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import de.yadrone.base.MKDrone;
import de.yadrone.base.datatypes.u8;
import de.yadrone.base.manager.SerialAbstractManager;
import de.yadrone.base.manager.SerialEventListener;
import de.yadrone.base.mkdrone.command.Encoder;

public class TestSerialEventListener extends Thread {
	
	public static void main(String[] args) {
		Thread t = new TestSerialEventListener();
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void run() {
		try {
			String address = "COM1";
			SerialPort serialPort = null;
			Enumeration portList = CommPortIdentifier.getPortIdentifiers();
			CommPortIdentifier portId;
			while (portList.hasMoreElements()) {
				portId = (CommPortIdentifier) portList.nextElement();
				if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL
						&& portId.getName().equals(address)) {
					serialPort = (SerialPort) portId
							.open("SimpleReadApp", 2000);
					break;
				}
			}
			if (serialPort != null) {
				SerialEventListener listener = new SerialEventListener();
				listener.setInputStream(serialPort.getInputStream());
				serialPort.addEventListener(listener);
				serialPort.notifyOnDataAvailable(true);
				serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				serialPort.notifyOnOutputEmpty(true);
				Encoder encoder = new Encoder(serialPort.getOutputStream());
				encoder.sendCommand(SerialAbstractManager.NC_ADDRESS, 'a', new u8(1).getAsInt());
			}
		} catch (PortInUseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
		}
//		catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
//		try {
//			MKDrone drone = new MKDrone("COM1", false);
//			this.wait();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}
