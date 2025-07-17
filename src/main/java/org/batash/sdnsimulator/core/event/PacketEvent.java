package org.batash.sdnsimulator.core.event;

import lombok.Getter;
import lombok.Setter;
import org.batash.sdnsimulator.core.Event;
import org.batash.sdnsimulator.core.entity.traffic.Packet;

@Getter
@Setter
public abstract class PacketEvent extends Event {

	protected Packet packet;

	public PacketEvent(float eventTime, Packet packet) {
		super(eventTime);
		this.packet = packet;
	}

}
