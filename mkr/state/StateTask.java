package mkr.state;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StateTask {
    public static String state = Program.S;

    public static final String Z = "Z";

    public static final String S = "S";

    public static void main(String[] args) {
        var locker = new ReentrantLock();
        var firstTask = new Thread(new FirstTask(locker));
        var secondTask = new Thread(new SecondTask(locker));

        firstTask.start();
        secondTask.start();

        try {
            firstTask.join();
            secondTask.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class FirstTask implements Runnable {
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
    
    public static class SecondTask implements Runnable {

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
}
