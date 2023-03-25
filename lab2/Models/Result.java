package lab2.Models;

public final class Result {
    private final Matrix resultMatrix;
    private long duration = 0;

    public Result(Matrix resultMatrix, long duration) {
        this.resultMatrix = resultMatrix;
        this.duration = duration;
    }

    public Matrix getMatrix() {
        return this.resultMatrix;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append(String.format("Duration (milliseconds): %o", this.duration));

        return builder.toString();
    }
}
