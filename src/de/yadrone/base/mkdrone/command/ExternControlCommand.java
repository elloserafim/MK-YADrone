package de.yadrone.base.mkdrone.command;

import de.yadrone.base.datatypes.str_ExternControl;

/**
 * This command is used to send basic movements command to MKDrone Flight Control
 * @author Ello Oliveira
 *
 */
public class ExternControlCommand extends FCCommand {
	

	public ExternControlCommand(int pitch, int roll, int yaw, int throtle, boolean sticky){
		param = new str_ExternControl(pitch, roll, yaw, throtle);
		id = 'b';
		this.sticky = sticky;
	}
}
