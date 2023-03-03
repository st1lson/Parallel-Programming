package lab1.task6.Counters;

import java.util.concurrent.locks.ReentrantLock;

public class LockCounter implements ICounter {
    private int value = 0;

    ReentrantLock locker= new ReentrantLock();

    @Override
    public int getCounter() {
        return this.value;
    }

    @Override
    public void increment() {
        locker.lock();

        try {
            this.value++;
        } finally {
            locker.unlock();
        }
    }

    @Override
    public void decrement() {
        locker.lock();

        try {
            this.value--;
        } finally {
            locker.unlock();
        }
    }
}
