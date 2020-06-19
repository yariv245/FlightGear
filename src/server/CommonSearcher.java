package server;

import java.util.PriorityQueue;

public abstract class CommonSearcher<T> implements Searcher<T> {

	protected PriorityQueue<State<T>> openList;
	private int evaluatedNodes;

	public CommonSearcher() {
		openList = new PriorityQueue<State<T>>();
		evaluatedNodes = 0;
	}
	
	public Solution backtrace(State<T> goal) {
		MatSolution<Problem> s = new MatSolution<Problem>(new ArrayList<Problem>());
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
	public abstract Solution search(Searchable<T> s);
}
