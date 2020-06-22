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
import java.util.ArrayList;
import java.util.Stack;
//
public class MyClientHandler implements ClientHandler {
	SearcherSolver solver;
	CacheManager<Searchable<String>, Solution<State<String>>> cm;

	public MyClientHandler() {
		this.solver = new SearcherSolver(new BFS<>());
		this.cm = new FileCacheManager<Searchable<String>, Solution<State<String>>>();
	}

	public void handleClient(InputStream inFromClient, OutputStream outToClient) {

		BufferedReader userInput = new BufferedReader(new InputStreamReader(inFromClient));
		PrintWriter outClient = new PrintWriter(outToClient);
		ArrayList<String> string_matrix = new ArrayList<String>();
		State<String> start;
		State<String> goal;
		State<String>[][] board;
		MatProblem mat;
		String[] postions = new String[2];
		Solution<State<String>> solution = new MatSolution<State<String>>();
		try {
			String line; // "2,3,4,6,7,8"
			while (!(line = userInput.readLine()).equals("end")) {
				string_matrix.add(line);
			}
			board = convert_to_matrix(string_matrix); // Convert string_matrix to state<String> matrix
			line = userInput.readLine();
			postions = line.split(",");
			start = board[Integer.parseInt(postions[0])][Integer.parseInt(postions[1])]; //Set start position
			start.setTotalCost(start.getCost());
			line = userInput.readLine();
			postions = line.split(",");
			goal = board[Integer.parseInt(postions[0])][Integer.parseInt(postions[1])]; //Set goal position
			mat = new MatProblem(board, start, goal);
			
			if (cm.existSolution(mat)) {
				outClient.println(cm.loadSolution(mat));
			} else {
				solution = this.solver.solve(mat);
				cm.store(mat, solution);
				
				Stack<State<String>> stack = solution.getStates();
				State<String> from = stack.pop();
				State<String> to = stack.pop();
				String path = direction(from, to);
				while (!stack.isEmpty()) {
					from = to;
					to = stack.pop();
					path = path + "," + direction(from, to);
				}
				outClient.println(path);
			}
			outClient.flush();
		} catch (

		IOException e) {
			e.printStackTrace();
		}	

	}

	public String direction(State to, State from) {
		if (from.getCol() > to.getCol())
			return "Right";
		else if (from.getRow() > to.getRow())
			return "Down";
		else if (from.getCol() < to.getCol())
			return "Left";
		else if (from.getRow() < to.getRow())
			return "Up";

		return null;
	}

	private State<String>[][] convert_to_matrix(ArrayList<String> string_matrix) {
		State<String>[][] matrix = (State<String>[][]) new State[string_matrix.size()][string_matrix.size()];
		int j = 0;
		for (int i = 0; i < string_matrix.size(); i++) {
			for (String s : string_matrix.get(i).split(",")) {
				matrix[i][j] = new State<String>(s, i, j, Integer.parseInt(s));
				j++;
			}
			j = 0;
		}
		return matrix;
	}

}

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
//	}
