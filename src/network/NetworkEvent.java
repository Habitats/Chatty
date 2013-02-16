package network;

import java.util.Date;

import network.server.ServerEvent.ServerEvents;

import msg.ChatEvent;


public abstract class NetworkEvent {

	private Date date;
	public Exception e;
//	protected ChatEvent chatEvent;
	public ChatEvent chatEvent;
	
	protected void generateGeneralInfo() {
		date = new Date();
	}

	public Date getDate() {
		return date;
	}

	public Exception getException() {
		return e;
	}

	public abstract Object getEvent();

	public ChatEvent getChatEvent() {
		return chatEvent;
	}
	protected void setChatEvent(ChatEvent chatEvent) {
		this.chatEvent = chatEvent;
	}
}
