package server;

<<<<<<< HEAD
public interface Solution<S> {
	public void store(S var1);
=======
import java.io.Serializable;
import java.util.Stack;

public interface Solution<T> extends Serializable  {
	public void store(T element);
	public Stack<T> getStates(); 
>>>>>>> refs/remotes/origin/Yariv_Changes
}
