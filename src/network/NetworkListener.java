package network;

public interface NetworkListener {
	public void startServer();

	public void onCrash();

	public void lostConnection();

	public void serverDisconnect();

	public void startClient(String hostname, int port);

	public void startClient();

}
