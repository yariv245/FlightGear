package server;

public interface Searcher<T> {
	Solution<?> search(Searchable<T> P);
}
