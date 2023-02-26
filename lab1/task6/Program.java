package lab1.task6;

import lab1.task6.Counters.DefaultCounter;
import lab1.task6.Counters.ICounter;
import lab1.task6.Counters.LockCounter;
import lab1.task6.Counters.SyncBlockCounter;
import lab1.task6.Counters.SynchronizedCounter;

public class Program {
    public static void main(String[] args) {
        defaultCounter();
        synchronizedCounter();
        syncBlockCounter();
        lockCounter();
    }

    private static void defaultCounter() {
        ICounter counter = new DefaultCounter();
        runCounter(counter);
        System.out.println("Default counter:" + counter.getCounter());
    }

    private static void synchronizedCounter() {
        ICounter counter = new SynchronizedCounter();
        runCounter(counter);

        System.out.println("Synchronized counter:" + counter.getCounter());
    }

    private static void syncBlockCounter() {
        ICounter counter = new SyncBlockCounter();
        runCounter(counter);

        System.out.println("Synchronized block counter:" + counter.getCounter());
    }

    private static void lockCounter() {
        ICounter counter = new LockCounter();
        runCounter(counter);

        System.out.println("Lock counter:" + counter.getCounter());
    }

    private static void runCounter(ICounter counter) {
        CountThread incrementThread = new CountThread(true, counter);
        CountThread decrementThread = new CountThread(false, counter);
        incrementThread.start();
        decrementThread.start();

        try {
            incrementThread.join();
            decrementThread.join();
        } catch (Exception e) {
        }
    }
}
