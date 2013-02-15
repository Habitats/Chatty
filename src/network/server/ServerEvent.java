package network.server;

import chatty.ChatEvent;
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
		private final String prefix = "STATUS";

		private ServerEvents() {
		}

		private ServerEvents(String msg) {
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

	private ServerEvents event;

	public ServerEvent(ServerEvents event) {
		this.event = event;
		generateGeneralInfo();
	}

	public ServerEvent(ServerEvents event, String msg) {
		event.setMsg(msg);
		this.event = event;
		generateGeneralInfo();
	}

	public ServerEvent(ServerEvents event, Exception e) {
		super.e = e;
		this.event = event;
	}

	public ServerEvent(ServerEvents event, Exception e, String msg) {
		event.setMsg(msg);
		super.e = e;
		this.event = event;
	}

	public ServerEvent(ServerEvents event, ChatEvent chatEvent) {
		this.event = event;
		super.chatEvent = chatEvent;
	}

	@Override
	public ServerEvents getEvent() {
		return event;
	}
}
