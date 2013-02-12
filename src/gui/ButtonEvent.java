package gui;

import java.awt.event.MouseEvent;
import java.util.Date;

public class ButtonEvent {
	public enum ButtonEvents {
		CLICKED, //
		ACTIVATED, //
		DEACTIVATED, //
	}

	private Date date;
	private Button button;
	private ButtonEvents event;
	private final MouseEvent mouseEvent;

	public ButtonEvent(MouseEvent mouseEvent, Button button, ButtonEvents event) {
		this.mouseEvent = mouseEvent;
		this.date = new Date();
		this.event = event;
		this.button = button;
	}

	public Date getDate() {
		return date;
	}

	public Button getButton() {
		return button;
	}

	public ButtonEvents getEvent() {
		return event;
	}

	public MouseEvent getActionEvent() {
		return mouseEvent;
	}
}
