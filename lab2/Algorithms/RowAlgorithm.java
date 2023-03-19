package lab2.Algorithms;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import lab2.Interfaces.IAlgorithm;
import lab2.Models.Matrix;
import lab2.Models.Result;
import lab2.Models.SubTask;

public final class RowAlgorithm implements IAlgorithm {   
    private final Matrix firstMatrix;
    private final Matrix secondMatrix;

    private static final int ITEMS_PER_THREAD = 75;

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
        var resultMatrix = new Matrix(rowsCount, columnsCount);

        try {
            var threadPool = Executors.newFixedThreadPool(numberOfThreads);
            var counter = 0;
            var tasks = new CopyOnWriteArrayList<SubTask>();
            for (int i = 0; i < rowsCount; i++) {
                for (int j = 0; j < columnsCount; j++) {
                    if (counter == ITEMS_PER_THREAD) {
                        threadPool.execute(new RowAlgorithmThread(tasks, resultMatrix));
                        tasks.clear();
                        counter = 0;
                    }

                    tasks.add(new SubTask(rows[i], columns[j], i, j));
                    counter++;
                }
            }
            if (counter > 0) {
                threadPool.execute(new RowAlgorithmThread(tasks, resultMatrix));
            }

            threadPool.shutdown();
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        var endTime = System.nanoTime();
        var result = new Result(resultMatrix, endTime - startTime);

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
                resultMatrix[i][j] = multiplyArrays(rows[i], columns[j]);
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
