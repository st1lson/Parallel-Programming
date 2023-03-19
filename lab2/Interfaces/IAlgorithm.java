package lab2.Interfaces;

import lab2.Models.Result;

public interface IAlgorithm {
    Result solve(int numberOfThreads);
    Result solve();
}
