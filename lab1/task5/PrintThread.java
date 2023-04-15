package lab1.task5;

public class PrintThread extends Thread {
    private char symbol;
    private Boolean isSynchronized;
    private static long previousThread;
    private static int lineItemsCount = 0;

    private static final Object locker = new Object();

    public PrintThread(char symbol, Boolean isSynchronized) {
        this.symbol = symbol;
        this.isSynchronized = isSynchronized;
    }

    @Override
    public void run() {
        for (int i = 0; i < 50 * 50; i++) {
            if (this.isSynchronized) {
                printSync();
            } else {
                System.out.print(symbol);
            }

            PrintThread.lineItemsCount++;
            if (PrintThread.lineItemsCount == 100) {
                PrintThread.lineItemsCount = 0;
                System.out.println();
            }
        }
    }

    private void printSync() {
        synchronized (locker) {
            try {
                final long threadId = Thread.currentThread().threadId();
                while (PrintThread.previousThread == threadId) {
                    locker.wait();
                }
        
                System.out.print(symbol);
                PrintThread.previousThread = threadId;
            } catch (Exception e) {
            } finally {
                locker.notifyAll();
            }
        }
    }
}
