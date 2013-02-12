package network;

import java.util.Date;

import chatty.ChatEvent;

public abstract class NetworkEvent {
	private Date date;
	protected String msg;
	public Exception e;
	protected ChatEvent chatEvent;

	protected void generateGeneralInfo() {
		date = new Date();
	}

	public Date getDate() {
		return date;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObject() {
		return chatEvent;
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
}
