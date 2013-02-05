package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;

import chatty.Chatty;

public class MenuButton extends Button {

	public enum Type {
		OPTIONS("options"), //
		SERVER("server"), //
		CLIENT("client");

		String text;

		Type(String text) {
			this.text = text;
		}

		String getText() {
			return text;
		}
	}

	private Color color = Color.DARK_GRAY;
	protected Type type;

	public MenuButton(final Type TYPE, int buttonWidth, final EventListener feedListener, final Chatty main) {
		setBackground(color);
		// font color
		setForeground(Color.white);
		setText(TYPE.text);
		setPreferredSize(new Dimension(buttonWidth, 30));
		setOpaque(true);
		setHorizontalAlignment(SwingConstants.CENTER);
		
		addMouseListener(new ButtonMouseListener(this) {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!super.button.isActive()) {
					if (TYPE == Type.SERVER && !main.getNetworkHandler().isRunning()) {
						color = Color.red;
						main.getNetworkHandler().startServer();
						setActive(true);
					} else if (TYPE == Type.CLIENT && !main.getNetworkHandler().isRunning()) {
						color = Color.red;
						main.getNetworkHandler().startClient("bob");
						setActive(true);
					}
				} else {
					main.getNetworkHandler().shutDown();
					setActive(false);
				}
			}
		});
	}

	public void onCrash() {
		setActive(false);
	}


}
