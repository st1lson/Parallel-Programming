package lab1.task3;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.*;

public class BounceFrame extends JFrame {
    private BallCanvas canvas;
    public static final int WIDTH = 450;
    public static final int HEIGHT = 350;

    public BounceFrame() {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Bound program");

        this.canvas = new BallCanvas();
        Container content = this.getContentPane();
        content.add(this.canvas, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);

        JButton buttonStart = new JButton("Start");

        JButton buttonAddBlue = new JButton("Add blue");
        JButton buttonAddRed = new JButton("Add red");

        JButton buttonStop = new JButton("Stop");

        buttonAddBlue.addActionListener((ActionEvent e) -> {
            for (int i = 0; i < 100; i++) {
                Ball ball = new Ball(canvas);
                canvas.add(ball);
            }
        });

        buttonAddRed.addActionListener((ActionEvent e) -> {
            Ball ball = new Ball(canvas, BallType.RED);
            canvas.add(ball);
        });

        buttonStart.addActionListener((ActionEvent e) -> {
            for (Ball ball : canvas.getBalls()) {
                BallThread thread = new BallThread(ball);
                thread.start();
            }
        });

        buttonStop.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });

        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonAddBlue);
        buttonPanel.add(buttonAddRed);
        buttonPanel.add(buttonStop);

        content.add(buttonPanel, BorderLayout.SOUTH);
    }
}
