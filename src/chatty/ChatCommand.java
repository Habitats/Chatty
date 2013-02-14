package chatty;

public enum ChatCommand {
	DISCONNECT("/disconnect", "Disconnect from server: /disconnect"), //
	QUIT("/quit", "Shut down + " + Config.APPLICATION_NAME + ": /quit"), //

	CHANGE_NICK("/nick", "Change your nick: /nick yourNewNick"), //
	PRIV_MSG("/query", "Send a private message: /query nickname message"), //
	CONNECT("/connect", "Connect to the specified IP: /connect IP PORT"), //
	LISTEN_PORT("/port", "Change listening port (server will restart!): /port PORT"), //

	HELP("/help", "Available commands (prefix with /): nick, disconnect, query, quit, connect, port"), //

	STATUS("/status","Stats...");
	;
	private String cmdAsString;
	private String help;

	ChatCommand(String cmd, String help) {
		this.cmdAsString = cmd;
		this.help = help;
	}

	@Override
	public String toString() {
		return cmdAsString;
	}

	public static ChatCommand getCmd(String cmdAsString) {
		for (ChatCommand cmd : ChatCommand.values())
			if (cmd.toString().toLowerCase().equals(cmdAsString.toLowerCase()))
				return cmd;
		return null;
	}

	public String getHelp() {
		return help;
	}
}
