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

        System.out.println(String.format("Row algorithm: %s", rowResult));
        System.out.println(String.format("Parallel striped algorithm: %s", parallelStripedResult));
        System.out.println(String.format("Parallel Fox algorithm: %s", parallelFoxResult));

        System.out.println(String.format("Striped correct: %s", rowResult.getMatrix().equals(parallelStripedResult.getMatrix())));
        System.out.println(String.format("Fox correct: %s", rowResult.getMatrix().equals(parallelFoxResult.getMatrix())));
    }
}
