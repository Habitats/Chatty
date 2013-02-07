package gui;

import gui.ButtonEvent.Event;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.SwingConstants;

import network.client.ClientEvent;
import network.server.ServerEvent;

import chatty.Chatty;
import chatty.Config;
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

	private Color color = Color.DARK_GRAY;
	private final ButtonHandler buttonHandler;

	public MenuButton(final Type type, Dimension dim, ButtonHandler buttonHandler) {
		this.buttonHandler = buttonHandler;
		setBackground(color);
		// font color
		setForeground(Config.TEXT_COLOR_DEFAULT_MENU);
		setText(type.getText());
		setType(type);

		setPreferredSize(dim);
		setMinimumSize(dim);

		setOpaque(true);
		setHorizontalAlignment(SwingConstants.CENTER);

		addMouseListener(new ButtonMouseListener(this) {

			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				fireButtonEvent(new ButtonEvent(mouseEvent, button, Event.CLICKED));
			}
		});
	}

	public void fireButtonEvent(ButtonEvent event) {
		for (ButtonListener listener : buttonHandler.getButtonListeners())
			listener.buttonClicked(event);
	}

	@Override
	public void serverStart(ServerEvent event) {
		if (getType() == Type.SERVER)
			setActive(true);
	}

	@Override
	public void serverShutDown(ServerEvent event) {
		setActive(false);
	}

	@Override
	public void clientStart(ClientEvent event) {
		if (getType() == Type.CLIENT)
			setActive(true);
	}

	@Override
	public void clientShutDown(ClientEvent event) {
		setActive(false);
	}
}
