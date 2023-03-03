package lab1.task2;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Pocket {
    private static final int XSIZE = 20;
    private static final int YSIZE = 20;
    private int x = 0;
    private int y = 0;

    public Pocket(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.black);
        g2.fill(new Ellipse2D.Double(x, y, XSIZE, YSIZE));
    }

    public boolean intersection(int x, int y) {
        return x >= this.x && x <= this.x + XSIZE && y >= this.y && y <= this.y + YSIZE;
    }
}
