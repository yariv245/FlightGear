package server;

import java.util.Collection;

public class Solution<T> implements Solutionit<T> {
	
	Collection<T> database;
	
	public Solution(Collection<T> database) {
		this.database = database;
	}
	
	@Override
	public void store(T var) {
		this.database.add(var);
	}
	
}
