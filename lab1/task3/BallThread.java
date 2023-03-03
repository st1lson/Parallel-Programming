package lab1.task3;

public class BallThread extends Thread {
    private Ball ball;

    public BallThread(Ball ball) {
        this.ball = ball;
        setPriority(ball.getType() == BallType.RED ? Thread.MAX_PRIORITY : Thread.MIN_PRIORITY);
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i < 10000; i++) {
                ball.move();
                Thread.sleep(5);
            }
        } catch (Exception e) {
        }
    }
}
