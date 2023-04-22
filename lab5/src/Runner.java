import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class Runner implements Runnable {

    private static final long SIMULATION_DURATION = 10000;

    private static final int QUEUE_LENGTH = 10;

    private static final int CONSUMERS_COUNT = 5;

    private final boolean liveInfo;

    private final int index;

    public Runner(boolean liveInfo, int index) {
        this.liveInfo = liveInfo;
        this.index = index;
    }

    @Override
    public void run() {
        var startTime = System.currentTimeMillis();

        var queue = new Queue(QUEUE_LENGTH);
        var executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        var tasks = new ArrayList<Callable<Void>>();
        tasks.add(Helper.toCallable(new Producer(queue, startTime, SIMULATION_DURATION)));

        for (int i = 0; i < CONSUMERS_COUNT; i++) {
            tasks.add(Helper.toCallable(new Consumer(queue, startTime, SIMULATION_DURATION)));
        }

        var lengthCounter = new LengthCheckerThread(queue, startTime, SIMULATION_DURATION);

        try {
            if (liveInfo) {
                lengthCounter.start();
            }

            executor.invokeAll(tasks);

            if (liveInfo) {
                lengthCounter.join();
            }

            var servedItems = queue.servedItemsCount;
            var rejectedItems = queue.rejectedItemsCount;
            var chanceOfReject = rejectedItems / (servedItems + rejectedItems);
            System.out.printf("Runner %s%nServed: %s%nRejected: %s%nReject chance: %s%n", index, servedItems, rejectedItems, chanceOfReject);

            if (liveInfo) {
                lengthCounter.showAvgLength();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }
}