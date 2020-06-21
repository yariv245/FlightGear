/**
 * 
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class MyClientHandler implements ClientHandler {

	State<String> start;
	State<String> goal;
	MatProblem mat;
	
	SearcherSolver s;
	CacheManager<String, String> cm;	
	
	public MyClientHandler() {
		start= new State<String>("1",0,0,5);
		goal= new State<String>("1",9,9,5);
		mat= new MatProblem(start,goal);
		
		s = new SearcherSolver(new BFS<>());
		this.cm = new FileCacheManager<String, String>();
	}

	public void handleClient(/*InputStream inFromClient, OutputStream outToClient*/) {
		
		s.solve(mat);
		
		
		
		
//		BufferedReader userInput = new BufferedReader(new InputStreamReader(inFromClient));
//		PrintWriter outClient = new PrintWriter(outToClient);
//
//		readInputsAndSend(userInput, outClient);
//	}
//
//	private void readInputsAndSend(BufferedReader in, PrintWriter out) {
//		try {
//			String line,solution;
//			while (!(line = in.readLine()).equals("end")) {
//				if (cm.existSolution(line)) {
//					out.println(cm.loadSolution(line));
//				} else {
//					solution = this.solver.solve(line);
//					cm.store(line, solution);
//					out.println(solution);
//				}
//				out.flush();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}
