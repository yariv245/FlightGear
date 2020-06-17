package server;

import java.util.List;

public interface Searchable<Param> {

	public State<Param> getInitialState();

	boolean isGoalState(State<Param> state);

	List<State<Param>> getAllPossibleStates(State<Param> state);

}
