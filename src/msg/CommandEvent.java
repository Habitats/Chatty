package msg;

public class CommandEvent {

	private ChatCommand cmd;
	private String[] msgArr;

	private String raw;

	private String returnMsg;

	public CommandEvent(String raw) {
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
