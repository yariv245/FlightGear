package servers;

import java.io.IOException;

// change this to match your code

import calcServer.MySerialServer;
import calcServer.MyClientHandler;
import calcServer.Server;

public class CalcPathServer {

    static Server s;

    public static void runServer(int port) {
		// put the code here that runs your server
		// for example:
		s=new MySerialServer();
		try {
			s.start(port,new MyClientHandler());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void stopServer() {
		s.stop();
    }

}
