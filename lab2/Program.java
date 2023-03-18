package lab2;

import lab2.Algorithms.RowAlgorithm;
import lab2.Models.Matrix;

public class Program {
    public static void main(String[] args) {
        var firstMatrix = new Matrix(3, 3, 1);
        var secondMatrix = new Matrix(3, 3, 10);

        var rowAlgorithm = new RowAlgorithm(firstMatrix, secondMatrix);

        var result = rowAlgorithm.solve();
        System.out.println(result);
    }
}
