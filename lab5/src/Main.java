import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    public static void main(String[] args) {
        var executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        var callables = new ArrayList<Callable<Object>>();

        callables.add(Executors.callable(new Runner(1)));
        callables.add(Executors.callable(new Runner(2)));
        callables.add(Executors.callable(new Runner(3)));

        try {
            executor.invokeAll(callables);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }
}
