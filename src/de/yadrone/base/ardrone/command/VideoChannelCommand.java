package de.yadrone.base.ardrone.command;

public class VideoChannelCommand extends ConfigureCommand {

	public VideoChannelCommand(VideoChannel c) {
		super("video:video_channel", String.valueOf(c.ordinal()));
	}

}
