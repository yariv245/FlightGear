
package server;

import java.io.Serializable;
import java.util.Stack;
//
public interface Solution<T> extends Serializable  {
	public void store(T element);
	public Stack<T> getStates(); 
}
