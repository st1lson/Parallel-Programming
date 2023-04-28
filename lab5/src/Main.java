import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public final class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        var tasks = new ArrayList<Callable<SimulationResult>>();

        tasks.add(new Runner("first"));
        tasks.add(new Runner("second"));
        tasks.add(new Runner("third"));

        List<Future<SimulationResult>> results = new ArrayList<>();
        try {
            results = threadPool.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        var servedItems = 0;
        var rejectedItems = 0;
        var queueLength = 0d;
        for (var task : results) {
            var result = task.get();
            servedItems += result.served();
            rejectedItems += result.rejected();
            queueLength += result.averageQueueLength();
        }

        var chanceOfReject = (double) rejectedItems / (servedItems + rejectedItems);
        System.out.printf("%nAggregated result%nServed: %s%nRejected: %s%nReject chance: %3$,.3f%nAverage queue length: %4$,.3f%n", servedItems, rejectedItems, chanceOfReject, queueLength / results.size());

        threadPool.shutdown();
    }
}
