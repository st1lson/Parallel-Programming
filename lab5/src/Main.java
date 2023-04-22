import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public final class Main {

    public static void main(String[] args) {
        var threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        var tasks = new ArrayList<Callable<Object>>();

        tasks.add(Executors.callable(new Runner("first")));
        tasks.add(Executors.callable(new Runner("second")));
        tasks.add(Executors.callable(new Runner("third")));

        try {
            threadPool.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threadPool.shutdown();
    }
}
