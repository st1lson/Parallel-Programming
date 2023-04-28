import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public final class Queue {

    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final ArrayList<Integer> items;
    private final int itemsCount;
    private int served = 0;
    private int rejected = 0;

    public Queue(int itemsCount) {
        this.itemsCount = itemsCount;
        items = new ArrayList<>(itemsCount);
    }

    public int getServed() {
        return served;
    }

    public int getRejected() {
        return rejected;
    }

    public synchronized void incrementServed() {
        served++;
    }

    public int size() {
        return items.size();
    }

    public void serve() {
        lock.lock();
        try {
            while (items.isEmpty()) {
                notEmpty.await();
            }

            items.remove(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void put(int item) {
        lock.lock();
        try {
            if (items.size() == itemsCount) {
                rejected++;
                return;
            }

            items.add(item);
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
