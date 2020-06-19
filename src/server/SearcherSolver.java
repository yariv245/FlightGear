package server;

<<<<<<< HEAD
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
=======
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
>>>>>>> refs/remotes/origin/Yariv_Changes
