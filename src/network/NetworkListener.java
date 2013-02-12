package network;

import network.client.ClientEvent;
import network.server.ServerEvent;

public interface NetworkListener {

	// SERVER EVENTS

	public void serverStatus(ServerEvent event);

	public void serverNormalMessage(ServerEvent event);

	// CLIENT EVENTS

	public void clientStatus(ClientEvent event);

	public void clientMessage(ClientEvent event);
}
