package network.server;

import msg.ChatEvent;
import network.NetworkEvent;

public class ServerEvent extends NetworkEvent {
	public enum ServerEvents {
		START("Server started!"), //
		SHUTDOWN("Server shutting down!"), //
		CRASH("Server crashed!"), //
		CLIENT_CONNECT("Client connected!"), //
		CLIENT_DROPPED("Client dropped!"), //
		STATUS, //
		CHAT_EVENT, //
		;
		private String msg;

		private ServerEvents() {
		}

		private ServerEvents(String msg) {
			this.msg = msg;
		}

		public String getDefaultMessage() {
			return msg;
		}

	}

	private ServerEvents event;

	public ServerEvent(ServerEvents event) {
		this(event, null, null);
	}

	public ServerEvent(ServerEvents event, String msg) {
		this(event, null, msg);
	}

	public ServerEvent(ServerEvents event, Exception e) {
		this(event, e, null);
	}

	public ServerEvent(ServerEvents event, Exception e, String msg) {
		generateGeneralInfo();
		if (msg == null)
			msg = event.getDefaultMessage();
		super.chatEvent = new ChatEvent(msg);
		if (e != null)
			e.printStackTrace();
		super.e = e;
		this.event = event;
	}

	public ServerEvent(ServerEvents event, ChatEvent chatEvent) {
		super.chatEvent = chatEvent;
		this.event = event;
		generateGeneralInfo();
	}

	@Override
	public ServerEvents getEvent() {
		return event;
	}
}
