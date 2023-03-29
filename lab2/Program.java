package lab2;

import lab2.Algorithms.FoxAlgorithm;
import lab2.Algorithms.StripedAlgorithm;
import lab2.Algorithms.StripedImprovedAlgorithm;
import lab2.Models.Matrix;

public class Program {
    public static void main(String[] args) {
        var firstMatrix = new Matrix(5, 5, 1);
        var secondMatrix = new Matrix(5, 5, 10);

        var rowAlgorithm = new StripedAlgorithm(firstMatrix, secondMatrix);

        var result = rowAlgorithm.solve(Runtime.getRuntime().availableProcessors());
        // var syncResult = rowAlgorithm.solve();

        System.out.println(result.getMatrix());
        // System.out.println(syncResult);
    }
}
