package de.yadrone.base.datatypes;



/**
 * @author Ello Oliveira
 * The data structure sent in ExternControl commands to control the drone 
 */
public class str_ExternControl extends c_int  {
    public u8 Digital[] = new u8[2];
    public u8 RemoteTasten;
    public s8 Pitch; //"Nick" in Mikrokopter wiki
    public s8 Roll;
    public s8 Yaw; //"Gier" in Mikrokopter wiki
    public u8 Gas;
    public s8 Hight;
    public u8 free;
    public u8 Frame;
    public u8 Config;
}
