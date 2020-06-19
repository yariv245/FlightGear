package server;

public interface Searcher<Problem> {
	Solution<Problem> search(Searchable<Problem> problem);

}
