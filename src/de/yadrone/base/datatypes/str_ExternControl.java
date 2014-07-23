package de.yadrone.base.datatypes;

import java.util.LinkedList;


/**
 * The data structure sent in ExternControl commands to control the drone 
 * Based in struct str_ExternControl in http://mikrokopter.de/mikrosvn/FlightCtrl/tags/V0.84a/uart.c
 * @author Ello Oliveira
 * 
 */
public class str_ExternControl extends c_int  {
    public u8 Digital[] = new u8[2];
    public u8 RemoteTasten;
    public s8 Pitch; //"Nick" in str_ExternControl
    public s8 Roll;
    public s8 Yaw; //"Gier" in str_ExternControl
    public u8 Gas;
    public s8 Hight;
    public u8 free;
    public u8 Frame;
    public u8 Config;
 
    public str_ExternControl(){
    super();
    allAttribs = new LinkedList<c_int>();
    for (int i = 0; i < Digital.length; i++) {
        Digital[i] = new u8("Digital" + i);
        allAttribs.add(Digital[i]);
    }
    RemoteTasten = new u8("RemoteTasten");
    allAttribs.add(RemoteTasten);
    Pitch = new s8("Pitch");
    allAttribs.add(Pitch);
    Roll = new s8("Roll");
    allAttribs.add(Roll);
    Yaw = new s8("Yaw");
    allAttribs.add(Yaw);
    Gas = new u8("Gas");
    allAttribs.add(Gas);
    Hight = new s8("Hight");
    allAttribs.add(Hight);
    free = new u8("free");
    allAttribs.add(free);
    Frame = new u8("Frame");
    allAttribs.add(Frame);
    Config = new u8("Config");
    allAttribs.add(Config);
}
}

