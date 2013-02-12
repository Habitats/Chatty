package network.client;

import chatty.ChatEvent;
import chatty.CommandEvent;
import network.NetworkEvent;
import network.client.ClientEvent.ClientEvents;

public class ClientEvent extends NetworkEvent {

	public enum ClientEvents {
		START, //
		SHUTDOWN, //
		DISCONNECT, //
		CRASH, //
		CONNECT, //
		STATUS, //
		ERROR, //
		COMMAND, //
		CHAT_EVENT, //
	}

	private ClientEvents event;

	public ClientEvent(ClientEvents event) {
		this.event = event;
		generateGeneralInfo();
	}

	public ClientEvent(ClientEvents event, String msg) {
		super.msg = msg;
		this.event = event;
		generateGeneralInfo();
	}

	public ClientEvent(ClientEvents event, Exception e) {
		this.event = event;
		super.e = e;
	}

	public ClientEvent(ClientEvents event, Exception e, String msg) {
		super.e = e;
		super.msg = msg;
		this.event = event;
	}

	public ClientEvent(ClientEvents event, ChatEvent chatEvent) {
		this.event = event;
		super.chatEvent = chatEvent;
	}

	public ClientEvents getEvent() {
		return event;
	}

}
