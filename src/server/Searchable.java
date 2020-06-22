package server;

import java.util.Collection;
//
public interface Searchable<T> {
	State<T> getInitialState();
	boolean isGoalState(State<T> s);
	Collection<State<T>> getAllPossibleStates(State<T> s); //
}
