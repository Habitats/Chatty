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
		if (args.length > 0 && args[0] != null) {
			int instances = Integer.parseInt(args[0]);
			chatty.test(instances, null);
		} else if (args.length == 2) {
			int instances = Integer.parseInt(args[0]);
			String hostname = args[1];
			chatty.test(instances, hostname);
		} else {
			chatty.test(1, null);
//			chatty.run();
		}
	}

	private void test(int instances, String hostname) {

		for (int i = 0; i < instances; i++) {
			controller = new Controller();
			MainFrame guiCli = new MainFrame(controller);
			controller.setGui(guiCli);
			controller.buttonClicked(new ButtonEvent(null, new MenuButton(MenuButton.Type.CLIENT, null, new ButtonHandler()), ButtonEvents.CLICKED));
		}
		controller = new Controller();
		MainFrame guiServ = new MainFrame(controller);
		controller.setGui(guiServ);
		controller.buttonClicked(new ButtonEvent(null, new MenuButton(MenuButton.Type.SERVER, null, new ButtonHandler()), ButtonEvents.CLICKED));
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
