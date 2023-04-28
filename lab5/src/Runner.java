import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public final class Runner implements Callable<SimulationResult> {

    public static final long SIMULATION_DURATION = 10000;

    private static final int QUEUE_LENGTH = 30;

    private static final int CONSUMERS_COUNT = 5;

    private final String name;

    public Runner(String name) {
        this.name = name;
    }

    @Override
    public SimulationResult call() {
        var startTime = System.currentTimeMillis();

        var queue = new Queue(QUEUE_LENGTH);
        var threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        var tasks = new ArrayList<Callable<Object>>();
        tasks.add(Executors.callable(new Producer(queue, startTime)));

        for (int i = 0; i < CONSUMERS_COUNT; i++) {
            tasks.add(Executors.callable(new Consumer(queue, startTime)));
        }

        var logger = new Logger(queue, startTime, name);
        var loggerThread = new Thread(logger);

        try {
            loggerThread.start();

            threadPool.invokeAll(tasks);

            loggerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threadPool.shutdown();

        return logger.getResult();
    }
}
