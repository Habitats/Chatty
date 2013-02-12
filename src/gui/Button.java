package gui;

import gui.MenuButton.Type;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import network.NetworkListener;
import network.client.ClientEvent;
import network.server.ServerEvent;

import chatty.Config;

public  abstract class Button extends JLabel implements NetworkListener {

	private boolean active = false;
	protected Dimension dim;
	private ButtonMouseListener buttonMouseListener = new ButtonMouseListener(this);
	private Type type;

	public Button() {
		setPreferredSize(dim);
		init();
	}

	public Button(String text) {
		setText(text.toUpperCase());
		init();
	}

	public Button(String text, Dimension dim) {
		setText(text.toUpperCase());
		init();
	}

	private void init() {
		addMouseListener(buttonMouseListener);
		setFont(Config.genFont(18, true));
	}

	public boolean isActive() {
		return active;
	}

	protected void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public void setActive(boolean active) {
		this.active = active;
		if (active)
			setBackground(Themes.BUTTON_COLOR_ACTIVE);
		else
			setBackground(Themes.BUTTON_COLOR_DEFAULT);
	}


	@Override
	public void serverStatus(ServerEvent event) {
	}

	@Override
	public void clientStatus(ClientEvent event) {
	}

	@Override
	public void clientMessage(ClientEvent event) {
	}

	@Override
	public void serverNormalMessage(ServerEvent event) {
		// TODO Auto-generated method stub
		
	}
}
