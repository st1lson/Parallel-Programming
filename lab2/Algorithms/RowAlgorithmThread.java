package lab2.Algorithms;

import lab2.Models.Matrix;

public final class RowAlgorithmThread implements Runnable {

    private final int[] row;
    private final int[] column;

    private final int i;
    private final int j;

    private final Matrix resultMatrix;

    public RowAlgorithmThread(int[] row, int[] column, int i, int j, Matrix resultMatrix) {
        this.row = row;
        this.column = column;

        this.i = i;
        this.j = j;

        this.resultMatrix = resultMatrix;
    }

    @Override
    public void run() {
        var result = 0;
        for (var i = 0; i < this.row.length; i++) {
            result += this.row[i] * this.column[i];
        }
        
        this.resultMatrix.setItem(this.i, this.j, result);
    }
}
