package side_server;

public interface Server {
	void open(int port); // Open server and wait for customers
	void stop(); // Close the server
}
	