package lab2.Algorithms;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;

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
        var startTime = System.nanoTime();
        var rows = this.firstMatrix.clone().getMatrix();
        var columns = Matrix.transpose(this.secondMatrix.clone()).getMatrix();
        var rowsCount = this.firstMatrix.getRows();
        var columnsCount = this.secondMatrix.getColumns();
        var resultMatrix = new int[rowsCount][columnsCount];

        var executor = Executors.newFixedThreadPool(numberOfThreads);
        try {
            for (int i = 0; i < rowsCount; i++) {
                var tasks = new ArrayList<Future<Integer>>();
                for (int j = 0; j < columnsCount; j++) {
                    var col = (i + j) % columnsCount;
                    tasks.add(executor.submit(new RowAlgorithmThread(rows[i], columns[col])));
                }

                for (int j = 0; j < columnsCount; j++) {
                    var col = (i + j) % columnsCount;
                    resultMatrix[i][col] = tasks.get(j).get().intValue();
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        executor.shutdown();

        var endTime = System.nanoTime();
        var result = new Result(new Matrix(resultMatrix), endTime - startTime);

        return result;
    }

    @Override
    public Result solve() {
        var startTime = System.nanoTime();
        var rows = this.firstMatrix.clone().getMatrix();
        var columns = Matrix.transpose(this.secondMatrix.clone()).getMatrix();
        var rowsCount = this.firstMatrix.getRows();
        var columnsCount = this.secondMatrix.getColumns();
        var resultMatrix = new int[rowsCount][columnsCount];

        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                var col = (i + j) % columnsCount;
                resultMatrix[i][col] = multiplyArrays(rows[i], columns[col]);
            }
        }

        var endTime = System.nanoTime();
        var result = new Result(new Matrix(resultMatrix), endTime - startTime);

        return result;
    }

    private static int multiplyArrays(int[] row, int[] column) {
        var result = 0;
        
        for (int i = 0; i < row.length; i++) {
            result += row[i] * column[i];
        }

        return result;
    }
}
