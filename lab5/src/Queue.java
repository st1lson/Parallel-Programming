import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Queue {
    final Lock lock = new ReentrantLock();
    final Condition notEmpty = lock.newCondition();
    final ArrayList<Integer> items;
    private final int totalItemsCount;
    public int servedItemsCount = 0;
    public int rejectedItemsCount = 0;

    public Queue(int itemsCount) {
        items = new ArrayList<>(itemsCount);
        totalItemsCount = itemsCount;
    }

    public int size() {
        return items.size();
    }

    public void serve() {
        lock.lock();
        int item = 0;
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

        try {
            sleep(150);
            servedItemsCount++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void put(int item) {
        lock.lock();
        try {
            if (items.size() == totalItemsCount) {
                rejectedItemsCount++;
                return;
            }

            items.add(item);
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }
}