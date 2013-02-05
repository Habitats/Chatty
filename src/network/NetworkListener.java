package network;

public interface NetworkListener {
	public void startServer();

	public void startClient(String name);

	public void onCrash();

	public void lostConnection();

	public void serverDisconnect();

}
