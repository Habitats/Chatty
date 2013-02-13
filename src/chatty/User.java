package chatty;

import java.io.Serializable;
import java.net.Socket;

public class User implements Serializable {
	private String userName;
	private String displayName;
	private int activePort;

	public User(String name) {
		this.userName = name;
	}

	public String getName() {
		return userName;
	}

	public void setName(String name) {
		this.userName = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setActivePort(int activePort) {
		this.activePort = activePort;
	}

	public int getActivePort() {
		return activePort;
	}
}
