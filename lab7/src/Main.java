import Models.Matrix;

public class Main {
    public static void main(String[] args) {
        var firstMatrix = new Matrix(20, 20, 1);
        var secondMatrix = new Matrix(20, 20, 10);

        var result = new Multiplier(firstMatrix, secondMatrix, args).multiply();

        if (result == null) return;

        System.out.println(result.getMatrix());
    }
}