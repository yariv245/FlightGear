package server_side;

import java.io.IOException;
import java.net.ServerSocket;

public interface Server {
	void open(int port,ClientHandler c) throws IOException; // Open server and wait for customers
	void stop(); // Close the server
	void start(ServerSocket server, ClientHandler ch) throws IOException;
}
	