/*
 *  Copyright (C) 2010-2011 by Claas Anders "CaScAdE" Rathje
 *  admiralcascade@gmail.com
 *  Licensed under: Creative Commons / Non Commercial / Share Alike
 *  http://creativecommons.org/licenses/by-nc-sa/2.0/de/
 *
 */
package de.yadrone.base.datatypes;

/**
 * 16bit signed debug-int with label
 *
 * @author Claas Anders "CaScAdE" Rathje
 */
public class s16Debug extends s16 {

    int index = 0;
    String prefix;
    public int ADDRESS;

    public s16Debug(String name, int index, int address) {
        signed = true;
        length = 16;
        this.name = name;
        this.prefix = name;
        this.index = index;
        this.ADDRESS = address;
    }

    public int[] getLabelArray() {
        int[] ret = new int[16];
        String s = name + " " + index;
        if (s.length() > 16) {
            s = s.substring(0, 16);
        }

        int i = 0;
        for (char c : s.toCharArray()) {
            ret[i++] = (int) c;
        }
        for (; i < ret.length; i++) {
            ret[i] = (int) ' ';
        }
        //ret[15] = 0;

        return c_int.concatArray(new int[]{index}, ret);
    }


    public void setName(String get) {
        this.name = get;
    }
}

