package server;

import java.io.InputStream;//
import java.io.OutputStream;

public interface ClientHandler {
	void handleClient(InputStream in, OutputStream out);
}
