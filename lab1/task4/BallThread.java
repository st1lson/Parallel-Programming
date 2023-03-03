package lab1.task4;

public class BallThread extends Thread {
    private Ball ball;
    private BallThread parentThread;

    public BallThread(Ball ball, BallThread parentThread) {
        this.ball = ball;
        this.parentThread = parentThread;
    }

    public BallThread(Ball ball) {
        this.ball = ball;
    }

    @Override
    public void run() {
        try {
            if (this.parentThread != null)
                this.parentThread.join();
            for (int i = 1; i< 1000; i++) {
                ball.move();
                Thread.sleep(5);
            }
        } catch (Exception e) {
        }
    }
}
