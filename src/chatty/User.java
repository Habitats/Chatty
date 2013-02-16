package chatty;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class User implements Serializable {
	private final String userName;
	private String displayName;
	private int activePort;
	private Date created;
	private Date lastMessage;

	public User(String name) {
		this.userName = name;
		displayName = name;
		created = new Date();
		lastMessage = created;
	}

	public String getUsername() {
		return userName;
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

	@Override
	public String toString() {
		return String.format("[UN: %s - DN: %s - P: %s - C: %s - LM: %s]", userName, displayName, activePort, new SimpleDateFormat("HH:mm:ss:SSSS").format(created), new SimpleDateFormat("mm:ss:SSSS").format(lastMessage));
	}

	public Date getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage() {
		lastMessage = new Date();
	}
}
