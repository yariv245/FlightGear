package server;

public interface Searcher<T> {
	T search(Searchable<T> P);
}
