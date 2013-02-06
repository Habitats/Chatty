package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;

import network.client.ClientEvent;
import network.server.ServerEvent;

import chatty.Chatty;

public class MenuButton extends Button {

	public enum Type {
		OPTIONS("options"), //
		SERVER("server"), //
		CLIENT("client");

		private String text;

		Type(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}

	private Color color = Color.DARK_GRAY;

	public MenuButton(final Type type, int buttonWidth, final Chatty main) {
		setBackground(color);
		// font color
		setForeground(Color.white);
		setText(type.getText());
		setType(type);
		setPreferredSize(new Dimension(buttonWidth, 30));
		setOpaque(true);
		setHorizontalAlignment(SwingConstants.CENTER);

		addMouseListener(new ButtonMouseListener(this) {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!super.button.isActive()) {
					if (type == Type.SERVER && !main.getController().getNetworkHandler().isRunning()) {
						main.getController().getNetworkHandler().startServer();
					} else if (type == Type.CLIENT && !main.getController().getNetworkHandler().isRunning()) {
						main.getController().getNetworkHandler().startClient();
					}
				} else {
					main.getController().getNetworkHandler().shutDown();
				}
			}
		});
	}

	@Override
	public void serverStart(ServerEvent event) {
		if (getType() == Type.SERVER)
			setActive(true);
	}

	@Override
	public void serverShutDown(ServerEvent event) {
		setActive(false);
	}

	@Override
	public void clientStart(ClientEvent event) {
		if (getType() == Type.CLIENT)
			setActive(true);
	}

	@Override
	public void clientShutDown(ClientEvent event) {
		setActive(false);
	}
}
