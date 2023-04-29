import Models.Matrix;

public class Program {
    public static void main(String[] args) {
        var firstMatrix = new Matrix(50, 50);
        var secondMatrix = new Matrix(50, 50);

        BlockingMultiplier.setArgs(args);
        new BlockingMultiplier().multiply(firstMatrix, secondMatrix);
    }
}