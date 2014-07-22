package de.yadrone.base.ardrone.command;

public class ResetControlAckCommand extends ControlCommand {

	public ResetControlAckCommand() {
		super(ControlMode.ACK, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.yadrone.base.command.ATCommand#getPriority()
	 */
	@Override
	public Priority getPriority() {
		return Priority.MAX_PRIORITY;
	}

}
