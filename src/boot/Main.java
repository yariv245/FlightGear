package boot;

import java.io.IOException;

import server.ClientHandler;
import server.MyClientHandler;
import server.MySerialServer;
import server.Server;

public class Main {

	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		Server s = new MySerialServer();
		ClientHandler testch = new MyClientHandler();
		try {
			s.start(port, testch);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
}
