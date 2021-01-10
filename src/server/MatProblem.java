package server;

import java.util.ArrayList;
import java.util.Collection;

public class MatProblem implements Searchable<String> {

    ArrayList<ArrayList<State<String>>> board;
    State<String> startposition;
    State<String> goalposition;

    public MatProblem(ArrayList<ArrayList<State<String>>> board, State<String> startposition,
                      State<String> goalposition) {
        this.startposition = startposition;
        this.goalposition = goalposition;
        this.board = board;
    }

    public MatProblem() {
        this.board = new ArrayList<ArrayList<State<String>>>();
        this.startposition = new State<String>();
        this.goalposition = new State<String>();
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
        int x = this.board.size();
        int y = this.board.get(0).size();
        if (isCabin(i, j, x, y)) {
            if (isCabin(i + 1, j, x, y))
                neighbors.add(board.get(i + 1).get(j));
            if (isCabin(i - 1, j, x, y))
                neighbors.add(board.get(i - 1).get(j));
            if (isCabin(i, j + 1, x, y))
                neighbors.add(board.get(i).get(j + 1));
            if (isCabin(i, j - 1, x, y))
                neighbors.add(board.get(i).get(j - 1));
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
