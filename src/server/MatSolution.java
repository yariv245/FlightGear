package server;

import java.util.Collection;

public class MatSolution<Sol> implements Solution<Sol>{
	
	Collection<Sol> database;
	
	public MatSolution(Collection<Sol> database) {
		this.database = database;
	}
	
	@Override
	public void store(Sol var1) {
		this.database.add(var1);
	}

}
