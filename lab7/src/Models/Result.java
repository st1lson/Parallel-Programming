package Models;

public final class Result {
    private final Matrix resultMatrix;
    private final long duration;

    public Result(Matrix resultMatrix, long duration) {
        this.resultMatrix = resultMatrix;
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
