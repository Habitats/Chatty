package chatty;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.IOException;

import network.client.ClientEvent;
import network.client.ClientEvent.ClientEvents;

public class Config {
	// general settings
	public static final String CHATTY_VER = "Chatty v0.5 - Work in progress";
	public static final String APPLICATION_NAME = "Chatty";

	// general GUI stuff
	public static final int BORDER_WIDTH = 5;
	public static final String SEP = " > ";

	public static Font genFont(float size, boolean bold) {
		Font font = null;
		if (font == null) {
			try {
				// font = Font.createFont(Font.TRUETYPE_FONT,
				// getClass().getResourceAsStream("/res/8-BitCustom.TTF"));
				font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("./res/consola.ttf"));
				font = font.deriveFont(size);
				if (bold)
					font = font.deriveFont(Font.BOLD);
			} catch (IOException | FontFormatException e) {
				System.err.println("LOADING FONT FAILED!");
			}
		}
		return font;
	}


}
