package mkr.buffer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class BufferPusher implements Runnable {
    private final Lock locker;

    private final Condition collectionFull;
    private final Condition collectionEmpty;

    private final Object[] buffer;

    public BufferPusher(Lock locker, Condition collectionFull, Condition collectionEmpty, Object[] buffer) {
        this.locker = locker;
        this.collectionFull = collectionFull;
        this.collectionEmpty = collectionEmpty;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            this.locker.lock();
            try {
                while (Program.index == buffer.length) {
                    System.out.println("The buffer is full");
                    try {
                        this.collectionFull.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
    
                if (Program.index == -1) {
                    Program.index = this.buffer.length - 1;
                }

                this.buffer[Program.index] = new Object();
                Program.index += 1;
                this.collectionEmpty.signal();
            } finally {
                this.locker.unlock();
            }
        }
    }
}
