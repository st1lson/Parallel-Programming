public class LengthCheckerThread extends Thread {

    private static final int SLEEP_TIME = 500;
    private final Queue queue;
    private final long startTime;
    private final long workTime;
    public int lengthCounter = 0;

    public LengthCheckerThread(Queue queue, long startTime, long workTime) {
        this.queue = queue;
        this.startTime = startTime;
        this.workTime = workTime;
    }

    @Override
    public void run() {
        while (System.currentTimeMillis() - startTime <= workTime) {
            try {
                sleep(SLEEP_TIME);
                var queueSize = queue.size();
                lengthCounter += queueSize;
                System.out.printf("Served: %s Rejected: %s Queue length: %s%n", queue.servedItemsCount, queue.rejectedItemsCount, queueSize);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public double getAverageQueueLength() {
        return lengthCounter / (double) workTime / SLEEP_TIME;
    }
}
