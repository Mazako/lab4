package gui;

import model.Graph;
import model.Node;

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
         Node node1 = new Node("A", Color.BLACK);
         Node node2 = new Node("B", Color.BLACK);
         Node node3 = new Node("C", Color.BLACK);
         graph.add(node1);
         graph.add(node2);
         graph.add(node3);
         graph.addEdge(node1, node2);
        graph.addEdge(node1, node3);
         System.out.println(graph.getAllDistinctEdges());
         System.out.println(graph.getAllNodesConnectedTo(node1));

    }
}

