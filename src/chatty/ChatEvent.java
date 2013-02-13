package chatty;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import chatty.ChatEvent.Receipient;

public class ChatEvent implements Serializable {
	public enum Receipient {
		SERVER(null), //
		PRIVATE(null), //
		GLOBAL(null),

		//
		;
		private String username;

		private Receipient(String username) {
			this.username = username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getUsername() {
			return username;
		}
	}

	private final User from;
	private Receipient rec;
	private String msg;
	private final String[] msgArr;

	private final String sendDate;
	private Date receivedDate;

	private ChatCommand cmd;

	private String returnMsg;

	public ChatEvent(User from, Receipient rec, String msg) {
		this(from, null, rec, msg);
	}

	public ChatEvent(User from, String to, Receipient rec, String msg) {
		this.from = from;
		this.msg = msg;
		setRec(rec, to);
		this.sendDate = new SimpleDateFormat("hh:mm:ss").format(new Date());
		msgArr = msg.split(" ");
		if (msgArr[0].startsWith("/")) {
			cmd = ChatCommand.getCmd(msgArr[0]);
		}
	}

	public void setRec(Receipient rec, String to) {
		this.rec = rec;
		if (rec == Receipient.PRIVATE && cmd == ChatCommand.PRIV_MSG) {
			rec.setUsername(to);
			String tmpMsg = msg;
			msg = "";
			for (int i = 2; i < tmpMsg.split(" ").length; i++)
				msg += msgArr[i] + " ";
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

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate() {
		this.receivedDate = new Date();
	}
}
