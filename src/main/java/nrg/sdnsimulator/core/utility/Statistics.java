package nrg.sdnsimulator.core.utility;

import java.util.HashMap;
import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;
import nrg.sdnsimulator.core.Network;
import nrg.sdnsimulator.core.entity.network.Controller;
import nrg.sdnsimulator.core.entity.network.Host;
import nrg.sdnsimulator.core.entity.network.Link;
import nrg.sdnsimulator.core.entity.network.SDNSwitch;
import nrg.sdnsimulator.core.entity.traffic.Flow;

@Getter
@Setter
public class Statistics {

	private Link bottleneckLink;
	private HashMap<Integer, Flow> flows;
	private HashMap<Integer, Link> links;
	private HashMap<Integer, Controller> controllers;
	private HashMap<Integer, SDNSwitch> switches;

	public Statistics(Network net, int btlLinkID) {
		links = new HashMap<Integer, Link>();
		switches = new HashMap<Integer, SDNSwitch>();
		flows = new HashMap<Integer, Flow>();
		controllers = new HashMap<Integer, Controller>();
		this.controllers = net.getControllers();
		this.switches = net.getSwitches();
		this.links = net.getLinks();
		for (Host host : net.getHosts().values()) {
			if (host.getTransportAgent().getFlow().getID() < 10000) {
				this.flows.put(host.getTransportAgent().getFlow().getID(), host.getTransportAgent().getFlow());
			}
		}
		for (Link link : links.values()) {
			if (link.isMonitored()) {
				bottleneckLink = link;
			}
		}

	}

	public float getAvgFlowCompletionTime() {
		float sum = 0;
		for (Flow flow : flows.values()) {
			sum = Mathematics.addFloat(sum, Mathematics.subtractFloat(flow.getFINSendingTime(), flow.getArrivalTime()));
		}
		return sum / (float) flows.size();
	}

	public float getAvgFlowThroughput() {
		float sum = 0;
		for (Flow flow : flows.values()) {
			sum = Mathematics.addFloat(sum, calculateFlowThroughput(flow));
		}
		return 100 * (sum / (float) flows.size());
	}

	public float getAvgStartupDelay() {
		float sum = 0;
		for (Flow flow : flows.values()) {
			float startupDelay = (Mathematics.subtractFloat(flow.getDataSendingStartTime(), flow.getArrivalTime()));
			sum = Mathematics.addFloat(sum, startupDelay);
		}
		return Mathematics.divideFloat(sum, (float) flows.size());
	}

	public float getFairnessIndex() {
		float f = 0;
		float numinator = 0;
		float denuminator = 0;
		for (Flow flow : flows.values()) {
			float flowThroughput = calculateFlowThroughput(flow);
			numinator = Mathematics.addFloat(numinator, flowThroughput);
			denuminator = Mathematics.addFloat(denuminator, Mathematics.multiplyFloat(flowThroughput, flowThroughput));
		}
		numinator = Mathematics.multiplyFloat(numinator, numinator);
		denuminator = Mathematics.multiplyFloat(flows.size(), denuminator);
		f = Mathematics.divideFloat(numinator, denuminator);
		return f;
	}

	public float getBottleneckUtilization() {
		float utilization = 0;
		float totalUpTime = Mathematics.subtractFloat(bottleneckLink.getLastSegmentTransmittedTime(),
				bottleneckLink.getFirstSegmentArrivalTime());
		if (totalUpTime > 0) {
			utilization = Mathematics.divideFloat(bottleneckLink.getTotalTransmissionTime(), totalUpTime);
		}
		return utilization;
	}

	public float getBtlAvgQueueLength() {
		TreeMap<Float, Float> queueTimeSeries = bottleneckLink.getQueueLength();
		float avgQueuelength = 0;
		int n = queueTimeSeries.size();
		float numinator = 0;
		float denuminator = Mathematics.subtractFloat(queueTimeSeries.lastKey(), queueTimeSeries.firstKey());
		for (int i = 0; i < n - 1; i++) {
			float lenght = queueTimeSeries.get(queueTimeSeries.firstKey());
			float tNow = queueTimeSeries.firstKey();
			float tNext = queueTimeSeries.higherKey(tNow);
			queueTimeSeries.remove(tNow);
			numinator = Mathematics.addFloat(numinator,
					Mathematics.multiplyFloat(lenght, Mathematics.subtractFloat(tNext, tNow)));
		}
		if (denuminator > 0) {
			avgQueuelength = Mathematics.divideFloat(numinator, denuminator);
		}
		return avgQueuelength;
	}

	public float getBtlMaxQueueLength() {
		return bottleneckLink.getMaxQeueLength();
	}

	private float calculateFlowThroughput(Flow flow) {
		float throughput = Mathematics.divideFloat(flow.getTotalTransmissionTime(),
				Mathematics.subtractFloat(flow.getFINSendingTime(), flow.getArrivalTime()));
		return throughput;
	}

}
