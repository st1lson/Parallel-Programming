package mkr.buffer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BufferTask {
    public static int index = 0;

    private final static int BUFFER_SIZE = 5;

    public static void main(String[] args) {
        var locker = new ReentrantLock();
        var collectionFull = locker.newCondition();
        var collectionEmpty = locker.newCondition();
        var buffer = new Object[BUFFER_SIZE];

        var pusher = new Thread(new BufferPusher(locker, collectionFull, collectionEmpty, buffer));

        var firstRemover = new Thread(new BufferRemover(locker, collectionFull, collectionEmpty, buffer));
        var secondRemover = new Thread(new BufferRemover(locker, collectionFull, collectionEmpty, buffer));

        pusher.start();
        firstRemover.start();
        secondRemover.start();
        try {
            pusher.join();
            firstRemover.join();
            secondRemover.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class BufferPusher implements Runnable {
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

    public static class BufferRemover implements Runnable {
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
}