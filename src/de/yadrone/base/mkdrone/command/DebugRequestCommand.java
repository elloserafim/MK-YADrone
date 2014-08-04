package de.yadrone.base.mkdrone.command;

import de.yadrone.base.datatypes.u8;

public class DebugRequestCommand extends MKCommand {
	
	public DebugRequestCommand(int address, long interval) {
		this.address = address;
		param = new u8(interval/10);
		id = 'd';
	}

}
