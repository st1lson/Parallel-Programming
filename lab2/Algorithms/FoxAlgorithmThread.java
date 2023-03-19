package lab2.Algorithms;

import lab2.Models.Matrix;

public class FoxAlgorithmThread extends Thread {
    private final Matrix firstMatrix;
    private final Matrix secondMatrix;
    private final Matrix resultMatrix;

    private final int stepI;
    private final int stepJ;

    public FoxAlgorithmThread(Matrix firstMatrix, Matrix secondMatrix, Matrix resultMatrix, int stepI, int stepJ) {
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
        this.resultMatrix = resultMatrix;

        this.stepI = stepI;
        this.stepJ = stepJ;
    }

    @Override
    public void run() {
        Matrix blockRes = multiplyBlock();

        for (int i = 0; i < blockRes.getRows(); i++) {
            for (int j = 0; j < blockRes.getColumns(); j++) {

                resultMatrix.getMatrix()[i + stepI][j + stepJ] += blockRes.getMatrix()[i][j];
            }
        }
    }

    private Matrix multiplyBlock() {
        Matrix blockRes = new Matrix(firstMatrix.getRows(), secondMatrix.getColumns());
        for (int i = 0; i < firstMatrix.getRows(); i++) {
            for (int j = 0; j < secondMatrix.getColumns(); j++) {
                for (int k = 0; k < firstMatrix.getColumns(); k++) {
                    blockRes.getMatrix()[i][j] += firstMatrix.getMatrix()[i][k] * secondMatrix.getMatrix()[k][j];
                }
            }
        }
        return blockRes;
    }
}