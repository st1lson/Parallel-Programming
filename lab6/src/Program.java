import Models.Matrix;

public class Program {
    public static void main(String[] args) {
        var firstMatrix = new Matrix(2500, 2500, 1);
        var secondMatrix = new Matrix(2500, 2500, 10);

        var blockingResult = new BlockingMultiplier(firstMatrix, secondMatrix, args).multiply();

        if (blockingResult == null) return;

        System.out.println(blockingResult);

//        var result = new NonBlockingMultiplier(firstMatrix, secondMatrix, args).multiply();
//
//        if (result == null) return;
//
//        System.out.println(result);
    }
}