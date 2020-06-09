package server;

import java.util.PriorityQueue;

public abstract class CommonSearcher<T> implements Searcher<T> {
	protected PriorityQueue<State<T>> openList;
	private int evaluatedNodes;

	public CommonSearcher() {
		openList = new PriorityQueue<State<T>>();
		evaluatedNodes = 0;
	}
	public T backTrace(State<T> goal , State<T> initialState) {
		T result;
		while(goal.getCameFrom() != null) {
			result+=goal.getCameFrom().getState();
		}
	}

	final protected State<T> popOpenList() {
		evaluatedNodes++;
		return openList.poll();
	}

	public int getNumberOfNodesEvaluated() {
		return evaluatedNodes;
	}
	
	@Override
	public abstract T search(Searchable<T> s);
}
