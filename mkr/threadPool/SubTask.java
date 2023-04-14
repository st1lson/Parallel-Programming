package mkr.threadPool;

public class SubTask implements Runnable {
    private final int[] array;
    private final Calculator calculator;

    public SubTask(int[] array, Calculator calculator) {
        this.array = array;
        this.calculator = calculator;
    }

    @Override
    public void run() {
        var result = 0;
        for (int i = 0; i < this.array.length; i++) {
            result += this.array[i];
        }

        this.calculator.increment(result);
    }
}
