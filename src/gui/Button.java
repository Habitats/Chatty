package gui;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JLabel;

import network.NetworkListener;

import chatty.Config;

public class Button extends JLabel implements NetworkListener {

	private boolean active = false;
	protected String text;
	protected Dimension dim;
	protected ButtonMouseListener buttonMouseListener = new ButtonMouseListener(this);
	private boolean toggleButton;
	private ArrayList<Button> toggleButtonGroup;

	public Button() {
		setPreferredSize(dim);
		init();
	}

	public Button(String text) {
		setPreferredSize(dim);
		init();
	}

	public Button(String text, Dimension dim) {
		setText(this.text = text);
		init();
	}

	private void init() {
		addMouseListener(buttonMouseListener);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	ButtonMouseListener getButtonMouseListener() {
		return buttonMouseListener;
	}

	@Override
	public void startServer() {
		System.out.println("BUNCH OF DICKS");
	}

	@Override
	public void startClient(String hostname, int port) {
	}

	@Override
	public void onCrash() {
	}

	@Override
	public void lostConnection() {
	}

	@Override
	public void serverDisconnect() {
	}

	@Override
	public void startClient() {
		// TODO Auto-generated method stub

	}

}
