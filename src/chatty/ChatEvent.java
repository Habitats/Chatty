package chatty;

public class ChatEvent {

	private String msg;
	private String cmdAsString;
	private ChatCommand cmd;
	private String nickname;
	private String[] msgArr;

	private String raw;

	private String returnMsg;

	public ChatEvent(String raw) {
		this.raw = raw;
		msgArr = raw.split(" ");
		if (msgArr[0].startsWith("/")) {
			cmd = ChatCommand.getCmd(msgArr[0]);
		}
	}

	public void executeCommand() {
		if (cmd == ChatCommand.HELP || (msgArr.length > 2 && msgArr[2].equals("help")))
			returnMsg = cmd.getHelp();
		else if (cmd == ChatCommand.QUIT)
			System.exit(0);
		else if (msgArr.length == 2) {
			if (cmd == ChatCommand.CHANGE_NICK) {
				Config.NAME_USER = msgArr[1];
				returnMsg = "Changed nick to " + msgArr[1];
			} else if (cmd == ChatCommand.CONNECT)
				returnMsg = "";
			else if (cmd == ChatCommand.DISCONNECT)
				returnMsg = "";
			else if (cmd == ChatCommand.LISTEN_PORT)
				returnMsg = "";
			else if (cmd == ChatCommand.PRIV_MSG)
				returnMsg = "";
		}
	}

	public boolean isCommand() {
		return (cmd != null);
	}

	public ChatCommand getCommand() {
		return cmd;
	}

	public String[] getMsgArr() {
		return msgArr;
	}

	public String getRaw() {
		return raw;
	}

	public String getReturnMsg() {
		return returnMsg;
	}
}
