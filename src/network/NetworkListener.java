package network;

import network.client.ClientEvent;
import network.server.ServerEvent;

public interface NetworkListener {

	// SERVER EVENTS
	public void serverStart(ServerEvent event);

	public void serverShutDown(ServerEvent event);

	public void clientDropped(ServerEvent event);

	public void serverCrashed(ServerEvent event);

	public void serverStatus(ServerEvent event);

	// CLIENT EVENTS
	public void clientStart(ClientEvent event);

	public void clientShutDown(ClientEvent event);

	public void clientCrashed(ClientEvent event);

	public void clientConnect(ClientEvent event);

	public void clientStatus(ClientEvent event);

	public void clientMessage(ClientEvent event);
}
