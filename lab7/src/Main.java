import Models.Matrix;

public class Main {
    public static void main(String[] args) {
        var firstMatrix = new Matrix(2000, 2000, 1);
        var secondMatrix = new Matrix(2000, 2000, 10);

        var result = new Multiplier(firstMatrix, secondMatrix, args).multiply();

        if (result == null) return;

        System.out.println(result);
    }
}