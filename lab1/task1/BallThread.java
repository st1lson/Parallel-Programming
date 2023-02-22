package lab1.task1;

public class BallThread extends Thread {
    private Ball ball;

    public BallThread(Ball ball) {
        this.ball = ball;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i< 10000; i++) {
                ball.move();
                System.out.println("Thread name = " + Thread.currentThread().getName());
                Thread.sleep(5);
            }
        } catch (Exception e) {
        }
    }
}
