package server;

<<<<<<< HEAD
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
=======
import java.io.Serializable;
import java.util.Arrays;

public class State<T> implements Serializable  {

	private static final long serialVersionUID = -6724097601673807555L;
	private T m_state; // the state represented by a string
	private double cost; // cost to reach this state
	private State<T> cameFrom; // the state we came from to this state
	double totalCost;
	int row;
	int col;

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}
	public State() { // CTOR
		this.row = 0;
		this.col = 0;
		this.totalCost = 0;
		this.cost = 0;
		this.m_state = null;
	}

	public State(T state, int row, int col, int cost) { // CTOR
		this.m_state = state;
		this.row = row;
		this.col = col;
		this.totalCost = 0;
		this.cost = cost;
	}

	public boolean equals(State<T> goal) { // it’s easier to simply overload
		return /*m_state.equals(goal.m_state) &&*/ row==goal.getRow() && col == goal.getCol(); // ..
	}

	public State<T> getState() {
		return this;
	}

	public void setState(T state) {
		this.m_state = state;
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

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	

}
>>>>>>> refs/remotes/origin/Yariv_Changes
