package lab1.task6.Counters;

public class SyncBlockCounter implements ICounter {
    private int value;

    @Override
    public int getCounter() {
        return this.value;
    }

    @Override
    public void increment() {
        synchronized(this) {
            this.value++;
        }
    }

    @Override
    public void decrement() {
        synchronized(this) {
            this.value--;
        }
    }
}
