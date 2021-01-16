package calcServer;

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
        State<String> start = new State<String>();
        State<String> goal = new State<String>();
        ArrayList<ArrayList<State<String>>> board = new ArrayList<ArrayList<State<String>>>();
        MatProblem mat;
        String[] postions = new String[2];
        Solution<State<String>> solution = new MatSolution<State<String>>();
        try {
            String line; // "2,3,4,6,7,8"
            while (!(line = userInput.readLine()).equals("end")) {
//                System.out.println(line);
                string_matrix.add(line);
            }
            board = convert_to_matrix(string_matrix); // Convert string_matrix to state<String> matrix
            line = userInput.readLine();
            postions = line.split(",");
            start = board.get(Integer.parseInt(postions[0])).get(Integer.parseInt(postions[1])); // Set start position
            start.setTotalCost(start.getCost());
            line = userInput.readLine();
            postions = line.split(",");
            goal = board.get(Integer.parseInt(postions[0])).get(Integer.parseInt(postions[1])); // Set goal position
            mat = new MatProblem(board, start, goal);

            if (cm.existSolution(mat)) {
                outClient.println(cm.loadSolution(mat));
            } else {
                solution.getStates().clear();
                solution = this.solver.solve(mat);
                cm.store(mat, solution);

                Stack<State<String>> stack = new Stack<State<String>>();
                State<String> from = new State<String>();
                State<String> to = new State<String>();
                stack = solution.getStates();
                from = stack.pop();
                to = stack.pop();
//                System.out.println("from: " + from.getM_state() + " to: " + to.getM_state());
                String path = direction(from, to);
                while (!stack.isEmpty()) {
                    from = to;
                    to = stack.pop();
                    path = path + "-" + direction(from, to);
                }
                //Return answer to client
                outClient.println(path);
                stack.clear();
            }

            outClient.flush();
        } catch (

                IOException e) {
            e.printStackTrace();
        }

    }

    public String direction(State<String> from, State<String> to) {
//        if (from.getCol() > to.getCol())
//            return "Right";
//        else if (from.getRow() > to.getRow())
//            return "Down";
//        else if (from.getCol() < to.getCol())
//            return "Left";
//        else if (from.getRow() < to.getRow())
//            return "Up";
//"From: ["+from.row+","+from.col+"] "+" To: ["+to.row+","+to.col+"]"
        return to.row+","+to.col;
    }

    private ArrayList<ArrayList<State<String>>> convert_to_matrix(ArrayList<String> string_matrix) {
        ArrayList<ArrayList<State<String>>> matrix = new ArrayList<ArrayList<State<String>>>();
        int j = 0;
        for (int i = 0; i < string_matrix.size(); i++) {
            matrix.add(new ArrayList<State<String>>());
            for (String s : string_matrix.get(i).split(",")) {
                matrix.get(i).add(new State<String>(s, i, j, Integer.parseInt(s)));
                j++;
            }
            j = 0;
        }
        return matrix;
    }

}
