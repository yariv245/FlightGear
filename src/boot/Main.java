package boot;

import java.io.IOException;

import server.ClientHandler;
import server.MySerialServer;
import server.MyTestClientHandler;
import server.Server;

public class Main {

	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		Server s = new MySerialServer();
		ClientHandler testch = new MyTestClientHandler();
		try {
			s.start(port, testch);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
