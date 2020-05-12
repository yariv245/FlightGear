
package server_side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class MyTestClientHandler implements ClientHandler {
	Solver<?, ?> solver;
	CacheManager cm;

	public MyTestClientHandler(CacheManager cm, Solver<?, ?> solver) {
		this.cm = cm;
		this.solver = solver;
	}

	public void handleClient(InputStream inFromClient, OutputStream outToClient) {
		BufferedReader userInput = new BufferedReader(new InputStreamReader(inFromClient));
		PrintWriter outClient = new PrintWriter(outToClient);

		readInputsAndSend(userInput, outClient);
	}

	private void readInputsAndSend(BufferedReader in, PrintWriter out) {
		try {
			String line;
			Solver<String, String> solver;
			solver = (str) -> {
				return new StringBuilder(str).reverse().toString();
			};
			while (!(line = in.readLine()).equals("end")) {
				if (cm.existSolution(line)) {
					out.println(cm.loadSolution(line));
				} else {
					out.println(solver.solve(line));
				}
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
