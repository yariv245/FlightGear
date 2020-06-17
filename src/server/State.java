package server;

public class State<Param> {
	
	Param state;
	double cost;
	State<Param> cameFrom;
	double totalCost;
	
	public double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}
	public Param getState() {
		return state;
	}
	public void setState(Param state) {
		this.state = state;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public State<Param> getCameFrom() {
		return cameFrom;
	}
	public void setCameFrom(State<Param> cameFrom) {
		this.cameFrom = cameFrom;
	}
}
