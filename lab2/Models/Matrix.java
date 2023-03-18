package lab2.Models;

import java.util.Arrays;
import java.util.Random;

public final class Matrix {
    private final int[][] items;
    private final int rows;
    private final int columns;

    public Matrix(int[][] items) {
        this.items = items;
        this.rows = items.length;
        this.columns = items[0].length;
    }

    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.items = GenerateMatrix(rows, columns);
    }

    private static int[][] GenerateMatrix(int rows, int columns) {
        var matrix = new int[rows][columns];
        
        var random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = random.nextInt(100);
            }
        }

        return matrix;
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
