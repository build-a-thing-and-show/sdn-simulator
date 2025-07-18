package org.batash.sdnsimulator.core.entity.network.controller;

import lombok.Getter;
import lombok.Setter;
import org.batash.sdnsimulator.core.Network;
import org.batash.sdnsimulator.core.entity.network.Controller;
import org.batash.sdnsimulator.core.entity.traffic.Packet;
import org.batash.sdnsimulator.core.entity.traffic.Segment;
import org.batash.sdnsimulator.core.utility.Keywords;

@Getter
@Setter
public class DefaultController extends Controller {
	public DefaultController(int ID, short routingAlgorithm) {
		super(ID, routingAlgorithm);
	}

	public void recvPacket(Network net, int switchID, Packet packet) {
		Segment segment = packet.getSegment();
		this.recvdSegment = segment;
		switch (segment.getType()) {
			case Keywords.Segments.Types.SYN:
				database.addFlow(switchID, segment.getSrcHostID(), segment.getFlowID());

				handleRouting(net, switchID, getAccessSwitchID(net, recvdSegment.getDstHostID()));
				sendPacketToSwitch(net, switchID, new Packet(recvdSegment, null));
				break;
			case Keywords.Segments.Types.FIN:
				database.removeFlow(switchID, segment.getSrcHostID(), segment.getFlowID());
				sendPacketToSwitch(net, switchID, new Packet(recvdSegment, null));
				break;
			default:
				break;
		}
	}

	@Override
	public void executeTimeOut(Network net, int timerID) {
		// The controller does not need timer for now

	}

}
