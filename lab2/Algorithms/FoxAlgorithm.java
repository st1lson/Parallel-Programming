package lab2.Algorithms;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import lab2.Interfaces.IAlgorithm;
import lab2.Models.Matrix;
import lab2.Models.Result;

public class FoxAlgorithm implements IAlgorithm {
    private final Matrix firstMatrix;
    private final Matrix secondMatrix;

    public FoxAlgorithm(Matrix firstMatrix, Matrix secondMatrix) {
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
    }

    private int findNumberOfThreads(int threadsNumber, int rows) {
        var numberOfThreads = Math.min(threadsNumber, this.firstMatrix.getRows());
        numberOfThreads = Math.min(numberOfThreads, this.firstMatrix.getRows());
        
        var i = numberOfThreads;
        while (i > 1) {
            if (rows % i == 0) {
                break;
            }

            if (i >= numberOfThreads) {
                i++;
            } else {
                i--;
            }

            if (i > Math.sqrt(rows)) {
                i = Math.min(numberOfThreads, rows / numberOfThreads) - 1;
            }
        }

        return i >= numberOfThreads ? i : i != 0 ? rows / i : rows;
    }

    @Override
    public Result solve(int threadsNumber) {
        var startTime = System.currentTimeMillis();

        var numberOfThreads = findNumberOfThreads(threadsNumber, firstMatrix.getRows());
        var threadPool = Executors.newFixedThreadPool(numberOfThreads);

        var firstSplittedMatrixes = Matrix.splitMatrix(this.firstMatrix, threadsNumber);
        var secondSplittedMatrixes = Matrix.splitMatrix(this.secondMatrix, threadsNumber);
        
        int count = this.firstMatrix.getMatrix().length;
        int subMatrixSize = firstSplittedMatrixes[0][0].getRows();
        var resultMatrix = new Matrix[count][count];
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                resultMatrix[i][j] = new Matrix(subMatrixSize, subMatrixSize);
            }
        }

        for (int k = 0; k < count; k++) {
            var tasks = new ArrayList<Future<Matrix>>();
            for (int i = 0; i < count; i++) {
                for (int j = 0; j < count; j++) {
                    tasks.add(threadPool.submit(new FoxAlgorithmThread(firstSplittedMatrixes[i][(i + k) % count], secondSplittedMatrixes[(i + k) % count][j], resultMatrix[i][j])));
                }
            }

            for (int i = 0; i < count; i++) {
                for (int j = 0; j < count; j++) {
                    try {
                        if (resultMatrix[i][j] == null) {
                            resultMatrix[i][j] = new Matrix(subMatrixSize, subMatrixSize);
                        }

                        resultMatrix[i][j] = tasks.get(i * count + j).get();
                    } catch (Exception e) {
                    }
                }
            }
        }

        var endTime = System.currentTimeMillis();
        var result = new Result(new Matrix(resultMatrix, this.firstMatrix.getRows(), this.secondMatrix.getColumns()), endTime - startTime);

        return result;
    }

    @Override
    public Result solve() {
        throw new UnsupportedOperationException();
    }
}