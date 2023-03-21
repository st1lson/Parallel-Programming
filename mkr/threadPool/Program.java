package mkr.threadPool;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Program {
    private static final int ARRAY_SIZE = 50000;

    private static final int SUBTASK_SIZE = 500;

    private static final int NUMBER_OF_THREADS = 4;

    public static void main(String[] args) {
        var threadPool = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        var locker = new ReentrantLock();
        var calculator = new Calculator(locker);
        var array = createArray();
        var subtaskCount = 0;
        for (int i = 0; i < array.length; i++) {
            if (subtaskCount < SUBTASK_SIZE && i != array.length - 1) {
                subtaskCount++;
                continue;
            }

            threadPool.execute(new SubTask(Arrays.copyOfRange(array, i - subtaskCount, i), calculator));

            subtaskCount = 0;;
        }

        try {
            threadPool.shutdown();
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("Result: %d", calculator.getResult());
    }

    private static int[] createArray() {
        var array = new int[ARRAY_SIZE];
        var random = new Random();
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = random.nextInt(100);
        }

        return array;
    }
}
