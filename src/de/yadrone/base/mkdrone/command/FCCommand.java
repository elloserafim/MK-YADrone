package de.yadrone.base.mkdrone.command;

import de.yadrone.base.manager.SerialAbstractManager;

public class FCCommand extends MKCommand {
	protected boolean sticky;
	protected String title;
	public FCCommand() {
		this.address = SerialAbstractManager.FC_ADDRESS;
	}

	public boolean isSticky() {
		// TODO Auto-generated method stub
		return sticky;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
