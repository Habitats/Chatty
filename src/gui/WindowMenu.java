package gui;

import gui.GBC.Align;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import gui.Button;

import javax.swing.JPanel;

import msg.ChatEvent;
import msg.ChatEvent.Receipient;

public class WindowMenu extends JPanel {

	private ArrayList<WindowButtonListener> windowButtonListeners = new ArrayList<WindowButtonListener>();
	private ArrayList<WindowButton> windowButtons = new ArrayList<WindowButton>();

	public WindowMenu() {
		setLayout(new GridBagLayout());
		setBackground(Color.BLACK);

		Dimension buttonDim = new Dimension(0, 30);
		WindowButton statusButton = new WindowButton(Receipient.STATUS, buttonDim, this);
		WindowButton channelButton = new WindowButton(Receipient.CHANNEL, buttonDim, this);
		WindowButton queryButton = new WindowButton(Receipient.QUERY, buttonDim, this);
		WindowButton globalButton = new WindowButton(Receipient.GLOBAL, buttonDim, this);

		windowButtons.add(statusButton);
		windowButtons.add(channelButton);
		windowButtons.add(queryButton);
		windowButtons.add(globalButton);

		add(statusButton, new GBC(0, 0, Align.ONLY_RIGHT).setIpad(50, 0));
		add(channelButton, new GBC(1, 0, Align.ONLY_RIGHT).setIpad(50, 0));
		add(queryButton, new GBC(2, 0, Align.ONLY_RIGHT).setIpad(50, 0));
		add(globalButton, new GBC(3, 0, Align.ONLY_RIGHT).setIpad(50, 0));

		add(new WindowButton(buttonDim), new GBC(4, 0).setWeight(1, 0));
	}

	public void addWindowButtonListener(WindowButtonListener windowButtonListener) {
		windowButtonListeners.add(windowButtonListener);
	}

	public ArrayList<WindowButtonListener> getWindowButtonListeners() {
		return windowButtonListeners;
	}

	public void show(Receipient rec, WindowButton button) {
		for (WindowButtonListener windowButtonListener : windowButtonListeners)
			windowButtonListener.setVisible(windowButtonListener.getReceipient() == rec);
		for (WindowButton windowButton : windowButtons)
			windowButton.setActive(button == windowButton);
	}
}
