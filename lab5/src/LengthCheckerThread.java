public class LengthCheckerThread extends Thread {

    private static final int SLEEP_TIME = 50;
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
                System.out.print("Served: " + queue.servedItemsCount + " Rejected: " + queue.rejectedItemsCount + " Queue length " + queueSize);
                if (!(queue.servedItemsCount == 0 && queue.rejectedItemsCount == 0)) {
                    System.out.println("Chance of reject " + (double) queue.rejectedItemsCount / ((double) queue.rejectedItemsCount + (double) queue.servedItemsCount));
                } else {
                    System.out.println();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void showAvgLength() {
        System.out.println("Average queue length: " + lengthCounter / (double) workTime / SLEEP_TIME);
    }
}
