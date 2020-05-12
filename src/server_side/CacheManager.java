package server_side;

public interface CacheManager {
    boolean existSolution(Object P);

    Object loadSolution(Object P);

    void store(Object P, Object S);
}
