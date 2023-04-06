package lab2;

import lab2.Algorithms.FoxAlgorithm;
import lab2.Algorithms.RowAlgorithm;
import lab2.Algorithms.RowImprovedAlgorithm;
import lab2.Algorithms.StrippedAlgorithm;
import lab2.Models.Matrix;

public class Program {
    public static void main(String[] args) {
        var firstMatrix = new Matrix(5, 5, 1);
        var secondMatrix = new Matrix(5, 5, 10);

        var processors = Runtime.getRuntime().availableProcessors();

        var rowAlgorithm = new RowAlgorithm(firstMatrix, secondMatrix);

        var parallelRowResult = rowAlgorithm.solve(processors);

        var rowImprovedAlgorithm = new RowImprovedAlgorithm(firstMatrix, secondMatrix);

        var parallelRowImprovedResult = rowImprovedAlgorithm.solve(processors);

        var strippedAlgorithm = new StrippedAlgorithm(firstMatrix, secondMatrix);

        var parallelStrippedResult = strippedAlgorithm.solve(processors);

        var foxAlgorithm = new FoxAlgorithm(firstMatrix, secondMatrix);

        var parallelFoxResult = foxAlgorithm.solve(processors);

        System.out.println(String.format("Parallel row algorithm: %s", parallelRowResult.getMatrix()));
        System.out.println(String.format("Parallel row improved algorithm: %s", parallelRowImprovedResult.getMatrix()));
        System.out.println(String.format("Parallel stripped algorithm: %s", parallelStrippedResult.getMatrix()));
        System.out.println(String.format("Parallel Fox algorithm: %s", parallelFoxResult.getMatrix()));
    }
}
