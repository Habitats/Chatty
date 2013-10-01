package network;

public interface NetworkListener {

	// SERVER EVENTS

	// public void onStatusMessage(ServerEvent event);
	//
	// public void onNormalMessage(ServerEvent event);

	// CLIENT EVENTS

	public void onStatusMessage(NetworkEvent event);

	public void onNormalMessage(NetworkEvent event);
}
