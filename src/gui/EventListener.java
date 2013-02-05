package gui;

public interface EventListener {

	public void sendStatusToOwnFeed(String msg);

	public void sendNormalMessageToOwnFeed(String msg);
	
	public void sendErrorToOwnFeed(String msg);
}
