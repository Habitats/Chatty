package gui.options;

import gui.ButtonHandler;
import gui.GBC;
import gui.MenuButton;
import gui.MyKeyListener;
import gui.GBC.Align;
import gui.MenuButton.Type;
import gui.ButtonMouseListener;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

public class OptionsMenu extends JPanel {

	private final MenuButton submitButton;
	private final ButtonHandler buttonHandler;
	private ArrayList<OptionsField> optionFields = new ArrayList<OptionsField>();

	public enum Option {
		HOSTNAME("hostname"), //
		PORT("port"), //
		NICKNAME("nick"), //
		;
		private String option;

		Option(String option) {
			this.option = option;
		}

		public String getOption() {
			return option;
		}
	}

	public OptionsMenu(Dimension dim, ButtonHandler buttonHandler) {
		this.buttonHandler = buttonHandler;
		// setPreferredSize(dim);
		// setMinimumSize(dim);
		setLayout(new GridBagLayout());
		setVisible(false);

		Dimension labelDim = new Dimension(80, 20);
		Dimension fieldDim = new Dimension(160, 20);

		add(new OptionsLabel(labelDim, Option.HOSTNAME), new GBC(0, 0, Align.LEFT));
		add(new OptionsLabel(labelDim, Option.PORT), new GBC(0, 1, Align.LEFT));
		add(new OptionsLabel(labelDim, Option.NICKNAME), new GBC(0, 2, Align.LEFT));

		OptionsField hostnameField = new OptionsField(fieldDim, Option.HOSTNAME);
		OptionsField portField = new OptionsField(fieldDim, Option.PORT);
		OptionsField nicknameField = new OptionsField(fieldDim, Option.NICKNAME);

		optionFields.add(hostnameField);
		optionFields.add(portField);
		optionFields.add(nicknameField);

		add(hostnameField, new GBC(1, 0, Align.RIGHT));
		add(portField, new GBC(1, 1, Align.RIGHT));
		add(nicknameField, new GBC(1, 2, Align.RIGHT));

		submitButton = new MenuButton(Type.SUBMIT, null, buttonHandler);
		add(submitButton, new GBC(0, 3, Align.FULL_WIDTH_BOTTOM).setSpan(2, 1).setIpad(0, 5));
	}
	
	public ArrayList<OptionsField> getOptionFields() {
		return optionFields;
	}
}
