package msg;

import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import msg.ChatEvent.Receipient;
import network.NetworkListener;
import network.client.ClientEvent;
import network.server.ServerEvent;
import chatty.Config;
import chatty.Controller;

public class MessageHandler implements NetworkListener {
	private boolean enableTimeStamp;
	private boolean enableDeltaTime;

	private ArrayList<MessageListener> messageListeners = new ArrayList<MessageListener>();
	private final Controller controller;

	public MessageHandler(Controller controller) {
		this.controller = controller;
		enableTimeStamp = true;
		enableDeltaTime = true;
	}

	private ChatEvent formatChatMessage(ChatEvent chatEvent) {
		String deltaTime = "";
		String timeStamp = "";
		String nick = chatEvent.getFrom().getDisplayName() + Config.SEP;

		if (enableTimeStamp)
			timeStamp = new SimpleDateFormat("hh:mm:ss").format(new Date()) + Config.SEP;
		if (enableDeltaTime)
			deltaTime = chatEvent.getDelay() + " ms" + Config.SEP;

		chatEvent.setMsg(timeStamp + nick + deltaTime + chatEvent.getMsg());
		return chatEvent;
	}

	public void newChatEvent(ActionEvent ae) {
		ChatEvent chatEvent = new ChatEvent(getController().getUser(), Receipient.GLOBAL, ae.getActionCommand());
		if (chatEvent.getMsgArr().length > 0) {
			if (chatEvent.isCommand() && chatEvent.getCommand() == ChatCommand.PRIV_MSG) {
				chatEvent.setRec(Receipient.PRIVATE, chatEvent.getMsgArr()[1]);
				getController().sendChatEvent(chatEvent);
			} else if (chatEvent.isCommand())
				issueLocalChatCommand(chatEvent);
			else
				getController().sendChatEvent(chatEvent);
		}
	}

	private void issueLocalChatCommand(ChatEvent chatEvent) {
		String returnMsg = "";
		if (chatEvent.getCommand() == ChatCommand.HELP || (chatEvent.getMsgArr().length > 2 && chatEvent.getMsgArr()[2].equals("help"))) {
			returnMsg = chatEvent.getCommand().getHelp();
			chatEvent.setMsg(returnMsg);
		} else if (chatEvent.getCommand() == ChatCommand.QUIT)
			System.exit(0);
		if (chatEvent.getMsgArr().length == 1)
			switch (chatEvent.getCommand()) {
			case STATUS:
				chatEvent.setMsg(String.format("Online clients: " + getController().getNetworkHandler().getServer().getClientConnections().size()));
				break;
			}
		else if (chatEvent.getMsgArr().length == 2) {
			switch (chatEvent.getCommand()) {
			case CHANGE_NICK:
				getController().setNick(chatEvent.getMsgArr()[1]);
				break;
			case LISTEN_PORT:
				getController().setPort(chatEvent.getMsgArr()[1]);
				break;
			case CONNECT:
				getController().setHostname(chatEvent.getMsgArr()[1]);
				getController().getNetworkHandler().restartClient();
				break;
			case DISCONNECT:
				getController().getNetworkHandler().shutDown();
				break;
			}
		}
		fireChatEventToListeners(chatEvent);

	}

	// network stuff

	@Override
	public void serverStatus(ServerEvent event) {
		fireChatEventToListeners(event.getChatEvent());
	}

	@Override
	public void serverNormalMessage(ServerEvent event) {
		fireChatEventToListeners(event.getChatEvent());
	}

	@Override
	public void clientStatus(ClientEvent event) {
		fireChatEventToListeners(event.getChatEvent());
	}

	@Override
	public void clientMessage(ClientEvent event) {
		fireChatEventToListeners(event.getChatEvent());
	}

	public void fireChatEventToListeners(ChatEvent chatEvent) {
		chatEvent = formatChatMessage(chatEvent);
		for (MessageListener messageListener : messageListeners) {
			messageListener.onChatEvent(chatEvent);
		}
	}

	public void addMessageListener(MessageListener messageListener) {
		messageListeners.add(messageListener);
	}

	public Controller getController() {
		return controller;
	}

}
