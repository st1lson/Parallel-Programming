package lab2.Models;

import java.util.concurrent.TimeUnit;

public final class Result {
    private final Matrix resultMatrix;
    private long duration = 0;

    public Result(Matrix resultMatrix, long duration) {
        this.resultMatrix = resultMatrix;
        this.duration = TimeUnit.NANOSECONDS.toMillis(duration);
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        //builder.append(this.resultMatrix);
        builder.append(String.format("Duration (milliseconds): %o", this.duration));

        return builder.toString();
    }
}
