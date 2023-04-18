package task2;

import task2.Algorithms.StripedAlgorithm;
import task2.Models.Matrix;

public class Program {
    public static void main(String[] args) {
        var firstMatrix = new Matrix(1500, 1500, 1);
        var secondMatrix = new Matrix(1500, 1500, 10);

        var processors = Runtime.getRuntime().availableProcessors();

        var stripedAlgorithm = new StripedAlgorithm(firstMatrix, secondMatrix);

        var parallelStripedResult = stripedAlgorithm.solve(processors);

        System.out.printf("Parallel striped algorithm: %s%n", parallelStripedResult);
    }
}
