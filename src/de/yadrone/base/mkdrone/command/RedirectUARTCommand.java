package de.yadrone.base.mkdrone.command;

import de.yadrone.base.datatypes.MagicPacket;
import de.yadrone.base.datatypes.s8;

public class RedirectUARTCommand extends NCCommand {

	public RedirectUARTCommand(long address) {
		if(address < 0) {
			this.param = new MagicPacket();
		} else {
			this.param = new s8(address);
		}
		this.id = 'u';
	}
}
