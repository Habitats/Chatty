package gui;

public enum ChatCommand {
	CHANGE_NICK("/nick"), //
	DISCONNECT("/disconnect"), //
	PRIV_MSG("/query"), //
	QUIT("/quit"), //
	HELP("/help"), //
	CONNECT("/connect"),//
	LISTEN_PORT("/port"),//
	;
	private String cmdAsString;

	ChatCommand(String cmd) {
		this.cmdAsString = cmd;
	}

	public String toString() {
		return cmdAsString;
	}

	public static ChatCommand getCmd(String cmdAsString) {
		for (ChatCommand cmd : ChatCommand.values())
			if (cmd.toString().toLowerCase().equals(cmdAsString.toLowerCase()))
				return cmd;
		return null;
	}
}
