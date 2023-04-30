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

    public static Matrix transpose(Matrix matrixToTranspose) {
        var rows = matrixToTranspose.getRows();
        var columns = matrixToTranspose.getColumns();
        var matrix = matrixToTranspose.getMatrix();

        var transposedMatrix = new int[columns][rows];

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                transposedMatrix[i][j] = matrix[j][i];
            }
        }

        return new Matrix(transposedMatrix);
    }

    public static Matrix[][] splitMatrix(Matrix matrix, int step) {
        var result = new Matrix[step][step];
        int matrixSize = (matrix.getColumns() + step - 1) / step;
        for (int i = 0; i < step; i++) {
            for (int j = 0; j < step; j++) {
                result[i][j] = new Matrix(matrixSize, matrixSize);
                int[][] subMatrix = result[i][j].getMatrix();
                for (int i1 = 0; i1 < matrixSize; i1++) {
                    for (int j1 = 0; j1 < subMatrix.length; j1++) {
                        if (i * matrixSize + i1 >= matrix.getRows() || j * matrixSize + j1 >= matrix.getColumns()) {
                            subMatrix[i1][j1] = 0;
                            continue;
                        }
                        subMatrix[i1][j1] = matrix.getMatrix()[i * matrixSize + i1][j * matrixSize + j1];
                    }
                }
            }
        }

        return result;
    }

    public static Matrix copyBlock(Matrix matrix, int i, int j, int size) {
        var block = new Matrix(size, size);
        for (var k = 0; k < size; k++) {
            System.arraycopy(matrix.getMatrix()[k + i], j, block.getMatrix()[k], 0, size);
        }

        return block;
    }

    public void partialUpdate(Matrix subMatrix, int indexIStart, int indexIFinish) {
        int rowIndex = 0;
        for (int i = indexIStart; i < indexIFinish; i++) {
            if (columns >= 0) {
                System.arraycopy(subMatrix.items[rowIndex], 0, items[i], 0, columns);
            }

            rowIndex++;
        }
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

    public int[] getRow(int row) {
        return items[row];
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

    public void setItem(int i, int j, int item) {
        this.items[i][j] = item;
    }

    public void addItem(int i, int j, int value) {
        this.items[i][j] += value;
    }

    public Matrix getSubMatrix(int rowStartIndex, int rowFinishIndex, int columnsNumber) {
        var result = new int[rowFinishIndex - rowStartIndex + 1][columnsNumber];
        var resultIndex = 0;
        for (var i = rowStartIndex; i < rowFinishIndex; i++) {
            var temp = new int[columnsNumber];
            System.arraycopy(items[i], 0, temp, 0, columnsNumber);

            result[resultIndex] = temp;
            resultIndex++;
        }

        return new Matrix(result);
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
