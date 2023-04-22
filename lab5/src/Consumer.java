class Consumer implements Runnable {

    private final Queue queue;
    private final long startTime;
    private final long workTime;


    public Consumer(Queue queue, long startTime, long workTime) {
        this.queue = queue;
        this.startTime = startTime;
        this.workTime = workTime;
    }

    @Override
    public void run() {
        while (System.currentTimeMillis() - startTime <= workTime) {
            queue.serve();
        }
    }
}