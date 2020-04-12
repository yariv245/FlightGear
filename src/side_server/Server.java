package side_server;

public interface Server {
	void open(int port,ClientHandler c); // Open server and wait for customers
	void stop(); // Close the server
}
	