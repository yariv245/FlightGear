package server;

public class SearcherSolver<P, S> implements Solver<P, S> {

	Searcher<P,S> searcher;

	public SearcherSolver(Searcher<P,S> s) {
		this.searcher = s;
	}

	@Override
	public S solve(P var1) {
		return this.searcher.search(var1);
	}

}
