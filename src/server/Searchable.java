package server;

<<<<<<< HEAD
import java.util.List;

public interface Searchable<Param> {

	public State<Param> getInitialState();

	boolean isGoalState(State<Param> state);

	List<State<Param>> getAllPossibleStates(State<Param> state);
=======
import java.util.Collection;

public interface Searchable<T> {
	State<T> getInitialState();
	boolean isGoalState(State<T> s);
	Collection<State<T>> getAllPossibleStates(State<T> s);
>>>>>>> refs/remotes/origin/Yariv_Changes
}
