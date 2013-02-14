package chatty;

import gui.ButtonEvent;
import gui.ButtonHandler;
import gui.MainFrame;
import gui.MenuButton;
import gui.ButtonEvent.ButtonEvents;

public class Chatty {
	private Controller controller;

	public static void main(String[] args) {
		new Chatty().run();
	}

	private void run() {
		int instances = 200;

		// buttonClicked(new ButtonEvent(null, new
		// MenuButton(MenuButton.Type.SERVER, null, new ButtonHandler()),
		// ButtonEvents.CLICKED));
		controller = new Controller();
		MainFrame gui = new MainFrame(controller);
		controller.setGui(gui);
		controller.buttonClicked(new ButtonEvent(null, new MenuButton(MenuButton.Type.SERVER, null, new ButtonHandler()), ButtonEvents.CLICKED));

		for (int i = 0; i < instances; i++) {
			controller = new Controller();
			MainFrame guiCli = new MainFrame(controller);
			controller.setGui(guiCli);
			controller.buttonClicked(new ButtonEvent(null, new MenuButton(MenuButton.Type.CLIENT, null, new ButtonHandler()), ButtonEvents.CLICKED));
		}
		// controller.startTestInstance(1, 2);
	}

	public Controller getController() {
		return controller;
	}
}
