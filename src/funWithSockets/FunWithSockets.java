package funWithSockets;

import java.io.IOException;
import java.util.Scanner;

import funWithSockets.Client;

public class FunWithSockets {
	public static void main(String[] args) {
		FunWithSockets sockets = new FunWithSockets();
		int port = 7701;
		Scanner s = new Scanner(System.in);

		try {
			System.out.println("1: Client, 2: Server");
			int choice = s.nextInt();
			if (choice == 1) {
				System.out.println("Starting client...");
				Client client = new Client(port);
			} else if (choice == 2) {
				System.out.println("Starting server...");
				Server server = new Server(port);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
