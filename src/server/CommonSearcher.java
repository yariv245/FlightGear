package server;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public abstract class CommonSearcher<T> implements Searcher<T> {
	protected PriorityQueue<State<T>> openList;
	private int evaluatedNodes;

	public CommonSearcher() {
		openList = new PriorityQueue<State<T>>();
		evaluatedNodes = 0;
	}
	public Solution backTrace(State<T> goal) {
		Solution<State<T>> s = new Solution<State<T>>(new ArrayList<State<T>>());
		while(goal.getCameFrom() != null) {
			s.store(goal.getCameFrom());
			goal=goal.getCameFrom();
		}
		return s;
	}

	final protected State<T> popOpenList() {
		evaluatedNodes++;
		return openList.poll();
	}

	public int getNumberOfNodesEvaluated() {
		return evaluatedNodes;
	}
	
	@Override
	public abstract Solution<?> search(Searchable<T> s);
}
