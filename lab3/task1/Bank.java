package lab3.task1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bank {
    public static final int NTEST = 10000;
    private final int[] accounts;
    private long ntransacts = 0;

    private final Lock locker = new ReentrantLock();

    public Bank(int n, int initialBalance) {
        accounts = new int[n];
        int i;
        for (i = 0; i < accounts.length; i++)
            accounts[i] = initialBalance;
        ntransacts = 0;
    }

    public void transfer(int from, int to, int amount) {
        accounts[from] -= amount;
        accounts[to] += amount;
        ntransacts++;
        if (ntransacts % NTEST == 0) {
            test();
        }
    }

    public synchronized void syncTransfer(int from, int to, int amount) {
        transfer(from, to, amount);
    }

    public void syncBlockTransfer(int from, int to, int amount) {
        synchronized (this) {
            transfer(from, to, amount);
        }
    }

    public void transferLock(int from, int to, int amount) {
        locker.lock();
        try {
            transfer(from, to, amount);
        } finally {
            locker.unlock();
        }
    }

    public synchronized void waitTransfer(int from, int to, int amount) {
        while (accounts[from] < amount) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        transfer(from, to, amount);
        notifyAll();
    }

    public void test() {
        int sum = 0;
        for (int i = 0; i < accounts.length; i++)
            sum += accounts[i];
        System.out.println("Transactions:" + ntransacts
                + " Sum: " + sum);
    }

    public int size() {
        return accounts.length;
    }
}
