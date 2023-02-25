package lab1.task4;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class Ball {
    private Component canvas;
    private static final int XSIZE = 20;
    private static final int YSIZE = 20;
    private int x = 0;
    private int y = 0;
    private int dx = 2;
    private int dy = 2;
    private Color color;

    public Ball(Component component) {
        this.canvas = component;
        if (Math.random() < 0.5) {
            x = new Random().nextInt(this.canvas.getWidth());
            y = 0;
        } else {
            x = 0;
            y = new Random().nextInt(this.canvas.getHeight());
        }
        Random random = new Random();
        this.color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    public void draw(Graphics2D g2) {
        g2.setColor(this.color);
        g2.fill(new Ellipse2D.Double(x, y, XSIZE, YSIZE));
    }

    public void move() {
        x += dx;
        y += dy;
        if (x < 0) {
            x = 0;
            dx= -dx;
        }

        if (x + XSIZE >= this.canvas.getWidth()) {
            x = this.canvas.getWidth() - XSIZE;
            dx = - dx;
        }

        if (y < 0) {
            y = 0;
            dy = -dy;
        }

        if (y + YSIZE >= this.canvas.getHeight()) {
            y = this.canvas.getHeight() - YSIZE;
            dy = - dy;
        }
        
        this.canvas.repaint();
    }
}