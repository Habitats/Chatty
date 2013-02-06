package network;

import java.util.Date;

public abstract class NetworkEvent {
	private Date date;
	protected String msg;
	public Exception e;

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
}
