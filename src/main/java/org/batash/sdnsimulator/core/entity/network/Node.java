package org.batash.sdnsimulator.core.entity.network;

import org.batash.sdnsimulator.core.Network;
import org.batash.sdnsimulator.core.entity.Entity;
import org.batash.sdnsimulator.core.entity.traffic.Packet;

public abstract class Node extends Entity {

	public Node(int ID) {
		super(ID);
	}

	public abstract void connectToNode(int linkID, int dstNodeID, short dstNodeType);

	public abstract void recvPacket(Network net, int srcNodeID, Packet packet);

	public abstract void executeTimeOut(Network net, int timerID);

	public void updateStatisticalCounters() {

	}
}
