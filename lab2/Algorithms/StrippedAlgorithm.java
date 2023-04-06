package lab2.Algorithms;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lab2.Interfaces.IAlgorithm;
import lab2.Models.Matrix;
import lab2.Models.Result;

public final class StrippedAlgorithm implements IAlgorithm {

    private final Matrix firstMatrix;
    private final Matrix secondMatrix;

    public StrippedAlgorithm(Matrix firstMatrix, Matrix secondMatrix) {
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
    }

    @Override
    public Result solve(int numberOfThreads) {
        var startTime = System.currentTimeMillis();

        var rows = this.firstMatrix.clone();
        var columns = Matrix.transpose(this.secondMatrix.clone());

        var executor = Executors.newFixedThreadPool(numberOfThreads);

        var tasks = new ArrayList<Future<Integer>>();

        var numberOfRows = this.firstMatrix.getRows();
        var numberOfColumns = this.secondMatrix.getColumns();

        var resultMatrix = new int[numberOfColumns][numberOfRows];

        for (var i = 0; i < numberOfRows; i++) {
            var iterationTasks = new ArrayList<Callable<Integer>>();
            for (var j = 0; j < numberOfColumns; j++) {
                iterationTasks.add(new StrippedAlgorithmThread(rows.getMatrix()[i], columns.getMatrix()[j]));
            }

            try {
                tasks.addAll(executor.invokeAll(iterationTasks));
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        executor.shutdown();

        for (int i = 0; i < numberOfRows; i++) {
            var rowOffset = i * numberOfColumns;

            for (int j = 0; j < numberOfColumns; j++) {
                try {
                    resultMatrix[i][j] = tasks.get(rowOffset + j).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        var endTime = System.currentTimeMillis();
        var result = new Result(resultMatrix, endTime - startTime);

        return result;
    }

    @Override
    public Result solve() {
        throw new UnsupportedOperationException("Unimplemented method 'solve'");
    }
}
