package lab1.task4;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.JPanel;

public class BallCanvas extends JPanel {
    private ArrayList<Ball> balls = new ArrayList<>();
    
    public void add(Ball ball) {
        this.balls.add(ball);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics;
        for (int i = 0; i < balls.size(); i++) {
            Ball ball = balls.get(i);
            ball.draw(g2);
        }
    }
}
