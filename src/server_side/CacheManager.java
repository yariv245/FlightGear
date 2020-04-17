package server_side;

public interface CacheManager {
    boolean existSolution(Problem var1);

    Solution loadSolution(Problem var1);

    void store(Problem var1, Solution var2);
}
