package de.yadrone.base.mkdrone.command;

import de.yadrone.base.manager.SerialAbstractManager;

public class FCCommand extends MKCommand {
	
	public FCCommand() {
		this.address = SerialAbstractManager.FC_ADDRESS;
	}
}
