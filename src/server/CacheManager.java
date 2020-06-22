package server;
//
public interface CacheManager<P,S> {
    boolean existSolution(P Problem);

    S loadSolution(P Problem);

    void store(P Problem, S Solutin);
}
