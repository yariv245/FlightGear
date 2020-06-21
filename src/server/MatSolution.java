package server;

import java.util.Stack;

public class MatSolution<T> implements Solution<T> {
	
	Stack<T> states;
	
	public MatSolution(Stack<T> stack){
		this.states=stack;
	}

	public Stack<T> getStates() {
		return states;
	}
	
	public void store(T ele) {
		this.states.add(ele);
	}
}
