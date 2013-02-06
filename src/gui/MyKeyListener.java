package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyKeyListener implements KeyListener {

	@Override
	public void keyPressed(KeyEvent key) {
		System.out.println("Key pressed: " + key.getKeyChar());
		if (key.getKeyCode() == KeyEvent.VK_ESCAPE)
			System.exit(0);
		if (key.getKeyChar() == 'q')
			System.exit(0);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
