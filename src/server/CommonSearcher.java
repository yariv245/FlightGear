package server;

import java.util.ArrayList;
import java.util.PriorityQueue;

public abstract class CommonSearcher<Problem> implements Searcher<Problem> {
	protected PriorityQueue<State<Problem>> openList;
	private int evaluatedNodes;

	public CommonSearcher() {
		openList = new PriorityQueue<State<Problem>>();
		evaluatedNodes = 0;
	}
	public Solution<Problem> backTrace(State<Problem> goal) {
		MatSolution<Problem> s = new MatSolution<Problem>(new ArrayList<Problem>());
		while(goal.getCameFrom() != null) {
			s.store(goal.getCameFrom().getState());
			goal=goal.getCameFrom();
		}
		return s;
	}

	final protected State<Problem> popOpenList() {
		evaluatedNodes++;
		return openList.poll();
	}

	public int getNumberOfNodesEvaluated() {
		return evaluatedNodes;
	}
	
	@Override
	public abstract Solution<Problem> search(Searchable<Problem> problem);
}
