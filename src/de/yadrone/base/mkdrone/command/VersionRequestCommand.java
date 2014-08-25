package de.yadrone.base.mkdrone.command;

import de.yadrone.base.datatypes.c_int;

public class VersionRequestCommand extends MKCommand {

	public VersionRequestCommand(int address){
		this.param = new c_int();
		this.id = 'v';
		this.address = address;
	}
}
