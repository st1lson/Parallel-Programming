package lab2.Algorithms;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import lab2.Interfaces.IAlgorithm;
import lab2.Models.Matrix;
import lab2.Models.Result;

public final class RowAlgorithm implements IAlgorithm {

    private final Matrix firstMatrix;
    private final Matrix secondMatrix;

    public RowAlgorithm(Matrix firstMatrix, Matrix secondMatrix) {
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
    }

    @Override
    public Result solve(int numberOfThreads) {
        var startTime = System.currentTimeMillis();
        var rows = this.firstMatrix.clone().getMatrix();
        var columns = Matrix.transpose(this.secondMatrix.clone()).getMatrix();
        var rowsCount = this.firstMatrix.getRows();
        var columnsCount = this.secondMatrix.getColumns();
        var resultMatrix = new Matrix(rowsCount, columnsCount);

        try {
            var threadPool = Executors.newFixedThreadPool(numberOfThreads);
            for (int i = 0; i < rowsCount; i++) {
                for (int j = 0; j < columnsCount; j++) {
                    threadPool.execute(new RowAlgorithmThread(rows[i], columns[j], i, j, resultMatrix));
                }
            }

            threadPool.shutdown();
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        var endTime = System.currentTimeMillis();
        var result = new Result(resultMatrix, endTime - startTime);

        return result;
    }

    @Override
    public Result solve() {
        var startTime = System.currentTimeMillis();
        var rowsCount = this.firstMatrix.getRows();
        var columnsCount = this.secondMatrix.getColumns();
        var resultMatrix = new int[rowsCount][columnsCount];

        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                for (int k = 0; k < rowsCount; k++) {
                    resultMatrix[i][j] += this.firstMatrix.getMatrix()[i][k] * this.secondMatrix.getMatrix()[k][j];
                }
            }
        }

        var endTime = System.currentTimeMillis();
        var result = new Result(new Matrix(resultMatrix), endTime - startTime);

        return result;
    }
}
