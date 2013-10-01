package gui;

import gui.ButtonEvent.ButtonEvents;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import chatty.Config;

import network.NetworkEvent;
import network.NetworkEvent.NetworkEvents;

public class MenuButton extends Button {

	private static final long serialVersionUID = 1L;

	public enum Type {
		OPTIONS("options"), //
		SERVER("server"), //
		CLIENT("client"), //
		SUBMIT("apply"), //
		;

		private String text;

		Type(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}

	private final ButtonHandler buttonHandler;

	public MenuButton(final Type type, Dimension dim, ButtonHandler buttonHandler) {
		setFont(Config.genFont(18, true));
		this.buttonHandler = buttonHandler;
		setText(type.getText().toUpperCase());
		setType(type);

		setPreferredSize(dim);
		setMinimumSize(dim);

		addMouseListener(new ButtonMouseListener(this) {

			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				fireButtonEvent(new ButtonEvent(mouseEvent, button, ButtonEvents.CLICKED));
			}
		});
	}

	private void fireButtonEvent(ButtonEvent event) {
		for (ButtonListener listener : buttonHandler.getButtonListeners())
			listener.buttonClicked(event);
	}

	@Override
	public void onStatusMessage(NetworkEvent event) {
		if (getType() == Type.CLIENT) {
			if (event.getEvent() == NetworkEvents.START_CLIENT)
				setActive(true);
			else if (event.getEvent() == NetworkEvents.SHUTDOWN_CLIENT)
				setActive(false);
		} else if (getType() == Type.SERVER) {
			if (event.getEvent() == NetworkEvents.START_SERVER)
				setActive(true);
			else if (event.getEvent() == NetworkEvents.SHUTDOWN_SERVER)
				setActive(false);
		}
	}
}
