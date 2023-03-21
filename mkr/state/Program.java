package mkr.state;

import java.util.concurrent.locks.ReentrantLock;

public class Program {
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
}
