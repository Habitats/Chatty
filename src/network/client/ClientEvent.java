package network.client;

import network.NetworkEvent;

public class ClientEvent extends NetworkEvent {
	public enum Event {
		START, //
		SHUTDOWN, //
		DISCONNECT, //
		CRASH, //
		CONNECT, //
		MESSAGE, //
		STATUS, //
	}

	private Event event;

	public ClientEvent(Event event) {
		this.event = event;
		generateGeneralInfo();
	}

	public ClientEvent(Event event, String msg) {
		super.msg = msg;
		this.event = event;
		generateGeneralInfo();
	}

	public ClientEvent(Event event, Exception e) {
		this.event = event;
		super.e = e;
	}

	public ClientEvent(Event event, Exception e, String msg) {
		super.e = e;
		super.msg = msg;
		this.event = event;
	}

	public Event getEvent() {
		return event;
	}
}
