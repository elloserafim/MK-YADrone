package de.yadrone.base.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Observable;

import de.yadrone.base.datatypes.NaviData_t;
import de.yadrone.base.datatypes.str_DebugOut;
import de.yadrone.base.mkdrone.flightdata.FlightInfo;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class SerialEventListener extends Observable implements SerialPortEventListener {

	public static enum UART_CONNECTION {

        FC, MK3MAG, MKGPS, NC
    }
	
	private char UartState;
	private volatile int RxdBuffer[] = new int[MAX_EMPFANGS_BUFF];
	private volatile boolean NeuerDatensatzEmpfangen = false;
	private int AnzahlEmpfangsBytes = 0;
	private int crc = 0;
	private int crc1;
	private int crc2;
	private int buf_ptr = 0;
//	private UART_CONNECTION UART;
	private InputStream inputStream;
	
	public static final int MAX_EMPFANGS_BUFF = 190;

	
	public SerialEventListener() {
		UartState = 0;
//		UART = UART_CONNECTION.NC;
	}
	
	private void interpretData() {
		if (!NeuerDatensatzEmpfangen) {
			return;
		}
		int pRxData = 3; // decoded data start at the 4th byte
		Decode64(RxdBuffer);
//		System.out.print("Decoded data: ");
//		for(int i = 0; i < RxdBuffer.length; ++i) {
//			System.out.print(RxdBuffer[i]);
//		}
//		System.out.println();
		switch (RxdBuffer[1] - 'a') {
		case SerialAbstractManager.NC_ADDRESS:
			switch (RxdBuffer[2]) {
			case 'A': // NCanalog label
				System.out.println(RxdBuffer[pRxData]);
				char[] label = new char[16];
				for(int i = 1; i < 17; ++i) {
					label[i-1] = (char) RxdBuffer[pRxData+i];
				}
				System.out.println(new String(label));
				break;
			case 'D': // NCAnalog data
				str_DebugOut debugOut = new str_DebugOut(RxdBuffer, pRxData);
				debugOut.setAddress(SerialAbstractManager.NC_ADDRESS);
				setChanged();
				notifyObservers(debugOut);
				break;
			case 'O': //NC Navi data
				NaviData_t navData = new NaviData_t(RxdBuffer, pRxData);
				FlightInfo.naviData = navData;
				setChanged();
				notifyObservers(navData);
			}
			break;
		case SerialAbstractManager.FC_ADDRESS:
			switch (RxdBuffer[2]) {
			case 'D': // NCAnalog data
				str_DebugOut debugOut = new str_DebugOut(RxdBuffer, pRxData);
				debugOut.setAddress(SerialAbstractManager.FC_ADDRESS);
				setChanged();
				notifyObservers(debugOut);
				break;
			}
			break;
		case SerialAbstractManager.MK3MAG_ADDRESS:
			break;
		default:
			
		}
	}
	
	public void Decode64(int[] RxdBuffer) {// die daten werden im rx buffer dekodiert, das geht nur, weil aus 4 byte immer 3 gemacht werden.
        int a, b, c, d;
        int x, y, z;
        int ptrIn = 3; // start at begin of data block
        int ptrOut = 3;
        int len = AnzahlEmpfangsBytes - 6; // von der Gesamtbytezahl eines Frames gehen 3 Bytes des Headers  ('#',Addr, Cmd) und 3 Bytes des Footers (CRC1, CRC2, '\r') ab.


        while (len != 0) {
            a = 0;
            b = 0;
            c = 0;
            d = 0;
            try {
                a = RxdBuffer[ptrIn++] - '=';
                b = RxdBuffer[ptrIn++] - '=';
                c = RxdBuffer[ptrIn++] - '=';
                d = RxdBuffer[ptrIn++] - '=';
            } catch (Exception e) {
            }

            x = (a << 2) | (b >> 4);
            y = ((b & 0x0f) << 4) | (c >> 2);
            z = ((c & 0x03) << 6) | d;

            if ((len--) != 0) {
                RxdBuffer[ptrOut++] = x;
            } else {
                break;
            }
            if ((len--) != 0) {
                RxdBuffer[ptrOut++] = y;
            } else {
                break;
            }
            if ((len--) != 0) {
                RxdBuffer[ptrOut++] = z;
            } else {
                break;
            }
        }
    }
	
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	private void USART0_RX_vect(int SioTmp) {
		
		boolean CrcOkay = false;
        if (buf_ptr >= MAX_EMPFANGS_BUFF) {
            UartState = 0;
//            System.out.println("overflow");
        }
        if (SioTmp == '\r' && UartState == 2) {
            UartState = 0;
            crc -= RxdBuffer[buf_ptr - 2];
            crc -= RxdBuffer[buf_ptr - 1];
            crc %= 4096;
            crc1 = '=' + crc / 64;
            crc2 = '=' + crc % 64;
            CrcOkay = false;
            if ((crc1 == RxdBuffer[buf_ptr - 2]) && (crc2 == RxdBuffer[buf_ptr - 1])) {
                CrcOkay = true;
            } else {
                CrcOkay = false;

            }
            if (!NeuerDatensatzEmpfangen && CrcOkay) { // Datensatz schon verarbeitet

                NeuerDatensatzEmpfangen = true;
                AnzahlEmpfangsBytes = buf_ptr + 1;
                RxdBuffer[buf_ptr] = '\r';

                /*if (RxdBuffer[2] == 'R') {
                 LcdClear();
                 wdt_enable(WDTO_250MS); // Reset-Commando
                 ServoActive = 0;
                
                 }*/
                
//                hasChanged();
//                notifyObservers(RxdBuffer);
                interpretData();

            } else {
                System.out.println("NeuerDatensatzEmpfangen: " + NeuerDatensatzEmpfangen + " CrcOkay: " + CrcOkay);
            }
        } else {
            switch (UartState) {
                case 0:
                    if (SioTmp == '#' && !NeuerDatensatzEmpfangen) {
                        UartState = 1;  // Startzeichen und Daten schon verarbeitet
                    }
                    buf_ptr = 0;
                    RxdBuffer[buf_ptr++] = SioTmp;
                    crc = SioTmp;
                    break;
                case 1: // Adresse auswerten
                    UartState++;
                    RxdBuffer[buf_ptr++] = SioTmp;
                    crc += SioTmp;
                    break;
                case 2: //  Eingangsdaten sammeln
                    RxdBuffer[buf_ptr] = SioTmp;
                    if (buf_ptr < MAX_EMPFANGS_BUFF) {
                        buf_ptr++;
                    } else {
                        UartState = 0;
                    }
                    crc += SioTmp;
                    break;
                default:
                    UartState = 0;
                    break;
            }
        }
    }
	
	private void HandleInputData(byte[] data) {
		for (int i = 0; i < data.length; i++) {
//			if (i < data.length - 5
//					&& (data[i] == 0x1B && data[i + 1] == 0x1B
//							&& data[i + 2] == 0x55 /* && data[i + 3] == 170 */&& data[i + 4] == 0x00)) {
//				i += 4;
//				setUART(UART_CONNECTION.NC);
//				UartState = 0;
//			} else {
				USART0_RX_vect((char) data[i]);
//			}
		}
	}

	byte[] readBuffer = new byte[220];
	
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
               try {
                   while (inputStream.available() > 0) {
//                	   System.out.println("New data received.");
                       Arrays.fill(readBuffer, (byte) 0);
                       int numBytes = inputStream.read(readBuffer);
                       byte[] data = Arrays.copyOfRange(readBuffer, 0, numBytes);
////       System.out.println(Hex.encodeHexString(readBuffer));
////       System.out.println(Hex.encodeHexString(data));
//
                       HandleInputData(data);
                   }
               } catch (IOException ex) {
               }
               break;
       }
   }
   
//   public void setUART(UART_CONNECTION u) {
//	   UART = u;
//   }

}
