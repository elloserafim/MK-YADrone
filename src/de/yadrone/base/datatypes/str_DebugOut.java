/*
 *  Copyright (C) 2010-2011 by Claas Anders "CaScAdE" Rathje
 *  admiralcascade@gmail.com
 *  Licensed under: Creative Commons / Non Commercial / Share Alike
 *  http://creativecommons.org/licenses/by-nc-sa/2.0/de/
 *
 */
package de.yadrone.base.datatypes;

import java.util.LinkedList;

/**
 * Debug Sturct
 * adapted from: http://svn.mikrokopter.de/filedetails.php?repname=FlightCtrl&path=/tags/V0.82a/uart.h
 * @author Claas Anders "CaScAdE" Rathje
 */
public class str_DebugOut extends c_int {

    public u8 Status[] = new u8[2];
    public s16Debug Analog[] = new s16Debug[32];    // Debug values
    private int address;
    
    public str_DebugOut(int RxdBuffer[], int pRxData) {
    	allAttribs = new LinkedList<c_int>();
    	for (int i = 0; i < Status.length; i++) {
            Status[i] = new u8();
            allAttribs.add(Status[i]);
        }
    	for (int i = 0; i < Analog.length; i++) {
            Analog[i] = new s16Debug();
            allAttribs.add(Analog[i]);
        }
    	loadFromInt(RxdBuffer, pRxData);
    }

    public str_DebugOut(String prefix, int ADDRESS) {
        this.address = ADDRESS;
        allAttribs = new LinkedList<c_int>();

        for (int i = 0; i < Status.length; i++) {
            Status[i] = new u8(prefix + " Status " + i);
            allAttribs.add(Status[i]);
        }
        
        Status[0].value=255;
        
        for (int i = 0; i < Analog.length; i++) {
            Analog[i] = new s16Debug(prefix + "Analog ", i, ADDRESS);
            allAttribs.add(Analog[i]);
        }

        // TODO: see if the serialize pool under DataStorage is relevant
//        for (c_int c : allAttribs) {
//            DataStorage.addToSerializePool(c);
//        }
    }
    
    public void setAddress(int address) {
    	this.address = address;
    }
};
