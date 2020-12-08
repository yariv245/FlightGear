package server;

import java.util.Stack;
//
public class MatSolution<T> implements Solution<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3001776113483565234L;
	Stack<T> states;
	
	public MatSolution(){
		this.states= new Stack<T>();
	}

	public Stack<T> getStates() {
		return states;
	}

	
	public void store(T t) {
		this.states.add(t);
	}
}