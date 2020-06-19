package server;

public class State<T> {
	private T state; // the state represented by a string
	private double cost; // cost to reach this state
	private State<T> cameFrom; // the state we came from to this state
	double totalCost;
	
	public double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public State(T state) { // CTOR
		this.state = state;
	}

	public boolean equals(State<T> s) { // it’s easier to simply overload
		return state.equals(s.state); // ..
	}

	public T getState() {
		return state;
	}

	public void setState(T state) {
		this.state = state;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public State<T> getCameFrom() {
		return cameFrom;
	}

	public void setCameFrom(State<T> cameFrom) {
		this.cameFrom = cameFrom;
	}
}