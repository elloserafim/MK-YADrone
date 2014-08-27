package de.yadrone.base.mkdrone.command;

import de.yadrone.base.datatypes.Waypoint_t;

/** A command that sends a Waypoint to NC
 * @author Ello Oliveira
 *
 */
public class WPSendingCommand extends NCCommand {
	
	public WPSendingCommand(Waypoint_t wayPoint){
		super();
		this.id = 'w';
		this.param = wayPoint;
	}
}
