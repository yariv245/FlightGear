package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MySerialServer implements Server {

	private volatile boolean stop;
	
	@Override
	public void start(int port, ClientHandler ch) throws IOException {
		// TODO Auto-generated method stub
		new Thread(()->{
			try {
				open(port, ch);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		stop = true;
	}
	@Override
	public void open(int port, ClientHandler ch) throws IOException {
		ServerSocket server = new ServerSocket(port);
		server.setSoTimeout(1000);
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
		server.close();

	}

}
