package server;

import java.io.Serializable;
import java.util.Stack;
//
public class MatSolution<T> implements Solution<T> {
	
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
