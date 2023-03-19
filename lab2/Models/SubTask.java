package lab2.Models;

public class SubTask {
    private final int i;
    private final int j;
    private final int[] row;
    private final int[] column;

    public SubTask(int[] row, int[] column, int i, int j) {
        this.row = row;
        this.column = column;
        this.i = i;
        this.j = j;
    }

    public int[] getRow() {
        return row;
    }

    public int[] getColumn() {
        return column;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }
}