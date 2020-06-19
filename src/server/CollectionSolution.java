package server;

import java.util.Collection;

public class CollectionSolution implements Solution {
		
	Collection result;
	
	public CollectionSolution(Collection cl) {
		this.result= cl;
	}
	public void store() {
		this.result.add(obj);
	}
	
}
