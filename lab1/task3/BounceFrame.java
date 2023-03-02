package lab1.task3;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.*;

public class BounceFrame extends JFrame {
    private BallCanvas canvas;
    public static final int WIDTH = 700;
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

        JButton firstAddBallsButton = new JButton("Add balls (1 red 50 blue)");
        JButton secondAddBallsButton = new JButton("Add balls (1 red 500 blue)");

        JButton buttonStop = new JButton("Stop");

        firstAddBallsButton.addActionListener((ActionEvent e) -> {
            for (int i = 0; i < 50; i++) {
                canvas.add(new Ball(canvas));
            }

            canvas.add(new Ball(canvas, BallType.RED));
        });

        secondAddBallsButton.addActionListener((ActionEvent e) -> {
            for (int i = 0; i < 500; i++) {
                canvas.add(new Ball(canvas));
            }

            canvas.add(new Ball(canvas, BallType.RED));
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
        buttonPanel.add(firstAddBallsButton);
        buttonPanel.add(secondAddBallsButton);
        buttonPanel.add(buttonStop);

        content.add(buttonPanel, BorderLayout.SOUTH);
    }
}
