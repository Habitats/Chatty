package chatty;

import gui.EventListener;
import gui.MainFrame;

import network.NetworkHandler;

public class Chatty {
	private NetworkHandler networkHandler;
	private Controller controller;

	public static void main(String[] args) {
		new Chatty().run();
	}

	private void run() {
		int port = Config.DEFAULT_PORT;
		String hostname = Config.DEFAULT_HOST;

		MainFrame gui = new MainFrame(this);
		networkHandler = new NetworkHandler(port, hostname);

		controller = new Controller(networkHandler, gui);
	}

	public NetworkHandler getNetworkHandler() {
		return networkHandler;
	}

	public Controller getController() {
		return controller;
	}
}
