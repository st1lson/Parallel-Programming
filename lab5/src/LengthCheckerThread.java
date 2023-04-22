public class LengthCheckerThread extends Thread {

    private static final int SLEEP_TIME = 500;

    private final Queue queue;
    private final long startTime;
    private int counter;

    public LengthCheckerThread(Queue queue, long startTime) {
        this.queue = queue;
        this.startTime = startTime;
    }

    @Override
    public void run() {
        while (System.currentTimeMillis() - startTime <= Runner.SIMULATION_DURATION) {
            try {
                sleep(SLEEP_TIME);
                var queueSize = queue.size();
                counter += queueSize;
                System.out.printf("Served: %s Rejected: %s Queue length: %s%n", queue.getServed(), queue.getRejected(), queueSize);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public double getAverageQueueLength() {
        return counter / ((double) Runner.SIMULATION_DURATION / SLEEP_TIME);
    }
}
