package mkr.threadPool;

import java.util.concurrent.locks.Lock;

public class Calculator {
    private int result = 0;

    private final Lock locker;

    public Calculator(Lock locker) {
        this.locker = locker;
    }

    public int getResult() {
        return this.result;
    }

    public void increment(int value) {
        this.locker.lock();
        try {
            this.result += value;
        } finally {
            this.locker.unlock();
        }
    }
}
