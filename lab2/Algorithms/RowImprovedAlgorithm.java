package lab2.Algorithms;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import lab2.Interfaces.IAlgorithm;
import lab2.Models.Matrix;
import lab2.Models.Result;
import lab2.Models.SubTask;

public final class RowImprovedAlgorithm implements IAlgorithm {   
    private final Matrix firstMatrix;
    private final Matrix secondMatrix;

    private static final int ITEMS_PER_THREAD = 75;

    public RowImprovedAlgorithm(Matrix firstMatrix, Matrix secondMatrix) {
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
            var counter = 0;
            var tasks = new CopyOnWriteArrayList<SubTask>();
            for (var i = 0; i < rowsCount; i++) {
                for (var j = 0; j < columnsCount; j++) {
                    if (counter == ITEMS_PER_THREAD) {
                        threadPool.execute(new RowImprovedAlgorithmThread(tasks, resultMatrix));
                        tasks.clear();
                        counter = 0;
                    }

                    tasks.add(new SubTask(rows[i], columns[j], i, j));
                    counter++;
                }
            }
            if (counter > 0) {
                threadPool.execute(new RowImprovedAlgorithmThread(tasks, resultMatrix));
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
        throw new UnsupportedOperationException();
    }
}
