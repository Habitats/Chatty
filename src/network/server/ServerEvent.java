package network.server;

import network.NetworkEvent;

public class ServerEvent extends NetworkEvent {
	public enum Event {
		START, //
		SHUTDOWN, //
		CRASH, //
		CLIENT_CONNECT, //
		CLIENT_DROPPED, //
		STATUS, //
		MESSAGE, //
	}

	private Event event;

	public ServerEvent(Event event) {
		this.event = event;
		generateGeneralInfo();
	}

	public ServerEvent(Event event, String msg) {
		super.msg = msg;
		this.event = event;
		generateGeneralInfo();
	}

	public ServerEvent(Event event, Exception e) {
		super.e = e;
		this.event = event;
	}

	public ServerEvent(Event event, Exception e, String msg) {
		super.msg = msg;
		super.e = e;
		this.event = event;
	}

	public Event getEvent() {
		return event;
	}
}