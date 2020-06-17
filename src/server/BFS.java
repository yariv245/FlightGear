package server;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

public class BFS<T> extends CommonSearcher<T> {

	@Override
	public Solution search(Searchable<T> s) {
		openList.add(s.getInitialState());
		HashSet<State<T>> closedSet = new HashSet<State<T>>();

		while (openList.size() > 0) {
			State<T> n = popOpenList();// dequeue
			closedSet.add(n);
			if (s.isGoalState(n)) {
				return backTrace(n);
				// private method, back traces through the parents
			}

			ArrayList<State<T>> successors = (ArrayList<State<T>>) s.getAllPossibleStates(n);

			for (State<T> state : successors) {
				if (!closedSet.contains(state) && !openList.contains(state)) {
					state.setCameFrom(n);
					state.setTotalCost(state.getCost() + n.getTotalCost());
					openList.add(state);
				} else if (state.getTotalCost() > state.getCost() + n.getTotalCost()) {
					state.setTotalCost(state.getCost() + n.getTotalCost());
					state.setCameFrom(n);
					if (!openList.contains(state)) {
						openList.add(state);
					} else {// Update priority queue
						State<T> x = popOpenList();
						openList.add(x);
					}
				}

			}

		}
		return null;
	}
}
