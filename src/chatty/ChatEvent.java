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
