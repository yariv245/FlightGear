package server;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;

public interface Searchable<T> {
	State<T> getInitialState();
	boolean isGoalState(State<T> s);
	Collection<State<T>> getAllPossibleStates(State<T> s);
}
