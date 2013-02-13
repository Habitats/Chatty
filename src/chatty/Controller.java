package chatty;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;

import gui.ButtonEvent;
import gui.ButtonListener;
import gui.MainFrame;
import gui.options.OptionsField;
import gui.options.OptionsMenu;
import gui.options.OptionsMenu.Option;
import network.NetworkHandler;
import network.client.ClientEvent;
import network.client.ClientEvent.ClientEvents;

public class Controller implements ButtonListener {

	private final NetworkHandler networkHandler;
	private MainFrame gui;
	private int port = 7701;
	public String hostname = "localhost";
	private User user;

	public Controller() {
		networkHandler = new NetworkHandler(this);
		String nickname = "c@rl";
		user = new User(nickname);
	}

	public void sendChatEventToAll(ChatEvent chatEvent) {

		if (networkHandler.getProgramState() != null && networkHandler.getProgramState().isRunning() && networkHandler.getProgramState().isClient())
			try {
				networkHandler.getClient().getObjectOutputStream().writeObject(chatEvent);
			} catch (IOException e) {
				e.printStackTrace();
			}
		else if (networkHandler.getProgramState() != null && networkHandler.getProgramState().isRunning() && networkHandler.getProgramState().isServer()) {
			networkHandler.getServer().broadcastChatEventToAll(chatEvent);
		}
	}

	public NetworkHandler getNetworkHandler() {
		return networkHandler;

	}

	public void setGui(MainFrame gui) {
		this.gui = gui;
	}
	public MainFrame getGui(){
		return gui;
	}

	private void updateConfig(ButtonEvent event) {
		for (OptionsField field : gui.getOptionsMenu().getOptionFields())
			switch (field.getOption()) {
			case HOSTNAME:
				setHostname(field.submit());
				break;
			case PORT:
				setPort(field.submit());
				break;
			case NICKNAME:
				setNick(field.submit());
				break;
			}
	}

	private void setNick(String nickname) {
		if (nickname.length() == 0 || nickname.equals(user.getName()))
			return;
		if (nickname.length() > 15)
			networkHandler.fireClientEvent(new ClientEvent(ClientEvents.STATUS, String.format("Invalid nickname: \"%s\"", nickname)));
		else {
			nickname = nickname.replaceAll("\\s", "_");
			networkHandler.fireClientEvent(new ClientEvent(ClientEvents.STATUS, String.format("Changed nickname to: \"%s\"", nickname)));
			user.setName(nickname);
		}
	}

	private void setPort(String port) {
		if (port.length() == 0 || port.equals(Integer.toString(getPort())))
			return;
		System.out.println(port);
		System.out.println(port);
		for (Character c : port.toCharArray())
			if (!Character.isDigit(c)) {
				networkHandler.fireClientEvent(new ClientEvent(ClientEvents.STATUS, "Invalid port: " + port));
				return;
			}
		networkHandler.fireClientEvent(new ClientEvent(ClientEvents.STATUS, "Changed port to: " + port));
		this.port = Integer.parseInt(port);
	}

	private void setHostname(String hostname) {
		if (hostname.length() == 0 || getHostname().contains(hostname))
			return;
		InetAddress ip;
		String hostnameAndIP = "";
		boolean valid = false;
		try {
			ip = InetAddress.getByName(hostname);
			hostnameAndIP = hostname + String.format(" (%s)", ip.getHostAddress());
			valid = true;
		} catch (Exception e) {
			if (hostname.matches("\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b")) {
				valid = true;
			}
		}
		if (valid) {
			networkHandler.fireClientEvent(new ClientEvent(ClientEvents.STATUS, "Changed host to: " + hostnameAndIP));
			this.hostname = hostname;
		} else
			networkHandler.fireClientEvent(new ClientEvent(ClientEvents.STATUS, "Invalid hostname: " + hostname));
	}

	public void toggleOptions() {
		if (gui.getOptionsMenu().isVisible()) {
			gui.getOptionsMenu().setVisible(false);
			gui.getOptionsButton().setActive(false);
		} else {
			for (OptionsField str : gui.getOptionsMenu().getOptionFields()) {
				switch (str.getOption()) {
				case HOSTNAME:
					str.setText(hostname);
					break;
				case NICKNAME:
					str.setText(user.getName());
					break;
				case PORT:
					str.setText(Integer.toString(getPort()));
					break;
				}
			}
			gui.getOptionsMenu().setVisible(true);
			gui.getOptionsButton().setActive(true);
		}
	}

	@Override
	public void buttonClicked(ButtonEvent event) {
		switch (event.getButton().getType()) {
		case SERVER:
			if (event.getButton().isActive())
				networkHandler.shutDown();
			else
				networkHandler.startServer(port);
			break;
		case CLIENT:
			if (event.getButton().isActive())
				networkHandler.shutDown();
			else
				networkHandler.startClient(hostname, port);
			break;
		case SUBMIT:
			updateConfig(event);
			toggleOptions();
			break;
		case OPTIONS:
			toggleOptions();
			break;
		}
	}

	public void executeChatCommand(ChatEvent chatEvent) {
		String returnMsg = "";
		if (chatEvent.getCommand() == ChatCommand.HELP || (chatEvent.getMsgArr().length > 2 && chatEvent.getMsgArr()[2].equals("help")))
			returnMsg = chatEvent.getCommand().getHelp();
		else if (chatEvent.getCommand() == ChatCommand.QUIT)
			System.exit(0);
		else if (chatEvent.getMsgArr().length == 2) {
			switch (chatEvent.getCommand()) {
			case CHANGE_NICK:
				setNick(chatEvent.getMsgArr()[1]);
				break;
			case LISTEN_PORT:
				setPort(chatEvent.getMsgArr()[1]);
				break;
			case CONNECT:
				setHostname(chatEvent.getMsgArr()[1]);
				networkHandler.restartClient(hostname, port);
				break;
			case DISCONNECT:
				networkHandler.shutDown();
				break;
			}
		}
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

	public User getUser() {
		return user;
	}

}
