package de.yadrone.base.mkdrone.command;

import de.yadrone.base.datatypes.c_int;


public abstract class MKCommand {
	protected char id;
	protected int address;
	protected c_int param;
}
