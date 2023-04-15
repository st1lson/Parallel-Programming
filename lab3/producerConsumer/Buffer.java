package lab3.producerConsumer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class Buffer {
    private final Lock locker = new ReentrantLock();
    private final Condition notFull = locker.newCondition();
    private final Condition notEmpty = locker.newCondition();

    private final Object[] items = new Object[100];

    private int count;
    private int takePtr;
    private int putPtr;

    public final void put(Object x)
            throws InterruptedException {
        locker.lock();
        try {
            while (count == items.length) {
                notFull.await();
            }

            items[putPtr] = x;
            if (++putPtr == items.length) {
                putPtr = 0;
            }

            ++count;
            notEmpty.signal();
        } finally {
            locker.unlock();
        }
    }

    public final Object take() throws InterruptedException {
        locker.lock();
        try {
            while (count == 0) {
                notEmpty.await();
            }

            Object x = items[takePtr];
            if (++takePtr == items.length) {
                takePtr = 0;
            }

            --count;
            notFull.signal();

            return x;
        } finally {
            locker.unlock();
        }
    }
}
