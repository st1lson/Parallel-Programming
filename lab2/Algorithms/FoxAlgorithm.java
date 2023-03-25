package lab2.Algorithms;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        var resultMatrix = new Matrix(firstMatrix.getRows(), secondMatrix.getColumns());

        var numberOfThreads = findNumberOfThreads(threadsNumber, firstMatrix.getRows());
        var step = firstMatrix.getRows() / numberOfThreads;

        var threadPool = Executors.newFixedThreadPool(numberOfThreads);
        var threads = new ArrayList<Future<?>>();

        var matrixOfSizesI = new int[numberOfThreads][numberOfThreads];
        var matrixOfSizesJ = new int[numberOfThreads][numberOfThreads];

        var stepI = 0;
        for (int i = 0; i < numberOfThreads; i++) {
            var stepJ = 0;
            for (int j = 0; j < numberOfThreads; j++) {
                matrixOfSizesI[i][j] = stepI;
                matrixOfSizesJ[i][j] = stepJ;
                stepJ += step;
            }
            stepI += step;
        }

        for (int l = 0; l < numberOfThreads; l++) {
            for (int i = 0; i < numberOfThreads; i++) {
                for (int j = 0; j < numberOfThreads; j++) {
                    var stepI0 = matrixOfSizesI[i][j];
                    var stepJ0 = matrixOfSizesJ[i][j];

                    var stepI1 = matrixOfSizesI[i][(i + l) % numberOfThreads];
                    var stepJ1 = matrixOfSizesJ[i][(i + l) % numberOfThreads];

                    var stepI2 = matrixOfSizesI[(i + l) % numberOfThreads][j];
                    var stepJ2 = matrixOfSizesJ[(i + l) % numberOfThreads][j];

                    var thread = new FoxAlgorithmThread(
                            copyBlock(firstMatrix, stepI1, stepJ1, step),
                            copyBlock(secondMatrix, stepI2, stepJ2, step),
                            resultMatrix,
                            stepI0,
                            stepJ0);
                    threads.add(threadPool.submit(thread));
                }
            }
        }

        for (var mapFuture : threads) {
            try {
                mapFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        threadPool.shutdown();

        var endTime = System.currentTimeMillis();
        var result = new Result(resultMatrix, endTime - startTime);

        return result;
    }

    @Override
    public Result solve() {
        throw new UnsupportedOperationException();
    }

    private Matrix copyBlock(Matrix matrix, int i, int j, int size) {
        var block = new Matrix(size, size);
        for (int k = 0; k < size; k++) {
            System.arraycopy(matrix.getMatrix()[k + i], j, block.getMatrix()[k], 0, size);
        }

        return block;
    }
}