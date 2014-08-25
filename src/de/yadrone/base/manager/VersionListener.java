package de.yadrone.base.manager;

import de.yadrone.base.datatypes.str_VersionInfo;

public interface VersionListener {
	public void receivedVersionInfo(str_VersionInfo versionInfo);

}
