import java.util.Random;

import static java.lang.Thread.sleep;

class Producer implements Runnable {
    private static final int TIME_TO_PRODUCE = 1000;
    private final long startTime;
    private final long workTime;
    private final Queue queue;

    public Producer(Queue queue, long startTime, long workTime) {
        this.queue = queue;
        this.startTime = startTime;
        this.workTime = workTime;
    }

    @Override
    public void run() {
        var lowTimeLimit = (int) (TIME_TO_PRODUCE - (TIME_TO_PRODUCE * 0.5));
        var highTimeLimit = (int) (TIME_TO_PRODUCE + (TIME_TO_PRODUCE * 0.5));
        var random = new Random();

        while (System.currentTimeMillis() - startTime <= workTime) {
            try {
                sleep(random.nextInt((highTimeLimit - lowTimeLimit) + 1) + lowTimeLimit);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            queue.put(1);
            System.out.println("Item added");
        }
    }
}