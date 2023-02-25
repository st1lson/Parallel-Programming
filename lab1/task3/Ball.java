package lab1.task3;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Ball {
    private BallCanvas canvas;
    private static final int XSIZE = 20;
    private static final int YSIZE = 20;
    private int x = 0;
    private int y = 0;
    private int dx = 2;
    private int dy = 2;

    private final BallType type;

    public Ball(BallCanvas component) {
        this(component, BallType.BLUE);
    }

    public Ball(BallCanvas component, BallType type) {
        this.canvas = component;
        this.type = type;
    }

    public BallType getType() {
        return this.type;
    }

    public void draw(Graphics2D g2) {
        if (type == BallType.BLUE) {
            g2.setColor(Color.blue);
        }
        else {
            g2.setColor(Color.red);
        }

        g2.fill(new Ellipse2D.Double(x, y, XSIZE, YSIZE));
    }

    public void move() {
        x += dx;
        y += dy;
        if (x < 0) {
            x = 0;
            dx = -dx;
        }

        if (x + XSIZE >= this.canvas.getWidth()) {
            x = this.canvas.getWidth() - XSIZE;
            dx = -dx;
        }

        if (y < 0) {
            y = 0;
            dy = -dy;
        }

        if (y + YSIZE >= this.canvas.getHeight()) {
            y = this.canvas.getHeight() - YSIZE;
            dy = -dy;
        }

        this.canvas.repaint();
    }
}