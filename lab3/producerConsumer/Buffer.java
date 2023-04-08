package lab3.producerConsumer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private final Lock locker = new ReentrantLock();
    private final Condition notFull = locker.newCondition();
    private final Condition notEmpty = locker.newCondition();

    private final Object[] items = new Object[100];

    private int count;
    private int takePtr;
    private int putPtr;

    public void put(Object x)
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
            printCount();
            locker.unlock();
        }
    }

    public Object take() throws InterruptedException {
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
            printCount();
            locker.unlock();
        }
    }

    private void printCount() {
        System.out.println(String.format("Buffer's count: %s", count));
    }
}
