package lab2.Models;

public final class Result {
    private final Matrix resultMatrix;
    private float duration = 0f;

    public Result(Matrix resultMatrix) {
        this.resultMatrix = resultMatrix;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append(this.resultMatrix);

        return builder.toString();
    }
}
