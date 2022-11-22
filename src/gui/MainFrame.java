package gui;

import model.Graph;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static final int WINDOW_WIDTH = 700;
    public static final int WINDOW_HEIGHT = 700;
    private final JPanel paintPanel = new JPanel();

    private final Graph graph = new Graph();


    public MainFrame() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.add(paintPanel);
        drawInitData();
    }

    private void drawInitData() {
         Graphics2D g2d = (Graphics2D) paintPanel.getGraphics();

    }
}

