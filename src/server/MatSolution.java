package server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

public class MatSolution<T> implements Solution<T> {
	
	Stack<State<T>> states;
	
	public MatSolution(Stack<State<T>> stack){
		this.states=stack;
	}

	public Stack<State<T>> getStates() {
		return states;
	}
	
	<E> Collection<E> getSolution(){
		
		
		return null;
	}

}
