package com.example.lab8.Services;

import com.example.lab8.Models.Matrix;
import com.example.lab8.Models.Result;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiplyService {
    private final Matrix firstMatrix;
    private final Matrix secondMatrix;

    public MultiplyService(Matrix firstMatrix, Matrix secondMatrix) {
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
    }

    private int findNumberOfSubMatrixes(int threadsNumber, int rows) {
        var numberOfSubMatrixes = Math.min(threadsNumber, this.firstMatrix.getRows());

        var i = numberOfSubMatrixes;
        while (i > 1) {
            if (rows % i == 0) {
                break;
            }

            if (i >= numberOfSubMatrixes) {
                i++;
            } else {
                i--;
            }

            if (i > Math.sqrt(rows)) {
                i = Math.min(numberOfSubMatrixes, rows / numberOfSubMatrixes) - 1;
            }
        }

        return i >= numberOfSubMatrixes
                ? i
                : i != 0 ? rows / i : rows;
    }

    public Result solve(int threadsNumber) {
        var startTime = System.currentTimeMillis();

        var numberOfSubMatrixes = findNumberOfSubMatrixes(threadsNumber, firstMatrix.getRows());
        var threadPool = Executors.newFixedThreadPool(threadsNumber);

        var firstSubMatrix = Matrix.splitMatrix(this.firstMatrix, numberOfSubMatrixes);
        var secondSubMatrix = Matrix.splitMatrix(this.secondMatrix, numberOfSubMatrixes);

        var matrixLength = firstSubMatrix.length;
        var subMatrixSize = firstSubMatrix[0][0].getRows();
        var resultMatrix = initMatrix(matrixLength, subMatrixSize);

        for (var k = 0; k < matrixLength; k++) {
            var tasks = new ArrayList<Future<Matrix>>();
            for (var i = 0; i < matrixLength; i++) {
                var offset = (i + k) % matrixLength;

                for (var j = 0; j < matrixLength; j++) {
                    tasks.add(threadPool.submit(new FoxAlgorithmThread(firstSubMatrix[i][offset], secondSubMatrix[offset][j], resultMatrix[i][j])));
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
