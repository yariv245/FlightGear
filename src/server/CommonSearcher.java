package server;

import java.util.PriorityQueue;
import java.util.Stack;

public abstract class CommonSearcher<T> implements Searcher<T> {

	protected PriorityQueue<State<T>> openList;
	private int evaluatedNodes;

	public CommonSearcher() {
		openList = new PriorityQueue<State<T>>();
		evaluatedNodes = 0;
	}
	
	public Solution<T> backtrace(State<T> goal) {
		MatSolution<T> s = new MatSolution<T>(new Stack<T>());
		while(goal.getCameFrom() != null) {
			s.store(goal.getCameFrom().getState());
			goal=goal.getCameFrom();
		}
		return s;
	}

	final protected State<T> popOpenList() {
		evaluatedNodes++;
		return openList.poll();
	}

	@Override
	public int getNumberOfNodesEvaluated() {
		return evaluatedNodes;
	}

	@Override
	public abstract Solution<T> search(Searchable<T> s);
}
