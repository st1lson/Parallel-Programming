import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Runner implements Runnable {

    private static final long SIMULATION_DURATION = 10000;

    private static final int QUEUE_LENGTH = 30;

    private static final int CONSUMERS_COUNT = 5;

    private final int index;

    public Runner(int index) {
        this.index = index;
    }

    @Override
    public void run() {
        var startTime = System.currentTimeMillis();

        var queue = new Queue(QUEUE_LENGTH);
        var executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        var tasks = new ArrayList<Callable<Object>>();
        tasks.add(Executors.callable(new Producer(queue, startTime, SIMULATION_DURATION)));

        for (int i = 0; i < CONSUMERS_COUNT; i++) {
            tasks.add(Executors.callable(new Consumer(queue, startTime, SIMULATION_DURATION)));
        }

        var lengthCounter = new LengthCheckerThread(queue, startTime, SIMULATION_DURATION);

        try {
            lengthCounter.start();

            executor.invokeAll(tasks);

            lengthCounter.join();

            var servedItems = queue.servedItemsCount;
            var rejectedItems = queue.rejectedItemsCount;
            var chanceOfReject = (double)rejectedItems / (servedItems + rejectedItems);
            System.out.printf("Runner %s%nServed: %s%nRejected: %s%nReject chance: %4$,.6f%n", index, servedItems, rejectedItems, chanceOfReject);

            System.out.printf("Average queue length: %1$,.6f%n", lengthCounter.getAverageQueueLength());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }
}
