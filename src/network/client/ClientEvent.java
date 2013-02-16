package network.client;

import msg.ChatEvent;
import network.NetworkEvent;

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
		private String msg;

		private ClientEvents() {
		}

		private ClientEvents(String msg) {
			this.msg = msg;
		}

		public String getDefaultMessage() {
			return msg;
		}
	}

	private ClientEvents event;

	public ClientEvent(ClientEvents event) {
		this(event, null, null);
	}

	public ClientEvent(ClientEvents event, String msg) {
		this(event, null, msg);
	}

	public ClientEvent(ClientEvents event, Exception e) {
		this(event, e, null);
	}

	public ClientEvent(ClientEvents event, Exception e, String msg) {
		super.e = e;
		if (msg == null)
			msg = event.getDefaultMessage();
		if(e != null)
			e.printStackTrace();
		super.chatEvent = new ChatEvent(msg);
		generateGeneralInfo();
		this.event = event;
	}

	public ClientEvent(ClientEvents event, ChatEvent chatEvent) {
		super.chatEvent = chatEvent;
		this.event = event;
		generateGeneralInfo();
	}

	@Override
	public ClientEvents getEvent() {
		return event;
	}

}
