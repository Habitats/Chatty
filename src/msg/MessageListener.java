package msg;

import java.awt.event.ActionEvent;

public interface MessageListener {

	public void onChatEvent(ChatEvent msg);

	public void clear();

	public ChatEvent newChatEvent(ActionEvent ae);
}
