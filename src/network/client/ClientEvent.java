package network.client;

import chatty.ChatEvent;
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
	}

	private Event event;
	private ChatEvent chatEvent;

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

	public ClientEvent(Event event, ChatEvent chatEvent) {
		this.chatEvent = chatEvent;
		this.event = event;
	}

	public Event getEvent() {
		return event;
	}
	
	public ChatEvent getChatEvent() {
		return chatEvent;
	}
}
