import java.util.Random;

import static java.lang.Thread.sleep;

public final class Producer implements Runnable {

    private static final int TIME_TO_PRODUCE = 40;

    private final long startTime;
    private final Queue queue;

    public Producer(Queue queue, long startTime) {
        this.queue = queue;
        this.startTime = startTime;
    }

    @Override
    public void run() {
        var random = new Random();

        while (System.currentTimeMillis() - startTime <= Runner.SIMULATION_DURATION) {
            try {
                sleep(random.nextInt(TIME_TO_PRODUCE));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            queue.put(1);
        }
    }
}
