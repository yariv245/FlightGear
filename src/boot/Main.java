package boot;

import java.io.IOException;

import server.CacheManager;
import server.ClientHandler;
import server.FileCacheManager;
import server.MySerialServer;
import server.MyTestClientHandler;
import server.Server;
import server.Solver;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int port = Integer.parseInt("6400");
		
		Server s = new MySerialServer();
		
		Solver<String,String> stringsolver;
		stringsolver = (str)->new StringBuilder(str).reverse().toString();
		
		CacheManager cm = new FileCacheManager();
		ClientHandler yariv = new MyTestClientHandler(cm,stringsolver);
		try {
			s.start(port, yariv);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
