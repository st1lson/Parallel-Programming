package lab2;

import lab2.Algorithms.FoxAlgorithm;
import lab2.Algorithms.RowAlgorithm;
import lab2.Models.Matrix;

public class Program {
    public static void main(String[] args) {
        var firstMatrix = new Matrix(3, 3, 1);
        var secondMatrix = new Matrix(3, 3, 10);
        System.out.println(firstMatrix);
        System.out.println(secondMatrix);
        var rowAlgorithm = new FoxAlgorithm(firstMatrix, secondMatrix);

        var result = rowAlgorithm.solve(20);
        // var syncResult = rowAlgorithm.solve();

        System.out.println(result.getMatrix());
        // System.out.println(syncResult);
    }
}
