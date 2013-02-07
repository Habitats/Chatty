package chatty;

import gui.MainFrame;

public class Chatty {
	private Controller controller;

	public static void main(String[] args) {
		new Chatty().run();
	}

	private void run() {

		controller = new Controller();
		MainFrame gui = new MainFrame(controller);
		controller.setGui(gui);
	}

	public Controller getController() {
		return controller;
	}
}
