package msg;

public interface MessageListener {

	public void onChatEvent(ChatEvent msg);

	public void clear();

}
