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

                var servedItems = queue.getServed();
                var rejectedItems = queue.getRejected();
                var chanceOfReject = (double) rejectedItems / (servedItems + rejectedItems);
                var averageQueueLength = counter / ((double) Runner.SIMULATION_DURATION / SLEEP_TIME);
                System.out.printf("%nRunner %s%nServed: %s%nRejected: %s%nReject chance: %4$,.3f%nAverage queue length: %5$,.3f%n", runnerName, servedItems, rejectedItems, chanceOfReject, averageQueueLength);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    SimulationResult getResult() {
        var servedItems = queue.getServed();
        var rejectedItems = queue.getRejected();

        return new SimulationResult(rejectedItems, servedItems, (double) rejectedItems / (servedItems + rejectedItems), counter / ((double) Runner.SIMULATION_DURATION / SLEEP_TIME));
    }
}
