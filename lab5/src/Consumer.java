public final class Consumer implements Runnable {

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
        }
    }
}
