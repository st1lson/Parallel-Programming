package lab1.task6;

import lab1.task6.Counters.ICounter;

public class CountThread extends Thread {
    private boolean increment;
    private ICounter counter;

    public CountThread(boolean increment, ICounter counter) {
        this.increment = increment;
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100000; i++) {
            if (this.increment) {
                this.counter.increment();
            } else {
                this.counter.decrement();
            }
        }
    }
}
