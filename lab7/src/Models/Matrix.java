package Models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

public final class Matrix {

    public static final int INT32_BYTE_SIZE = 4;

    private final int[][] items;
    private final int rows;
    private final int columns;
    private int randomSeed;

    public Matrix(int[][] items) {
        this.items = items;
        this.rows = items.length;
        this.columns = items[0].length;
    }

    public Matrix(int rows, int columns, int randomSeed) {
        this.rows = rows;
        this.columns = columns;
        this.randomSeed = randomSeed;
        this.items = generateMatrix(rows, columns);
    }

    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.items = new int[rows][columns];
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[0].length; j++) {
                items[i][j] = 1;
            }
        }
    }

    public Matrix(byte[] bytes, int rows, int cols) {
        var buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.nativeOrder());
        var array = new int[rows][cols];
        for (var i = 0; i < rows; i++) {
            for (var j = 0; j < cols; j++) {
                array[i][j] = buffer.getInt();
            }
        }

        this.items = array;
        this.rows = array.length;
        this.columns = array[0].length;
    }

    private int[][] generateMatrix(int rows, int columns) {
        var matrix = new int[rows][columns];

        var random = new Random(this.randomSeed);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = random.nextInt(10);
            }
        }

        return matrix;
    }

    public int getRows() {
        return this.rows;
    }

    public int getColumns() {
        return this.columns;
    }

    public int[][] getMatrix() {
        return this.items;
    }

    public byte[] toByteArray() {
        var buffer = ByteBuffer.allocate(rows * columns * INT32_BYTE_SIZE);
        buffer.order(ByteOrder.nativeOrder());
        var intBuffer = buffer.asIntBuffer();
        for (var ints : items) {
            intBuffer.put(ints);
        }

        return buffer.array();
    }

    public Matrix multiply(Matrix matrix) {
        var result = new int[rows][matrix.getColumns()];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < matrix.getColumns(); j++) {
                var sum = 0;
                for (int k = 0; k < matrix.getColumns(); k++) {
                    sum += items[i][k] * matrix.items[k][j];
                }

                result[i][j] = sum;
            }
        }

        return new Matrix(result);
    }

    @Override
    public Matrix clone() {
        int[][] copy = new int[this.rows][this.columns];

        for (int i = 0; i < this.rows; i++) {
            System.arraycopy(items[i], 0, copy[i], 0, this.columns);
        }

        return new Matrix(copy);
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                builder.append(String.format("%4d", items[i][j]));
            }

            builder.append('\n');
        }

        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        var matrix = (Matrix) obj;
        if (matrix == null) return false;

        var anotherItems = matrix.getMatrix();
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[0].length; j++) {
                if (items[i][j] != anotherItems[i][j])
                    return false;
            }
        }

        return true;
    }
}
