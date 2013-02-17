package msg;

import msg.ChatEvent.Receipient;


public interface MessageListener {

	public void onChatEvent(ChatEvent msg);

	public void clear();

	public Receipient getReceipient();

}
