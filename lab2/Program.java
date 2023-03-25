package lab2;

import lab2.Algorithms.FoxAlgorithm;
import lab2.Algorithms.RowAlgorithm;
import lab2.Algorithms.RowImprovedAlgorithm;
import lab2.Models.Matrix;

public class Program {
    public static void main(String[] args) {
        var firstMatrix = new Matrix(2500, 2500, 1);
        var secondMatrix = new Matrix(2500, 2500, 10);

        var rowAlgorithm = new RowAlgorithm(firstMatrix, secondMatrix);

        var result = rowAlgorithm.solve(Runtime.getRuntime().availableProcessors());
        var syncResult = rowAlgorithm.solve();

        System.out.println(result);
        System.out.println(syncResult);
    }
}
