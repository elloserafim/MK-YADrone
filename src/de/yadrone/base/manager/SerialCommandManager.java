package de.yadrone.base.manager;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import de.yadrone.base.mkdrone.navdata.SerialEventListener;


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
		 	this.enconder = new Encoder(outputStream);
		 }

/*    public static HashMap<String, CommPortIdentifier> getPorts() {
        if (portMap == null) {
            portMap = new HashMap<String, CommPortIdentifier>();
            Enumeration portList = CommPortIdentifier.getPortIdentifiers();
            while (portList.hasMoreElements()) {
                portId = (CommPortIdentifier) portList.nextElement();
                if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    portMap.put(portId.getName(), portId);
                }
            }
        }
        return portMap;
    }*/
    
    public void setOutputStream(OutputStream out){
    	outputStream = out;
    }
	
/*	public SerialCommandManager(boolean isUSB, String port) 
	{
		//getPorts();

		this.isUSB = isUSB; 
		String defaultPort;
		String osname = System.getProperty("os.name", "").toLowerCase();
        if (osname.startsWith("windows")) {
            // windows
            defaultPort = "COM1";
        } else if (osname.startsWith("linux")) {
            // linux
            defaultPort = "/dev/ttyS0";
        } else if (osname.startsWith("mac")) {
            // mac
            defaultPort = "????";
        } else {
            System.out.println("Sorry, your operating system is not supported");
            return;
        }
        
        if (port!=null)
        	defaultPort = port;
        	
        
        
        if (!portMap.keySet().contains(defaultPort)) {
            System.out.println("port " + defaultPort + " not found.");
            System.exit(0);
        }
        
        portId = portMap.get(defaultPort);
        try {
			serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
		} catch (PortInUseException e) {
			// TODO Auto-generated catch block
			System.out.println("Port" + defaultPort + " already in use");
			e.printStackTrace();
		}
        
     //   initWriteToPort();
	//	this.enconder = new Encoder(outputStream);
        
        // set port parameters
        try {
			serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8,
			        SerialPort.STOPBITS_1,
			        SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
    public void initWriteToPort() {
        // initwritetoport() assumes that the port has already been opened and
        //    initialized by "public nulltest()"

        try {
            // get the outputstream
            outputStream = serialPort.getOutputStream();
        } catch (IOException e) {
        }

        try {
            // activate the OUTPUT_BUFFER_EMPTY notifier
            // DISABLE for USB
            //System.out.println(serialPort.getName());
            if (!isUSB) {
                serialPort.notifyOnOutputEmpty(true);
            }
        } catch (Exception e) {
            System.out.println("Error setting event notification");
            System.out.println(e.toString());
            System.exit(-1);
        }

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
