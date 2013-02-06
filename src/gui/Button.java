package gui;

import gui.MenuButton.Type;

import java.awt.Dimension;

import javax.swing.JLabel;

import network.NetworkListener;
import network.client.ClientEvent;
import network.server.ServerEvent;

import chatty.Config;

public class Button extends JLabel implements NetworkListener {

	private boolean active = false;
	protected Dimension dim;
	private ButtonMouseListener buttonMouseListener = new ButtonMouseListener(this);
	private Type type;

	public Button() {
		setPreferredSize(dim);
		init();
	}

	public Button(String text) {
		setPreferredSize(dim);
		init();
	}

	public Button(String text, Dimension dim) {
		setText(text);
		init();
	}

	private void init() {
		addMouseListener(buttonMouseListener);
	}

	protected boolean isActive() {
		return active;
	}

	protected void setType(Type type) {
		this.type = type;
	}

	protected Type getType() {
		return type;
	}

	protected void setActive(boolean active) {
		this.active = active;
		if (active)
			setBackground(Config.BUTTON_COLOR_ACTIVE);
		else
			setBackground(Config.BUTTON_COLOR_DEFAULT);
	}

	@Override
	public void serverStart(ServerEvent event) {
	}

	@Override
	public void serverShutDown(ServerEvent event) {
	}

	@Override
	public void clientDropped(ServerEvent event) {
	}

	@Override
	public void serverCrashed(ServerEvent event) {
	}

	@Override
	public void clientStart(ClientEvent event) {
	}

	@Override
	public void clientShutDown(ClientEvent event) {
	}

	@Override
	public void clientCrashed(ClientEvent event) {
	}

	@Override
	public void clientConnect(ClientEvent event) {
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
}
