package mkr.state;

import java.util.concurrent.locks.Lock;

public class FirstTask implements Runnable {
    private final Lock locker;

    public FirstTask(Lock locker) {
        this.locker = locker;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
                synchronized(this.locker) {
                    if (Program.state == Program.S) {
                        Program.state = Program.Z;
                    } else {
                        Program.state = Program.S;
                    }

                    locker.notifyAll();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
