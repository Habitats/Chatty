package network.server;

import chatty.ChatEvent;
import network.NetworkEvent;
import network.server.ServerEvent.ServerEvents;

public class ServerEvent extends NetworkEvent {
	public enum ServerEvents {
		START, //
		SHUTDOWN, //
		CRASH, //
		CLIENT_CONNECT, //
		CLIENT_DROPPED, //
		STATUS, //
		CHAT_EVENT, //
	}

	private ServerEvents event;

	public ServerEvent(ServerEvents event) {
		this.event = event;
		generateGeneralInfo();
	}

	public ServerEvent(ServerEvents event, String msg) {
		super.msg = msg;
		this.event = event;
		generateGeneralInfo();
	}

	public ServerEvent(ServerEvents event, Exception e) {
		super.e = e;
		this.event = event;
	}

	public ServerEvent(ServerEvents event, Exception e, String msg) {
		super.msg = msg;
		super.e = e;
		this.event = event;
	}

	public ServerEvent(ServerEvents event, ChatEvent chatEvent) {
		this.event = event;
		super.chatEvent = chatEvent;
	}

	public ServerEvents getEvent() {
		return event;
	}
}
