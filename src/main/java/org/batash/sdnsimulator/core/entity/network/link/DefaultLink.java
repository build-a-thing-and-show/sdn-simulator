package org.batash.sdnsimulator.core.entity.network.link;

import org.batash.sdnsimulator.core.Network;
import org.batash.sdnsimulator.core.entity.network.Link;
import org.batash.sdnsimulator.core.entity.traffic.Packet;
import org.batash.sdnsimulator.core.event.ArrivalToNode;
import org.batash.sdnsimulator.core.event.DepartureFromNode;
import org.batash.sdnsimulator.core.utility.Mathematics;

public class DefaultLink extends Link {

	public DefaultLink(int ID, int sourceID, int destinationID, float propagationDelay, float band,
			short bufferType, int bufferSize, int bufferPolicy) {
		super(ID, sourceID, destinationID, propagationDelay, band, bufferType, bufferSize,
				bufferPolicy);
	}

	public void bufferPacket(Network net, Packet packet) {

		if (packet.getSegment() != null) {
			updateSegementArrivalToLinkCounters(net.getCurrentTime(),
					packet.getSegment().getFlowID());
		}

		float bufferTime = buffer.enQueue(net.getCurrentTime(),
				getTransmissionDelay(packet.getSize()));
		if (bufferTime >= 0) {
			float transmissionDelay = getTransmissionDelay(packet.getSize());
			float nextTime = Mathematics.addFloat(net.getCurrentTime(), bufferTime);
			nextTime = Mathematics.addFloat(nextTime, transmissionDelay);
			net.getEventList().addEvent(new DepartureFromNode(nextTime, this.ID, packet));

			/** ===== Statistical Counters ===== **/
			if (isMonitored) {
				if (packet.getSegment() != null) {
					net.getHosts().get(packet.getSegment().getSrcHostID())
							.updateFlowTotalBufferTime(bufferTime);
					net.getHosts().get(packet.getSegment().getSrcHostID())
							.updateDataSegmentsDepartures(packet.getSegment().getSeqNum(),
									net.getCurrentTime());
					updateUtilizationCounters(net.getCurrentTime(), packet.getSegment().getFlowID(),
							transmissionDelay);
					updateQueueLenghtCounter(net.getCurrentTime(), buffer.getOccupancy());
				}

			}
			/** ================================ **/
		} else {
			// Packet Drop happens here
		}
	}

	public void transmitPacket(Network net, Packet packet) {
		buffer.deQueue();
		float nextTime = Mathematics.addFloat(net.getCurrentTime(), getPropagationDelay());
		net.getEventList().addEvent(new ArrivalToNode(nextTime, srcNodeID, dstNodeID, packet));
		/** ===== Statistical Counters ===== **/
		updateQueueLenghtCounter(net.getCurrentTime(), buffer.getOccupancy());
		updateMaxQueueLengthCounter(buffer.getOccupancy());
		/** ================================ **/
	}
}
