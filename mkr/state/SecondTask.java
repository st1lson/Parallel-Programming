package mkr.state;

import java.util.concurrent.locks.Lock;

public class SecondTask implements Runnable {

    private final Lock locker;

    public SecondTask(Lock locker) {
        this.locker = locker;
    }

    @Override
    public void run() {
        while (true) {
            try {
                synchronized(this.locker) {
                    while (Program.state != Program.S) {
                        locker.wait();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 100; i > 0; i--) {
                System.out.println(i);
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
