package org.batash.sdnsimulator.core.entity.network.controller.router;

import java.util.HashMap;

import org.batash.sdnsimulator.core.Network;
import org.batash.sdnsimulator.core.entity.network.SDNSwitch;

public abstract class Router {

	public Router() {
	}

	public abstract void setNodeInformation(HashMap<Integer, SDNSwitch> nodes);

	public abstract HashMap<Integer, Integer> run(Network net, int srcID, int targetID);

}
