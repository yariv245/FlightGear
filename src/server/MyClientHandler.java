
package server;

import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MyClientHandler implements ClientHandler {
	SearcherSolver<Searchable<String>, Solutionit<String>> solver;
	CacheManager<ArrayList<String>, String> cm;W

	public MyClientHandler() {
		//this.solver = (str)->new StringBuilder(str).reverse().toString();
		this.cm = new FileCacheManager<ArrayList<String>, String>();
	}

	public void handleClient(InputStream inFromClient, OutputStream outToClient) {
		BufferedReader userInput = new BufferedReader(new InputStreamReader(inFromClient));
		PrintWriter outClient = new PrintWriter(outToClient);

		readInputsAndSend(userInput, outClient);
	}

	private void readInputsAndSend(BufferedReader in, PrintWriter out) {
		try {
			String line,solution;
			while (!(line = in.readLine()).equals("end")) {
				if (cm.existSolution(line)) {
					out.println(cm.loadSolution(line));
				} else {
					solution = this.solver.solve(line);
					cm.store(line, solution);
					out.println(solution);
				}
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
