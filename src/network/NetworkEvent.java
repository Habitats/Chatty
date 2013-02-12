package network;

import java.util.Date;

import chatty.ChatEvent;

public abstract class NetworkEvent {

	private Date date;
	public Exception e;
	protected ChatEvent chatEvent;

	protected void generateGeneralInfo() {
		date = new Date();
	}

	public Date getDate() {
		return date;
	}

	public void setObject(ChatEvent chatEvent) {
		this.chatEvent = chatEvent;
	}

	public Exception getException() {
		return e;
	}

	public ChatEvent getChatEvent() {
		return chatEvent;
	}

	public abstract Object getEvent();
}
