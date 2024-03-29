import static java.lang.Thread.sleep;

public final class Consumer implements Runnable {

    private static final long WAIT_TIME = 175;

    private final Queue queue;
    private final long startTime;

    public Consumer(Queue queue, long startTime) {
        this.queue = queue;
        this.startTime = startTime;
    }

    @Override
    public void run() {
        while (System.currentTimeMillis() - startTime <= Runner.SIMULATION_DURATION) {
            queue.serve();

            try {
                sleep(WAIT_TIME);
                queue.incrementServed();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
