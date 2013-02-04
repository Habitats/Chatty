package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import chatty.Chatty;


public class MenuButton extends JLabel {

	private Color color = Color.DARK_GRAY;

	public MenuButton(final String text, int buttonWidth, final FeedListener feedListener, final Chatty main) {
		setBackground(color);
		// font color
		setForeground(Color.white);
		setText(text);
		setPreferredSize(new Dimension(buttonWidth, 30));
		setOpaque(true);
		setHorizontalAlignment(SwingConstants.CENTER);

		addMouseListener(new MouseListener() {
			
			boolean clicked = false;

			@Override
			public void mouseReleased(MouseEvent arg0) {
				setBackground(Color.gray);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				setBackground(Color.blue);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				setBackground(color);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				setBackground(Color.gray);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				feedListener.sendMessageToFeed(text + " clicked");
				if (!clicked) {
					clicked = true;
					color = Color.red;
					if (text.equals("server"))
						main.getNetworkHandler().startServer();
					else
						main.getNetworkHandler().startClient("bob");
				}
			}
		});
	}
}
