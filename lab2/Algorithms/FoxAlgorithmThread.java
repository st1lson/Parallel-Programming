package lab2.Algorithms;

import java.util.concurrent.Callable;

import lab2.Models.Matrix;

public final class FoxAlgorithmThread implements Callable<Matrix> {
    private final Matrix firstMatrix;
    private final Matrix secondMatrix;
    private final Matrix resultMatrix;

    public FoxAlgorithmThread(Matrix firstMatrix, Matrix secondMatrix, Matrix resultMatrix) {
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
        this.resultMatrix = resultMatrix;
    }

    @Override
    public Matrix call() {
        for (int i = 0; i < this.firstMatrix.getRows(); i++) {
            for (int j = 0; j < this.secondMatrix.getColumns(); j++) {
                int result = 0;
                for (int k = 0; k < this.firstMatrix.getColumns(); k++) {
                    result += this.firstMatrix.getMatrix()[i][k] * this.secondMatrix.getMatrix()[k][j];
                }

                resultMatrix.getMatrix()[i][j] += result;
            }
        }

        return resultMatrix;
    }
}