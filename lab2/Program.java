package lab2;

import lab2.Algorithms.FoxAlgorithm;
import lab2.Algorithms.RowAlgorithm;
import lab2.Algorithms.RowImprovedAlgorithm;
import lab2.Models.Matrix;

public class Program {
    public static void main(String[] args) {
        var firstMatrix = new Matrix(5, 5, 1);
        var secondMatrix = new Matrix(5, 5, 10);

        var strippedAlgorithm = new RowAlgorithm(firstMatrix, secondMatrix);

        var processors = Runtime.getRuntime().availableProcessors();

        var parallelStrippedResult = strippedAlgorithm.solve(processors);
        var syncStrippedResult = strippedAlgorithm.solve();

        var strippedImprovedAlgorithm = new RowImprovedAlgorithm(firstMatrix, secondMatrix);

        var strippedImprovedResult = strippedImprovedAlgorithm.solve(processors);

        var foxAlgorithm = new FoxAlgorithm(firstMatrix, secondMatrix);

        var parallelFoxResult = foxAlgorithm.solve(processors);

        System.out.println(String.format("Parallel stripped algorithm: %s", parallelStrippedResult));
        System.out.println(String.format("Sync stripped algorithm: %s", syncStrippedResult));
        System.out.println(String.format("Parallel stripped improved algorithm: %s", strippedImprovedResult));
        System.out.println(String.format("Parallel Fox algorithm: %s", parallelFoxResult));
    }
}
