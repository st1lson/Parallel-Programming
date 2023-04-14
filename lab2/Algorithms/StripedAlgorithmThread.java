package lab2.Algorithms;

import java.util.concurrent.Callable;

public final class StripedAlgorithmThread implements Callable<Integer> {

    private final int[] row;
    private final int[] column;

    public StripedAlgorithmThread(int[] row, int[] column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public Integer call() {
        var result = 0;
        for (var i = 0; i < this.row.length; i++) {
            result += this.row[i] * this.column[i];
        }

        return result;
    }
}
