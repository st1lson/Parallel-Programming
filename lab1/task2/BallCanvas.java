package lab1.task2;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;

public class BallCanvas extends JPanel {
    private ArrayList<Pocket> pockets = new ArrayList<>();
    private ArrayList<Ball> balls = new ArrayList<>();

    public ArrayList<Pocket> getPockets() {
        return pockets;
    }

    public void add(Pocket ball) {
        this.pockets.add(ball);
    }
    
    public void add(Ball ball) {
        this.balls.add(ball);
    }

    public void removeBall(Ball ball) {
        this.balls.remove(ball);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics;
        for (int i = 0; i < pockets.size(); i++) {
            Pocket pocket = pockets.get(i);
            pocket.draw(g2);
        }

        for (int i = 0; i < balls.size(); i++) {
            Ball ball = balls.get(i);
            ball.draw(g2);
        }
    }
}
