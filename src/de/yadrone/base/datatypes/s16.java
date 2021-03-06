/*
 *  Copyright (C) 2010-2011 by Claas Anders "CaScAdE" Rathje
 *  admiralcascade@gmail.com
 *  Licensed under: Creative Commons / Non Commercial / Share Alike
 *  http://creativecommons.org/licenses/by-nc-sa/2.0/de/
 *
 */
package de.yadrone.base.datatypes;


/**
 * 16bit signed int
 *
 * @author Claas Anders "CaScAdE" Rathje
 */
public class s16 extends c_int {

    public s16() {
        signed = true;
        length = 16;
        this.name = "";
    }

    public s16(String name) {
        signed = true;
        length = 16;
        this.name = name;
    }

    public s16(String name, int minValue, int maxValue) {
        signed = true;
        length = 16;
        this.name = name;
        this.minValue = new Integer(minValue);
        this.maxValue = new Integer(maxValue);
    }
}
