package lab2.Algorithms;

import lab2.Models.Matrix;

public class FoxAlgorithmThread extends Thread {
    private final Matrix A;
    private final Matrix B;
    private final Matrix C;

    private final int stepI;
    private final int stepJ;

    public FoxAlgorithmThread(Matrix A, Matrix B, Matrix C, int stepI, int stepJ) {
        this.A = A;
        this.B = B;
        this.C = C;

        this.stepI = stepI;
        this.stepJ = stepJ;
    }

    @Override
    public void run() {
        Matrix blockRes = multiplyBlock();

        for (int i = 0; i < blockRes.getRows(); i++) {
            for (int j = 0; j < blockRes.getColumns(); j++) {

                C.getMatrix()[i + stepI][j + stepJ] += blockRes.getMatrix()[i][j];
            }
        }
    }

    private Matrix multiplyBlock() {
        Matrix blockRes = new Matrix(A.getRows(), B.getColumns());
        for (int i = 0; i < A.getRows(); i++) {
            for (int j = 0; j < B.getColumns(); j++) {
                for (int k = 0; k < A.getColumns(); k++) {
                    blockRes.getMatrix()[i][j] += A.getMatrix()[i][k] * B.getMatrix()[k][j];
                }
            }
        }
        return blockRes;
    }
}