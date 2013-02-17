package gui;

import msg.ChatEvent.Receipient;

public interface WindowButtonListener {

	Receipient getReceipient();

	void setVisible(boolean b);

}
