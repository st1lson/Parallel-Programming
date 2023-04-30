import Models.Matrix;

public class Program {
    public static void main(String[] args) {
        var firstMatrix = new Matrix(2000, 2000, 1);
        var secondMatrix = new Matrix(2000, 2000, 10);

        BlockingMultiplier.setArgs(args);
        var result = new BlockingMultiplier().multiply(firstMatrix, secondMatrix);

        System.out.println(result);
    }
}