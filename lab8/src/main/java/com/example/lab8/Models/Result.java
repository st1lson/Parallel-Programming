package com.example.lab8.Models;

public final class Result {
    private final Matrix resultMatrix;
    private long duration = 0;

    public Result(Matrix resultMatrix, long duration) {
        this.resultMatrix = resultMatrix;
        this.duration = duration;
    }

    public Result(int[][] matrix, long duration) {
        this.resultMatrix = new Matrix(matrix);
        this.duration = duration;
    }

    public Matrix getMatrix() {
        return this.resultMatrix;
    }

    @Override
    public String toString() {
        return String.format("Duration (milliseconds): %s", this.duration);
    }
}
