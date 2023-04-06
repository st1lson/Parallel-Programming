package lab2.Algorithms;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lab2.Interfaces.IAlgorithm;
import lab2.Models.Matrix;
import lab2.Models.Result;

public final class FoxAlgorithm implements IAlgorithm {
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
        
        var matrixLength = firstSplittedMatrixes.length;
        var subMatrixSize = firstSplittedMatrixes[0][0].getRows();
        var resultMatrix = initMatrix(matrixLength, subMatrixSize);

        for (var k = 0; k < matrixLength; k++) {
            var tasks = new ArrayList<Future<Matrix>>();
            for (var i = 0; i < matrixLength; i++) {
                var offset = (i + k) % matrixLength;

                for (var j = 0; j < matrixLength; j++) {
                    tasks.add(threadPool.submit(new FoxAlgorithmThread(firstSplittedMatrixes[i][offset], secondSplittedMatrixes[offset][j], resultMatrix[i][j])));
                }
            }

            for (var i = 0; i < matrixLength; i++) {
                var offset = i * matrixLength;

                for (var j = 0; j < matrixLength; j++) {
                    try {
                        resultMatrix[i][j] = tasks.get(offset + j).get();
                    } catch (Exception e) {
                    }
                }
            }
        }

        var endTime = System.currentTimeMillis();
        var result = new Result(
            new Matrix(
                resultMatrix,
                this.firstMatrix.getRows(),
                this.secondMatrix.getColumns()),
            endTime - startTime);

        return result;
    }

    @Override
    public Result solve() {
        throw new UnsupportedOperationException();
    }

    private static Matrix[][] initMatrix(int matrixLength, int subMatrixSize) {
        var matrix = new Matrix[matrixLength][matrixLength];
        for (var i = 0; i < matrixLength; i++) {
            for (var j = 0; j < matrixLength; j++) {
                matrix[i][j] = new Matrix(subMatrixSize, subMatrixSize);
            }
        }

        return matrix;
    }
}