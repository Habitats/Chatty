package gui;

import java.util.ArrayList;

public class ButtonHandler {

	private ArrayList<ButtonListener> buttonListeners = new ArrayList<ButtonListener>();

	public void addButtonListener(ButtonListener listener) {
		buttonListeners.add(listener);
	}

	public ArrayList<ButtonListener> getButtonListeners() {
		return buttonListeners;
	}
}
