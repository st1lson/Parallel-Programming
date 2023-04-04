package lab2.Models;

import java.util.Random;

public final class Matrix {
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
    }

    public Matrix(Matrix[][] matrix, int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.items = new int[rows][columns];

        var totalItems = matrix.length;
        var subMatrixSize = matrix[0][0].getRows();

        for (var i = 0; i < totalItems; i++) {
            for (var j = 0; j < totalItems; j++) {
                var currentMatrix = matrix[i][j].getMatrix();

                for (var k = 0; k < subMatrixSize; k++) {
                    for (var p = 0; p < subMatrixSize; p++) {
                        if(i * subMatrixSize + k >= rows || j * subMatrixSize + p >= columns){
                            continue;
                        }
                        
                        this.items[i * subMatrixSize + k][j * subMatrixSize + p] = currentMatrix[k][p];
                    }
                }
            }
        }
    }

    private final int[][] generateMatrix(int rows, int columns) {
        var matrix = new int[rows][columns];
        
        var random = new Random(this.randomSeed);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = random.nextInt(10);
            }
        }

        return matrix;
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

    public static final Matrix[][] splitMatrix(Matrix matrix, int step) {
        var result = new Matrix[step][step];
        int matrixSize = (matrix.getColumns() + step - 1) / step;
        for (int i = 0; i < step; i++) {
            for (int j = 0; j < step; j++) {
                result[i][j] = new Matrix(matrixSize, matrixSize);
                int[][] subMatrix = result[i][j].getMatrix();
                for (int i1 = 0; i1 < matrixSize; i1++) {
                    for (int j1 = 0; j1 < subMatrix.length; j1++) {
                        if(i * matrixSize + i1 >= matrix.getRows() || j * matrixSize + j1 >= matrix.getColumns()){
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

    public static final Matrix copyBlock(Matrix matrix, int i, int j, int size) {
        var block = new Matrix(size, size);
        for (var k = 0; k < size; k++) {
            System.arraycopy(matrix.getMatrix()[k + i], j, block.getMatrix()[k], 0, size);
        }

        return block;
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
}
