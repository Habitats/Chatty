package msg;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import chatty.User;

public class ChatEvent implements Serializable {

	private static final long serialVersionUID = -2117726079856363734L;

	public enum Receipient {
		SERVER, //
		QUERY, //
		CHANNEL, //
		GLOBAL, //
		STATUS, //
		;
	}

	private User from;
	private Receipient rec;
	private String to;
	private String msg;
	private final String[] msgArr;

	private final long sendDate;
	private final String formattedSendDate;

	private ChatCommand cmd;

	private String returnMsg;
	private String formattedMessage;

	public ChatEvent(String msg) {
		this(null, null, Receipient.STATUS, msg);
	}

	public ChatEvent(User from, Receipient rec, String msg) {
		this(from, null, rec, msg);
	}

	public ChatEvent(User from, String to, String msg) {
		this(from, to, null, msg);
	}

	public ChatEvent(User from, String msg) {
		this(from, null, null, msg);
	}

	public ChatEvent(User from, String to, Receipient rec, String msg) {
		this.from = from;
		this.msg = msg;
		this.to = to;
		setRec(rec, to);
		sendDate = System.currentTimeMillis();
		formattedSendDate = new SimpleDateFormat("hh:mm:ss:SSSS").format(sendDate);
		msgArr = msg.split(" ");

		if (msgArr[0].startsWith("/")) {
			cmd = ChatCommand.getCmd(msgArr[0]);
			rec = Receipient.STATUS;
		}
	}

	public void setRec(Receipient rec) {
		setRec(rec, null);
	}

	public void setRec(Receipient rec, String to) {
		this.rec = rec;
		if (rec == Receipient.QUERY && cmd == ChatCommand.PRIV_MSG) {
			this.to = to;
			String tmpMsg = msg;
			msg = "";
			for (int i = 2; i < tmpMsg.split(" ").length; i++)
				msg += msgArr[i] + " ";
		} else if (rec == Receipient.STATUS) {
			this.from = new User("STATUS");
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

	public Receipient getReceipient() {
		return rec;
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

	public long getDelay() {
		return System.currentTimeMillis() - sendDate;
	}

	@Override
	public String toString() {
		return String.format("F: %s - T: %s - M: %s - T: %s", from, rec, msg, sendDate);
	}

	public void setFrom(User duplicate) {
		this.from = duplicate;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTo() {
		return to;
	}

	public void setFormattedMessage(String formattedMessage) {
		this.formattedMessage = formattedMessage;
	}

	public String getFormattedMessage() {
		return formattedMessage;
	}
}
