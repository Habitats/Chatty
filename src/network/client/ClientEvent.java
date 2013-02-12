package network.client;

import chatty.ChatEvent;
import chatty.CommandEvent;
import network.NetworkEvent;
import network.client.ClientEvent.ClientEvents;

public class ClientEvent extends NetworkEvent {

	public enum ClientEvents {
		START("Client stared!"), //
		SHUTDOWN("Client shutting down!"), //
		DISCONNECT("Disconnected!"), //
		CRASH("Client crashed!"), //
		CONNECT("Client connected!"), //
		STATUS, //
		COMMAND, //
		CHAT_EVENT, //
		;
		String msg;
		private final String prefix = "STATUS";

		private ClientEvents() {
		}

		private ClientEvents(String msg) {
			this.msg = msg;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public String getPrefix() {
			return prefix;
		}
	}

	private ClientEvents event;

	public ClientEvent(ClientEvents event) {
		this.event = event;
		generateGeneralInfo();
	}

	public ClientEvent(ClientEvents event, String msg) {
		event.setMsg(msg);
		this.event = event;
		generateGeneralInfo();
	}

	public ClientEvent(ClientEvents event, Exception e) {
		this.event = event;
		super.e = e;
	}

	public ClientEvent(ClientEvents event, Exception e, String msg) {
		super.e = e;
		event.setMsg(msg);
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
