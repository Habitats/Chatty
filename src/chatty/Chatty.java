package chatty;

import gui.ButtonEvent;
import gui.ButtonHandler;
import gui.MainFrame;
import gui.MenuButton;
import gui.ButtonEvent.ButtonEvents;

public class Chatty {
	private Controller controller;

	public static void main(String[] args) {
		// new Chatty().run();
		Chatty chatty = new Chatty();
		if (args[0] != null) {
			int instances = Integer.parseInt(args[0]);
			chatty.test(instances);
		} else
			chatty.run();

	}

	private void test(int instances) {

		for (int i = 0; i < instances; i++) {
			controller = new Controller();
			MainFrame guiCli = new MainFrame(controller);
			controller.setGui(guiCli);
			controller.buttonClicked(new ButtonEvent(null, new MenuButton(MenuButton.Type.CLIENT, null, new ButtonHandler()), ButtonEvents.CLICKED));
		}
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
