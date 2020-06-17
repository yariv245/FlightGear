package server;

public class SearcherSolver<P extends Searchable<P>, Sol extends Solutionit<Sol>> implements Solver<P, Sol> {

	Searcher<P> searcher;

	public SearcherSolver(Searcher<P> s) {
		this.searcher = s;
	}

	@Override
	public Sol solve(P var1) {
		return (Sol) this.searcher.search(var1);
	}

}
