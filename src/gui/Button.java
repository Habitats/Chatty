package gui;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JLabel;

import chatty.Config;

public abstract class Button extends JLabel {

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
}
