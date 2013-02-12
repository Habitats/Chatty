package network.client;

import chatty.CommandEvent;
import network.NetworkEvent;
import network.client.ClientEvent.Event;

public class ClientEvent extends NetworkEvent {

	public enum Event {
		START, //
		SHUTDOWN, //
		DISCONNECT, //
		CRASH, //
		CONNECT, //
		MESSAGE, //
		STATUS, //
		ERROR, //
		COMMAND, //
		OBJECT, //
	}

	private Event event;
	private CommandEvent chatEvent;

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

	public ClientEvent(Event event, CommandEvent chatEvent) {
		this.chatEvent = chatEvent;
		this.event = event;
	}

	public ClientEvent(Event event, Object objectFromServer) {
		this.event = event;
		super.object = objectFromServer;
	}

	public Event getEvent() {
		return event;
	}

	public CommandEvent getChatEvent() {
		return chatEvent;
	}
}
