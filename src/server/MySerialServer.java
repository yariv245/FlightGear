package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MySerialServer implements Server {

	private volatile boolean stop;
	
	@Override
	public void start(int port, ClientHandler ch) throws IOException {
		new Thread(()->{
			try {
				open(port, ch);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	@Override
	public void stop() {
		stop = true;
	}
	@Override
	public void open(int port, ClientHandler ch) throws IOException {
		ServerSocket server = new ServerSocket(port);
		server.setSoTimeout(1000);
		while (!stop) {
			try {
				Socket aClient = server.accept();
				System.out.println("Client is conected");

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
		server.close();

	}

}
