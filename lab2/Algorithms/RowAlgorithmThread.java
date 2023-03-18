package lab2.Algorithms;

import java.util.concurrent.Callable;

public class RowAlgorithmThread implements Callable<Integer> {
    private final int[] row;
    private final int[] column;

    public RowAlgorithmThread(int[] row, int[] column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public Integer call() throws Exception {
        var result = 0;
        
        for (int i = 0; i < this.row.length; i++) {
            result += this.row[i] * this.column[i];
        }

        return result;
    }
}
