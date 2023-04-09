package lab2;

import lab2.Algorithms.FoxAlgorithm;
import lab2.Algorithms.RowAlgorithm;
import lab2.Algorithms.StripedAlgorithm;
import lab2.Models.Matrix;

public class Program {
    public static void main(String[] args) {
        var firstMatrix = new Matrix(2500, 2500, 1);
        var secondMatrix = new Matrix(2500, 2500, 10);

        var processors = Runtime.getRuntime().availableProcessors();

        var rowAlgorithm = new RowAlgorithm(firstMatrix, secondMatrix);

        var parallelRowResult = rowAlgorithm.solve(processors);

        var stripedAlgorithm = new StripedAlgorithm(firstMatrix, secondMatrix);

        var parallelStripedResult = stripedAlgorithm.solve(processors);

        var foxAlgorithm = new FoxAlgorithm(firstMatrix, secondMatrix);

        var parallelFoxResult = foxAlgorithm.solve(processors);

        System.out.println(String.format("Parallel row algorithm: %s", parallelRowResult));
        System.out.println(String.format("Parallel striped algorithm: %s", parallelStripedResult));
        System.out.println(String.format("Parallel Fox algorithm: %s", parallelFoxResult));
    }
}
