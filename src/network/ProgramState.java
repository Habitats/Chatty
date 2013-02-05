package network;

public abstract class ProgramState {
	private boolean running = false;
	protected int port;
	private boolean client = false;
	private boolean server = false;

	public abstract void kill();

	protected void setClient(boolean value) {
		client = value;
	};

	protected void setServer(boolean value) {
		server = value;
	};

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isRunning() {
		return running;
	}

	public boolean isServer() {
		return server;
	}

	public boolean isClient() {
		return client;
	}
}
