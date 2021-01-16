package calcServer;

import java.util.ArrayList;
import java.util.HashSet;

public class BFS<Problem> extends CommonSearcher<Problem> {

    public BFS() {
        super();
    }

    @Override
    public Solution search(Searchable<Problem> s) {
        openList.add(s.getInitialState());
        HashSet<State<Problem>> closedSet = new HashSet<State<Problem>>();
        State<Problem> n = new State<Problem>();
        ArrayList<State<Problem>> successors = new ArrayList<>();

        while (openList.size() > 0) {
            n = popOpenList();// dequeue
            closedSet.add(n);
            if (s.isGoalState(n)) {
                return backtrace(n);
                // private method, back traces through the parent
            }

            successors.addAll(s.getAllPossibleStates(n));
            for (State<Problem> state : successors) {
                if (!closedSet.contains(state) && !openList.contains(state)) {
                    state.setCameFrom(n);
                    state.setTotalCost(state.getCost() + n.getTotalCost());
                    openList.add(state);
                } else if (state.getTotalCost() > state.getCost() + n.getTotalCost()) {
                    state.setTotalCost(state.getCost() + n.getTotalCost());
                    state.setCameFrom(n);
                    if (openList.contains(state)) {
                        openList.add(state);
                    } else {// Update priority queue
                        State<Problem> x = popOpenList();
                        openList.add(x);
                    }
                }

            }
            System.out.print(".");

        }
        return null;
    }

}
