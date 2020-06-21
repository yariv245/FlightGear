package server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class MatProblem implements Searchable<String> {

	//State<String>[][] board;
	State<String> startposition;
	State<String> goalposition;

	// T[][] arrays = (T[][])new Object[chunks][];
	public MatProblem(/* State<String>[][] p, */State<String> startposition, State<String> goalposition) {
		State<String>[][] board = (State<String>[][]) new State[10][];
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				board[i][j] = new State<String>(String.valueOf(random.nextInt(1)), i, j, random.nextInt(10));
			}
		}
		this.startposition = startposition;
		this.goalposition = goalposition;
	}

	@Override
	public State<String> getInitialState() {
		// TODO Auto-generated method stub
		return this.startposition;
	}

	@Override
	public boolean isGoalState(State<String> s) {
		// TODO Auto-generated method stub
		return goalposition.equals(s);
	}

	@Override
	public Collection<State<String>> getAllPossibleStates(State<String> s) {
		ArrayList<State<String>> lst = new ArrayList<State<String>>();

		if (s.getRow() != 0)
			lst.add(board[s.getRow()][s.getCol() - 1]);

		if (s.getRow() != this.board.length - 1)
			lst.add(board[s.getRow()][s.getCol() + 1]);

		if (s.getCol() != 0)
			lst.add(board[s.getRow() - 1][s.getCol()]);

		if (s.getCol() != this.board[0].length)
			lst.add(board[s.getRow() + 1][s.getCol()]);

		return lst;
	}

}
