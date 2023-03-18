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
