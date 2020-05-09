package server_side;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MySerialServer implements Server {

	private volatile boolean stop;

	@Override
	public void open(int port, ClientHandler ch) throws IOException {
		// TODO Auto-generated method stub
		ServerSocket server = new ServerSocket(port);
		server.setSoTimeout(1000);
		new Thread(()->{
			try {
				start(server, ch);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
		server.close();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		stop = true;
	}

	public void start(ServerSocket server, ClientHandler ch) throws IOException {
		while (!stop) {
			try {
				Socket aClient = server.accept();
				try {
					ch.handleClient(aClient.getInputStream(), aClient.getOutputStream());
					aClient.getInputStream().close();
					aClient.getOutputStream().close();
					aClient.close();
				} catch (IOException e) {
					/* ... */}
			} catch (SocketTimeoutException e) {
				/* ... */}
		}

	}
}
