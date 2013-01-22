package funWithSockets;

public class ConversationProtocol {

	private static final int WAITING = 0;
	private static final int SENTMSG = 1;

	private int state = WAITING;
	
	public String processinput(String theInput) {
		String theOutput = null;
		
		if (state == WAITING){
			theOutput = "YOYOYO";
		}
		return theOutput;
	}

}
