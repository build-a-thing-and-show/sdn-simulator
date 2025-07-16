package nrg.sdnsimulator.core.event;

import lombok.Getter;
import lombok.Setter;
import nrg.sdnsimulator.core.Network;
import nrg.sdnsimulator.core.Simulator;
import nrg.sdnsimulator.core.entity.traffic.Packet;
import nrg.sdnsimulator.core.utility.Keywords;

@Getter
@Setter
public class ArrivalToNode extends PacketEvent {

	private int nodeID;
	private int srcNodeID;

	public ArrivalToNode(float eventTime, int srcNodeID, int nodeID, Packet packet) {
		super(eventTime, packet);
		this.nodeID = nodeID;
		this.srcNodeID = srcNodeID;
	}

	@Override
	public void execute(Network net) {
		net.updateTime(eventTime);
		short nodeType = Simulator.getNodeType(nodeID);
		switch (nodeType) {
		case Keywords.Entities.Nodes.Types.Controller:
			net.getControllers().get(nodeID).recvPacket(net, srcNodeID, packet);
			break;
		case Keywords.Entities.Nodes.Types.SDNSwitch:
			net.getSwitches().get(nodeID).recvPacket(net, srcNodeID, packet);
			break;
		case Keywords.Entities.Nodes.Types.Host:
			net.getHosts().get(nodeID).recvPacket(net, srcNodeID, packet);
			break;
		default:
			break;
		}
	}

}
