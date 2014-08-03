package de.yadrone.base.mkdrone.navdata;

import de.yadrone.base.datatypes.NaviData_t;

public interface NCOSDListener {

	public void receivedOSDData(NaviData_t navData);
}
