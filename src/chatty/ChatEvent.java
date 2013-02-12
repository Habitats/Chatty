package chatty;

import java.io.Serializable;
import java.util.Date;

public class ChatEvent implements Serializable {

	private final User from;
	private final User to;
	private final String msg;
	private final String[] msgArr;

	private final Date sendDate;
	private Date receivedDate;

	private ChatCommand cmd;

	private String returnMsg;

	public ChatEvent(User from, User to, String msg) {
		this.from = from;
		this.to = to;
		this.msg = msg;
		this.sendDate = new Date();
		msgArr = msg.split(" ");
		if (msgArr[0].startsWith("/")) {
			cmd = ChatCommand.getCmd(msgArr[0]);
		}
	}

	public boolean isCommand() {
		return (cmd != null);
	}

	public ChatCommand getCommand() {
		return cmd;
	}

	public User getFrom() {
		return from;
	}

	public User getTo() {
		return to;
	}

	public String[] getMsgArr() {
		return msgArr;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public String getMsg() {
		return msg;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate() {
		this.receivedDate = new Date();
	}
}
