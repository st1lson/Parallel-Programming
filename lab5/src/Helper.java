import java.util.concurrent.Callable;

public class Helper {
    public static Callable<Void> toCallable(final Runnable runnable) {
        return () -> {
            runnable.run();

            return null;
        };
    }
}
