package de.yadrone.base.mkdrone.navdata;

import java.util.Observable;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class SerialEventListener extends Observable implements SerialPortEventListener {

	/**
    *
    * @param event
    * @see http://www.mikrokopter.de/ucwiki/en/SerialProtocol
    */
   public void serialEvent(SerialPortEvent event) {

       //"0x1B,0x1B,0x55,0xAA,0x00"
//       byte[] pattern = new byte[]{27, 27, 85, (byte) 170, 0};

       switch (event.getEventType()) {
           case SerialPortEvent.BI:
           case SerialPortEvent.OE:
           case SerialPortEvent.FE:
           case SerialPortEvent.PE:
           case SerialPortEvent.CD:
           case SerialPortEvent.CTS:
           case SerialPortEvent.DSR:
           case SerialPortEvent.RI:
           case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
               break;
           case SerialPortEvent.DATA_AVAILABLE:
//               try {
//                   while (inputStream.available() > 0) {
//
//                       Arrays.fill(readBuffer, (byte) 0);
//                       int numBytes = inputStream.read(readBuffer);
//                       byte[] data = Arrays.copyOfRange(readBuffer, 0, numBytes);
////       System.out.println(Hex.encodeHexString(readBuffer));
////       System.out.println(Hex.encodeHexString(data));
//
//                       HandleInputData(data);
//                   }
//               } catch (IOException ex) {
//               }
               break;
       }
   }

}
