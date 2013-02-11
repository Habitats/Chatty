package gui;

import java.awt.Color;

public class Themes {

	public static Color FOREGROUND;
	public static Color BACKGROUND;
	public static Color BUTTON_COLOR_ACTIVE;
	public static Color BUTTON_COLOR_CLICK;
	public static Color BUTTON_COLOR_DEFAULT;
	public static Color BUTTON_COLOR_HOVER;

	public static final int[] DREAM = { 0x207178, 0x173E4F, 0x174F5A, 0xFF9666, 0xF5E9BE, 0x174C4F };
	public static final int[] GRAY = { 0x373737, 0x151515, 0x151515, 0x373737, 0x909090, 0x222222 };

	public static void setTheme(int[] THEME) {
		int i = 0;
		BUTTON_COLOR_HOVER = new Color(THEME[i++]);
		BUTTON_COLOR_DEFAULT = new Color(THEME[i++]);
		BUTTON_COLOR_CLICK = new Color(THEME[i++]);
		BUTTON_COLOR_ACTIVE = new Color(THEME[i++]);
		FOREGROUND = new Color(THEME[i++]);
		BACKGROUND = new Color(THEME[i++]);
	}
}
