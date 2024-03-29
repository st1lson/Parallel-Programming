package com.example.lab8.Services;

import com.example.lab8.Models.Matrix;

import java.util.concurrent.Callable;

public final class FoxAlgorithmThread implements Callable<Matrix> {
    private final Matrix firstMatrix;
    private final Matrix secondMatrix;
    private final Matrix resultMatrix;

    public FoxAlgorithmThread(Matrix firstMatrix, Matrix secondMatrix, Matrix resultMatrix) {
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
        this.resultMatrix = resultMatrix;
    }

    public Matrix call() {
        for (var i = 0; i < this.firstMatrix.getRows(); i++) {
            for (var j = 0; j < this.secondMatrix.getColumns(); j++) {
                var result = 0;
                for (var k = 0; k < this.firstMatrix.getColumns(); k++) {
                    result += this.firstMatrix.getMatrix()[i][k] * this.secondMatrix.getMatrix()[k][j];
                }

                resultMatrix.getMatrix()[i][j] += result;
            }
        }

        return resultMatrix;
    }
}