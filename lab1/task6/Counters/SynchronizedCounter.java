package lab1.task6.Counters;

public class SynchronizedCounter implements ICounter {
    private int value = 0;

    @Override
    public int getCounter() {
        return this.value;
    }

    @Override
    public synchronized void increment() {
        this.value++;
    }

    @Override
    public synchronized void decrement() {
        this.value--;
    }
    
}
