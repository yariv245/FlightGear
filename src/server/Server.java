package server;

import java.io.IOException;
//
public interface Server {
	void start(int port,ClientHandler c) throws IOException; // Open server and wait for customers
	void stop(); // Close the server
	void open(int port, ClientHandler ch) throws IOException;
}
	