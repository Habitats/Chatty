package network;

import java.util.Date;

import msg.ChatEvent;
import network.NetworkEvent;

public class NetworkEvent {

	public enum NetworkEvents {
		START_CLIENT, //
		START_SERVER, //
		SHUTDOWN_SERVER, //
		SHUTDOWN_CLIENT,//
		DISCONNECT("Disconnected!"), //
		CRASH("Fatal error, disconnecting..."), //
		CONNECT("Connected!"), //
		STATUS, //
		COMMAND, //
		CHAT_EVENT, //

		CLIENT_CONNECT("Client connected!"), //
		CLIENT_DROPPED("Client dropped!"), //
		;
		private String msg;

		private NetworkEvents() {
		}

		private NetworkEvents(String msg) {
			this.msg = msg;
		}

		public String getDefaultMessage() {
			return msg;
		}
	}

	private Date date;
	private Exception e;
	// protected ChatEvent chatEvent;
	private ChatEvent chatEvent;

	private void generateGeneralInfo() {
		date = new Date();
	}

	public Date getDate() {
		return date;
	}

	public Exception getException() {
		return e;
	}

	public ChatEvent getChatEvent() {
		return chatEvent;

	private NetworkEvents event;

	public NetworkEvent(NetworkEvents event) {
		this(event, null, null);
	}

	public NetworkEvent(NetworkEvents event, String msg) {
		this(event, null, msg);
	}

	public NetworkEvent(NetworkEvents event, Exception e) {
		this(event, e, null);
	}

	public NetworkEvent(NetworkEvents event, Exception e, String msg) {
		this.e = e;
		if (msg == null)
			msg = event.getDefaultMessage();
		if (e != null)
			e.printStackTrace();
		chatEvent = new ChatEvent(msg);
		generateGeneralInfo();
		this.event = event;
	}

	public NetworkEvent(NetworkEvents event, ChatEvent chatEvent) {
		this.chatEvent = chatEvent;
		this.event = event;
		generateGeneralInfo();
	}

	public NetworkEvents getEvent() {
		return event;
	}

}
