package chatty;

import network.NetworkHandler;

public class Controller {

	private final NetworkHandler networkHandler;

	public Controller() {
		networkHandler = new NetworkHandler();
	}

	public void sendMessageToAll(String msg) {
		// TODO: send SELF messages to SERVER, TEMPORARY WORKAROUND

		// if CLIENT, send to SERVER only
		if (networkHandler.getProgramState() != null && networkHandler.getProgramState().isRunning() && networkHandler.getProgramState().isClient())
			networkHandler.getClient().getOutputStream().println(Config.NAME_USER + ": " + msg);

		// if SERVER, send to all CLIENTS and SELF
		else if (networkHandler.getProgramState() != null && networkHandler.getProgramState().isRunning() && networkHandler.getProgramState().isServer()) {
			networkHandler.getServer().broadcastServerMessage(msg);
		}
	}

	public NetworkHandler getNetworkHandler() {
		return networkHandler;

	}
}
