package de.yadrone.base.datatypes;

import java.util.LinkedList;

public class MagicPacket extends c_int {

	public MagicPacket() {
		allAttribs = new LinkedList<c_int>();
//		allAttribs.add(new s8(0x1B));
//		allAttribs.add(new s8(0x1B));
//		allAttribs.add(new s8(0x55));
//		allAttribs.add(new s8(0xAA));
//		allAttribs.add(new s8(0x00));
		allAttribs.add(new s8(27));
		allAttribs.add(new s8(27));
		allAttribs.add(new s8(85));
		allAttribs.add(new s8(170));
		allAttribs.add(new s8(0));
	}
}
