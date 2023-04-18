package task2.Interfaces;

import task2.Models.Result;

public interface IAlgorithm {
    Result solve(int numberOfThreads);
    Result solve();
}
