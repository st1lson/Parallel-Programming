package lab2;

import lab2.Algorithms.RowAlgorithm;
import lab2.Models.Matrix;

public class Program {
    public static void main(String[] args) {
        var firstMatrix = new Matrix(5, 5, 1);
        var secondMatrix = new Matrix(5, 5, 10);

        var rowAlgorithm = new RowAlgorithm(firstMatrix, secondMatrix);

        var result = rowAlgorithm.solve(20);
        var syncResult = rowAlgorithm.solve();

        // System.out.println(result.getMatrix());
        // System.out.println(syncResult.getMatrix());
        System.out.println(result);
        System.out.println(syncResult);
    }
}
