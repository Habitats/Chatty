package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ButtonMouseListener implements MouseListener {

	Button button;

	public ButtonMouseListener(Button button) {
		this.button = button;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		button.setBackground(Themes.BUTTON_COLOR_HOVER);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (button.isActive())
			button.setBackground(Themes.BUTTON_COLOR_ACTIVE);
		else
			button.setBackground(Themes.BUTTON_COLOR_DEFAULT);
		if (button.isActive())
			button.setBackground(Themes.BUTTON_COLOR_ACTIVE);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		button.setBackground(Themes.BUTTON_COLOR_CLICK);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		button.setBackground(Themes.BUTTON_COLOR_HOVER);
	}
}
