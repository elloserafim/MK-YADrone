package de.yadrone.base.mkdrone.command;

public class Encoder {
	
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
}
