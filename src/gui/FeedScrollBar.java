package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.plaf.metal.MetalScrollBarUI;

public class FeedScrollBar extends MetalScrollBarUI {
	public FeedScrollBar() {

	}

	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		g.setColor(Color.WHITE);
	}

}
