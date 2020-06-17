package server;

public class SearcherSolver<P, S> implements Solver<P, S> {

	Searcher<P> searcher;

	public SearcherSolver(Searcher<P> s) {
		this.searcher = s;
	}

	@Override
	public S solve(P var1) {
		return this.searcher.search(var1);
	}

}
