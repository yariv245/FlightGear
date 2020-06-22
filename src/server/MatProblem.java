package server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class MatProblem implements Searchable<String> {

	State<String>[][] board;
	State<String> startposition;
	State<String> goalposition;

	// T[][] arrays = (T[][])new Object[chunks][];
	public MatProblem(State<String>[][] board, State<String> startposition, State<String> goalposition) {
//		this.board = (State<String>[][]) new State[3][3];
//		Random random = new Random();
//		for (int i = 0; i < 3; i++) {
//			for (int j = 0; j < 3; j++) {
//				board[i][j] = new State<String>(String.valueOf(random.nextInt(1)), i, j, random.nextInt(10));
//			}
//		}
		this.startposition = startposition;
		this.goalposition = goalposition;
		this.board = board;
	}

	@Override
	public State<String> getInitialState() {
		return this.startposition;
	}

	@Override
	public boolean isGoalState(State<String> s) {
		return goalposition.equals(s);
	}

	@Override
	public Collection<State<String>> getAllPossibleStates(State<String> s) {
		ArrayList<State<String>> neighbors = new ArrayList<State<String>>();

		int i = s.getRow();
		int j = s.getCol();
		int x = this.board.length;
		int y = this.board[0].length;
		if (isCabin(i, j, x, y)) {
			if (isCabin(i + 1, j, x, y))
				neighbors.add(board[i + 1][j]);
			if (isCabin(i - 1, j, x, y))
				neighbors.add(board[i - 1][j]);
			if (isCabin(i, j + 1, x, y))
				neighbors.add(board[i][j + 1]);
			if (isCabin(i, j - 1, x, y))
				neighbors.add(board[i][j - 1]);
		}
		return neighbors;
	}

	public static boolean isCabin(int i, int j, int x, int y) {
		boolean flag = false;
		if (i >= 0 && i < x && j >= 0 && j < y) {
			flag = true;
		}
		return flag;
	}

}
