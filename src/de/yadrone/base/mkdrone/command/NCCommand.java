package de.yadrone.base.mkdrone.command;

import de.yadrone.base.manager.SerialAbstractManager;

public class NCCommand extends MKCommand {
	
	public NCCommand() {
		this.address = SerialAbstractManager.NC_ADDRESS;
	}

}
