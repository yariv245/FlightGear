
package server_side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class MyTestClientHandler implements ClientHandler {
	Solver solver;
	CacheManager cm;

	public MyTestClientHandler(CacheManager cm) {
		this.cm = cm;
	}

	public void handleClient(InputStream inFromClient, OutputStream outToClient) {
		BufferedReader userInput = new BufferedReader(new InputStreamReader(inFromClient));
		PrintWriter outClient = new PrintWriter(outToClient);
		
		readInputsAndSend(userInput,outClient);
	}
	private void readInputsAndSend(BufferedReader in, PrintWriter out) {
		try {
			String line;
			Solver solver= null;
			Solution sol = null;
			while (!(line = in.readLine()).equals("end")) {
				if(cm.existSolution(new Problem(line))) {
					out.println(cm.loadSolution(new Problem(line)));
					out.flush();
				}
				else {
					sol = solver.solve(new Problem(line));
					cm.store(new Problem(line), sol);
					out.println(sol);
					out.flush();
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
