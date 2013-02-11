package network;

import java.util.Date;

public abstract class NetworkEvent {
	private Date date;
	protected String msg;
	public Exception e;
	protected Object object;

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
		return object;
	}

	public void setObject(Object objectFromUser) {
		this.object = objectFromUser;
	}

	public Exception getException() {
		return e;
	}
}
