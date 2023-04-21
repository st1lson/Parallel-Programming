package lab2;

import lab2.Algorithms.FoxAlgorithm;
import lab2.Algorithms.RowAlgorithm;
import lab2.Algorithms.StripedAlgorithm;
import lab2.Models.Matrix;

public class Program {
    public static void main(String[] args) {
        var firstMatrix = new Matrix(1500, 1500, 1);
        var secondMatrix = new Matrix(1500, 1500, 10);

        var processors = Runtime.getRuntime().availableProcessors();

        var rowAlgorithm = new RowAlgorithm(firstMatrix, secondMatrix);

        var rowResult = rowAlgorithm.solve();

        var stripedAlgorithm = new StripedAlgorithm(firstMatrix, secondMatrix);

        var parallelStripedResult = stripedAlgorithm.solve(processors);

        var foxAlgorithm = new FoxAlgorithm(firstMatrix, secondMatrix);

        var parallelFoxResult = foxAlgorithm.solve(processors);

        System.out.printf("Row algorithm: %s%n", rowResult);
        System.out.printf("Parallel striped algorithm: %s%n", parallelStripedResult);
        System.out.printf("Parallel Fox algorithm: %s%n", parallelFoxResult);

        System.out.printf("Striped correct: %s%n", rowResult.getMatrix().equals(parallelStripedResult.getMatrix()));
        System.out.printf("Fox correct: %s%n", rowResult.getMatrix().equals(parallelFoxResult.getMatrix()));
    }
}
