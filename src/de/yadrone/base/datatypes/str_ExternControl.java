package de.yadrone.base.datatypes;

import java.util.LinkedList;

/**
 * The data structure sent in ExternControl commands to control the drone Based
 * in struct str_ExternControl in
 * http://mikrokopter.de/mikrosvn/FlightCtrl/tags/V0.84a/uart.c
 * 
 * @author Ello Oliveira
 * 
 */
public class str_ExternControl extends c_int {
	public u8 digital[] = new u8[2];
	public u8 remoteTasten;
	public s8 pitch; // "Nick" in str_ExternControl
	public s8 roll;
	public s8 yaw; // "Gier" in str_ExternControl
	public u8 throttle; // Gas
	public s8 hight;
	public u8 free;
	public u8 frame;
	public u8 config;

	public str_ExternControl(int pitch, int roll, int yaw, int throttle) {
		super();
		/*
		 * this.pitch = new s8("Pitch", pitch); this.roll = new s8("Roll",
		 * roll); this.yaw = new s8("Yaw", yaw); this.throttle = new
		 * u8("Throttle", throttle);
		 */

		allAttribs = new LinkedList<c_int>();
		for (int i = 0; i < digital.length; i++) {
			digital[i] = new u8("Digital" + i);
			allAttribs.add(digital[i]);
		}
		try {
			remoteTasten = new u8("RemoteTasten");
			allAttribs.add(remoteTasten);
			this.pitch = new s8("Pitch", -128, 127);

			this.pitch.setAndCheckValue(pitch);

			allAttribs.add(this.pitch);
			this.roll = new s8("Roll", -128, 127);
			this.roll.setAndCheckValue(roll);
			allAttribs.add(this.roll);
			this.yaw = new s8("Yaw", -128, 127);
			this.yaw.setAndCheckValue(yaw);
			allAttribs.add(this.yaw);
			this.throttle = new u8("Throttle", 255);
			this.throttle.setAndCheckValue(throttle);
			allAttribs.add(this.throttle);
			this.hight = new s8("Hight");
			allAttribs.add(hight);
			free = new u8("free");
			allAttribs.add(free);
			frame = new u8("Frame");
			allAttribs.add(frame);
			config = new u8("Config");
			config.value = 1;
			allAttribs.add(config);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public str_ExternControl() {
		super();

		allAttribs = new LinkedList<c_int>();
		for (int i = 0; i < digital.length; i++) {
			digital[i] = new u8("Digital" + i);
			allAttribs.add(digital[i]);
		}
		remoteTasten = new u8("RemoteTasten");
		allAttribs.add(remoteTasten);
		this.pitch = new s8("Pitch");
		allAttribs.add(this.pitch);
		this.roll = new s8("Roll");
		allAttribs.add(this.roll);
		this.yaw = new s8("Yaw");
		allAttribs.add(this.yaw);
		this.throttle = new u8("Throttle");
		allAttribs.add(this.throttle);
		this.hight = new s8("Hight");
		allAttribs.add(hight);
		free = new u8("free");
		allAttribs.add(free);
		frame = new u8("Frame");
		allAttribs.add(frame);
		config = new u8("Config");
		allAttribs.add(config);
	}
}
