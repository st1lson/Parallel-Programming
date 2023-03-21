package mkr.buffer;

import java.util.concurrent.locks.ReentrantLock;

public class Program {
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
}
