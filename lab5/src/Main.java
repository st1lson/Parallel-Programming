import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        var executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        var callables = new ArrayList<Callable<Void>>();

        callables.add(Helper.toCallable(new Runner(1)));
        callables.add(Helper.toCallable(new Runner(2)));
        callables.add(Helper.toCallable(new Runner(3)));

        try {
            executor.invokeAll(callables);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }
}