package server;

import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Comparator; 
  //
class The_Comparator implements Comparator<State> { 
    public int compare(State s1, State s2) 
    { 
        return s1.getTotalCost() < s2.getTotalCost() ? -1:1;
    } 
} 

public abstract class CommonSearcher<T> implements Searcher<T> {

	protected PriorityQueue<State<T>> openList;
	private int evaluatedNodes;

	public CommonSearcher() {
		openList = new PriorityQueue<State<T>>(new The_Comparator());
		evaluatedNodes = 0;
	}
	
	public Solution<State<T>> backtrace(State<T> goal) {
		MatSolution<State<T>> s = new MatSolution<State<T>>();
		s.store(goal.getState());
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
