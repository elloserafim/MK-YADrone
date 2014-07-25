package de.yadrone.base.mkdrone.command;

import de.yadrone.base.manager.SerialAbstractManager;

public class FCCommand extends MKCommand {
	
	public FCCommand(){
		address = SerialAbstractManager.FC_ADDRESS;
	}
}
