package chatty;

import gui.MainFrame;

import network.NetworkHandler;

public class Chatty {
	private NetworkHandler networkHandler;
	private Controller controller;

	public static void main(String[] args) {
		new Chatty().run();
	}

	private void run() {

		controller = new Controller();
		MainFrame gui = new MainFrame(this);
		controller.addGui(gui);
	}

	public Controller getController() {
		return controller;
	}
}
