package gui;

import model.Graph;
import model.Node;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static final int WINDOW_WIDTH = 700;
    public static final int WINDOW_HEIGHT = 700;
    private final PaintFrame paintPanel = new PaintFrame();


    public MainFrame() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setLocationRelativeTo(null);
        this.add(paintPanel);
        this.setVisible(true);
    }

}

