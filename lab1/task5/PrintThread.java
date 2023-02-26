package lab1.task5;

public class PrintThread extends Thread {
    private String symbol;
    private static long previousThread;

    private static final Object locker = new Object();

    public PrintThread(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            //System.out.println(symbol);
            printSync();
        }
    }

    private void printSync() {
        synchronized (locker) {
            try {
                long threadId = Thread.currentThread().getId();
                while (this.previousThread == threadId) {
                    locker.wait();
                }
        
                System.out.println(symbol);
                this.previousThread = threadId;
            } catch (Exception e) {
            } finally {
                locker.notifyAll();
            }
        }
    }
}
