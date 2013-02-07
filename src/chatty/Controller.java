package chatty;

import gui.ButtonEvent;
import gui.ButtonListener;
import gui.MainFrame;
import gui.options.OptionsField;
import gui.options.OptionsMenu;
import network.NetworkHandler;
import network.client.ClientEvent;
import network.client.ClientEvent.Event;

public class Controller implements ButtonListener {

	private final NetworkHandler networkHandler;
	private MainFrame gui;

	public Controller() {
		networkHandler = new NetworkHandler();
	}

	public void sendMessageToAll(String msg) {
		// TODO: send SELF messages to SERVER, TEMPORARY WORKAROUND

		// if CLIENT, send to SERVER only
		if (networkHandler.getProgramState() != null && networkHandler.getProgramState().isRunning() && networkHandler.getProgramState().isClient())
			networkHandler.getClient().getOutputStream().println(Config.NICKNAME + ": " + msg);

		// if SERVER, send to all CLIENTS and SELF
		else if (networkHandler.getProgramState() != null && networkHandler.getProgramState().isRunning() && networkHandler.getProgramState().isServer()) {
			networkHandler.getServer().broadcastServerMessage(msg);
		}
	}

	public NetworkHandler getNetworkHandler() {
		return networkHandler;

	}

	public void setGui(MainFrame gui) {
		this.gui = gui;
	}

	private void updateConfig(ButtonEvent event) {
		for (OptionsField field : gui.getOptionsMenu().getOptionFields())
			switch (field.getOption()) {
			case HOSTNAME:
				Config.HOSTNAME = field.submit();
				break;
			case PORT:
				try {
					Config.PORT = Integer.parseInt(field.submit());
					break;
				} catch (NumberFormatException e) {
					networkHandler.fireClientEvent(new ClientEvent(Event.ERROR, "Invalid port!"));
				}
			case NICKNAME:
				Config.NICKNAME = field.submit();
				break;
			}
	}

	public void toggleOptions() {
		if (gui.getOptionsMenu().isVisible()) {
			gui.getOptionsMenu().setVisible(false);
			gui.getOptionsButton().setActive(false);
		} else {
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
				networkHandler.startServer();
			break;
		case CLIENT:
			if (event.getButton().isActive())
				networkHandler.shutDown();
			else
				networkHandler.startClient();
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
}
