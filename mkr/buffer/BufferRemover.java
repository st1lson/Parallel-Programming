package mkr.buffer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class BufferRemover implements Runnable {
    private final Lock locker;

    private final Condition collectionFull;
    private final Condition collectionEmpty;

    private final Object[] buffer;

    public BufferRemover(Lock locker, Condition collectionFull, Condition collectionEmpty, Object[] buffer) {
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
                while (Program.index < 0) {
                    System.out.println("The buffer is empty");
                    try {
                        this.collectionEmpty.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
    
                if (Program.index == this.buffer.length) {
                    Program.index = this.buffer.length - 1;
                }

                this.buffer[Program.index] = null;
                Program.index -= 1;
                this.collectionFull.signal();
            } finally {
                this.locker.unlock();
            }
        }
    }
}
