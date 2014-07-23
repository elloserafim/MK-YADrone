package de.yadrone.base.mkdrone.command;

import java.io.OutputStream;



/**
 * @author Ello Oliveira
 * Class to encode and send MK commands
 */
public class Encoder {
	
    public java.io.OutputStream writer;
    
    public Encoder(OutputStream writer) {
        this.writer = writer;
    }

    public void setWriter(OutputStream writer) {
        this.writer = writer;
    }
	
    /**
    *
     * @param in_arr array of received data
     * @param offset the index where the first char to decode is
     * @param len the length of data to decode
    * @return array representing the decoded data
    *
    * @author  Marcus -LiGi- Bueschleb
    * http://github.com/ligi/DUBwise/blob/master/shared/src/org/ligi/ufo/MKCommunicator.java
    */
    public static int[] Decode64(byte[] in_arr, int offset, int len) {
        int ptrIn = offset;
        int a, b, c, d, x, y, z;
        int ptr = 0;

        int[] out_arr = new int[len];

        while (len != 0) {
            a = 0;
            b = 0;
            c = 0;
            d = 0;
            try {
                a = in_arr[ptrIn++] - '=';
                b = in_arr[ptrIn++] - '=';
                c = in_arr[ptrIn++] - '=';
                d = in_arr[ptrIn++] - '=';
            } catch (Exception e) {
            }

            x = (a << 2) | (b >> 4);
            y = ((b & 0x0f) << 4) | (c >> 2);
            z = ((c & 0x03) << 6) | d;

            if ((len--) != 0) {
                out_arr[ptr++] = x;
            } else {
                break;
            }
            if ((len--) != 0) {
                out_arr[ptr++] = y;
            } else {
                break;
            }
            if ((len--) != 0) {
                out_arr[ptr++] = z;
            } else {
                break;
            }
        }

        return out_arr;

    }
    
    /**
     * Encodes and sends a MK Command
     * @param address NC or FC address
     * @param cmdId Command Id as described in http://mikrokopter.de/ucwiki/en/SerialProtocol
     * @param params The data sent with the command, encoded in a int array
     * @author  Marcus -LiGi- Bueschleb
     * http://github.com/ligi/DUBwise/blob/master/shared/src/org/ligi/ufo/MKCommunicator.java
     */
    public synchronized void sendCommandNoCheck(byte address, char cmdId, int[] params) {
    	
    	

            byte[] send_buff = new byte[3 + (params.length / 3 + (params.length % 3 == 0 ? 0 : 1)) * 4]; // 5=1*start_char+1*addr+1*cmd+2*crc
            send_buff[0] = '#';
            send_buff[1] = (byte) (address + 'a');
            send_buff[2] = (byte) cmdId;

            for (int param_pos = 0; param_pos < (params.length / 3 + (params.length % 3 == 0 ? 0 : 1)); param_pos++) {
                int a = (param_pos * 3 < params.length) ? params[param_pos * 3] : 0;
                int b = ((param_pos * 3 + 1) < params.length) ? params[param_pos * 3 + 1] : 0;
                int c = ((param_pos * 3 + 2) < params.length) ? params[param_pos * 3 + 2] : 0;

                send_buff[3 + param_pos * 4] = (byte) ((a >> 2) + '=');
                send_buff[3 + param_pos * 4 + 1] = (byte) ('=' + (((a & 0x03) << 4) | ((b & 0xf0) >> 4)));
                send_buff[3 + param_pos * 4 + 2] = (byte) ('=' + (((b & 0x0f) << 2) | ((c & 0xc0) >> 6)));
                send_buff[3 + param_pos * 4 + 3] = (byte) ('=' + (c & 0x3f));

            }
            try {
                int tmp_crc = 0;
                for (int tmp_i = 0; tmp_i < send_buff.length; tmp_i++) {
                    tmp_crc += (int) send_buff[tmp_i];
                }



                writer.write(send_buff, 0, send_buff.length);
                tmp_crc %= 4096;



                writer.write((char) (tmp_crc / 64 + '='));
                writer.write((char) (tmp_crc % 64 + '='));
                writer.write('\r');
                writer.flush();


                String out = "";
                for (int i : send_buff) {
                    out += (char) i;
                }

                out += (char) (tmp_crc / 64 + '=');
                out += (char) (tmp_crc % 64 + '=');
                out += '\r';


            } catch (Exception e) { // problem sending data to FC
            }
        }
    
}
