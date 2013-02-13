package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class RightClickMenu extends JPanel {
	public RightClickMenu(Dimension dim) {
		setVisible(false);
		setSize(dim);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setVisible(false);
			}
		});
	}

	public enum RightClickType {
		NICK, //
		DEFAULT, //
	}

	public void open(RightClickType type) {
		switch (type) {
		case NICK:
			setBackground(Color.BLUE);
			break;
		case DEFAULT:
			setBackground(Color.RED);
			break;
		}
		setVisible(true);
	}
}
