package gui;

import gui.MenuButton.Type;

import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import network.NetworkEvent;
import network.NetworkListener;

public abstract class Button extends JLabel implements NetworkListener {

	private static final long serialVersionUID = 1L;
	private boolean active = false;
	protected Dimension dim;
	private ButtonMouseListener buttonMouseListener = new ButtonMouseListener(this);
	private Type type;

	public Button() {
		setPreferredSize(dim);
		init();
	}

	public Button(Dimension dim) {
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
		setBackground(Themes.BUTTON_COLOR_DEFAULT);
		setOpaque(true);
		setHorizontalAlignment(SwingConstants.CENTER);
		// font color
		setForeground(Themes.FOREGROUND);
		addMouseListener(buttonMouseListener);
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
	public void onStatusMessage(NetworkEvent event) {
	}

	@Override
	public void onNormalMessage(NetworkEvent event) {
	}
}
