import static java.lang.Thread.sleep;

public final class Logger implements Runnable {

    private static final int SLEEP_TIME = 500;

    private final Queue queue;
    private final long startTime;
    private final String runnerName;
    private int counter;

    public Logger(Queue queue, long startTime, String runnerName) {
        this.queue = queue;
        this.startTime = startTime;
        this.runnerName = runnerName;
    }

    @Override
    public void run() {
        while (System.currentTimeMillis() - startTime <= Runner.SIMULATION_DURATION) {
            try {
                sleep(SLEEP_TIME);
                var queueSize = queue.size();
                counter += queueSize;
                System.out.printf("Runner: %s Served: %s Rejected: %s Queue length: %s%n", runnerName, queue.getServed(), queue.getRejected(), queueSize);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public double getAverageQueueLength() {
        return counter / ((double) Runner.SIMULATION_DURATION / SLEEP_TIME);
    }
}
