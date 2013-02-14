package gui;


import gui.ButtonEvent.ButtonEvents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.SwingConstants;

import network.client.ClientEvent;
import network.client.ClientEvent.ClientEvents;
import network.server.ServerEvent;
import network.server.ServerEvent.ServerEvents;

import chatty.Chatty;
import chatty.Controller;

public class MenuButton extends Button {

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
		this.buttonHandler = buttonHandler;
		setBackground(Themes.BUTTON_COLOR_DEFAULT);
		// font color
		setForeground(Themes.FOREGROUND);
		setText(type.getText().toUpperCase());
		setType(type);

		setPreferredSize(dim);
		setMinimumSize(dim);

		setOpaque(true);
		setHorizontalAlignment(SwingConstants.CENTER);

		addMouseListener(new ButtonMouseListener(this) {

			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				fireButtonEvent(new ButtonEvent(mouseEvent, button, ButtonEvents.CLICKED));
			}
		});
	}

	public void fireButtonEvent(ButtonEvent event) {
		for (ButtonListener listener : buttonHandler.getButtonListeners())
			listener.buttonClicked(event);
	}

	@Override
	public void serverStatus(ServerEvent event) {
		if (getType() == Type.SERVER) {
			if (event.getEvent() == ServerEvents.START)
				setActive(true);
			else if (event.getEvent() == network.server.ServerEvent.ServerEvents.SHUTDOWN)
				setActive(false);
		}
	}

	@Override
	public void clientStatus(ClientEvent event) {
		if (getType() == Type.CLIENT)
			if (event.getEvent() == ClientEvents.START)
				setActive(true);
			else if (event.getEvent() == ClientEvents.SHUTDOWN)
				setActive(false);
	}
}
