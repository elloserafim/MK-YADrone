package de.yadrone.base.ardrone.command;

public class QuitCommand extends DroneCommand {
	@Override
	public Priority getPriority() {
		return Priority.MAX_PRIORITY;
	}
}
