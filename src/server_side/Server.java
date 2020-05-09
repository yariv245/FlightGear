package server_side;

import java.io.IOException;

public interface Server {
	void open(int port,ClientHandler c) throws IOException; // Open server and wait for customers
	void stop(); // Close the server
	void start(int port, ClientHandler ch);
}
	