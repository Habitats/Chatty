package gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class GBC extends GridBagConstraints {

	public enum Align {
		LEFT, RIGHT, MID, TIGHT;
	}

	public GBC(int gridx, int gridy, Align align) {
		this.gridx = gridx;
		this.gridy = gridy;
		if (align != null) {
			if (align == Align.LEFT)
				setInsets(5, 5, 5, 5);
			else if (align == Align.MID)
				setInsets(5, 5, 5, 5);
			else if (align == Align.RIGHT)
				setInsets(5, 5, 5, 5);
			else if (align == Align.TIGHT)
				setInsets(3, 5, 3, 5);
		}
		setFill(BOTH);
	}

	// how many grids shall it span
	public GBC setSpan(int gridwidth, int gridheight) {
		this.gridwidth = gridwidth;
		this.gridheight = gridheight;
		return this;
	}

	/* Fill area if the component doesn't match the available space given
	 * HORIZONTAL = fill horizontally
	 * VERTICAL = fill vertically
	 * BOTH = guess
	 * */
	public GBC setFill(int fill) { // NO_UCD (use private)
		this.fill = fill;
		return this;
	}

	// this one's complicated, check docs lol
	public GBC setWeight(double weightx, double weighty) { // NO_UCD (unused code)
		this.weightx = weightx;
		this.weighty = weighty;
		return this;
	}

	// internal padding
	public GBC setInsets(int top, int left, int bottom, int right) { // NO_UCD (use private)
		this.insets = new Insets(top, left, bottom, right);
		return this;
	}

	public GBC setIpad(int ipadx, int ipady) { // NO_UCD (unused code)
		this.ipadx = ipadx;
		this.ipady = ipady;
		return this;
	}
}
