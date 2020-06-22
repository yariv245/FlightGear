package server;
//
public class SearcherSolver implements Solver<Searchable,Solution> {

	Searcher<Searchable> searcher;

	public SearcherSolver(Searcher<Searchable> s) {
		this.searcher = s;
	}

	@Override
	public Solution solve(Searchable var1) {
		return this.searcher.search(var1);
	}
}
