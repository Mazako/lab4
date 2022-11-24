package gui;

import model.Edge;
import model.Graph;
import model.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;

public class PaintFrame extends JPanel implements MouseMotionListener, MouseListener, KeyListener, MouseWheelListener {
    protected Graph graph = new Graph();
    private int mouseX = 0;
    private int mouseY = 0;
    private boolean mouseButtonLeft = false;
    private boolean mouseButtonRight = false;
    private Node nodeUnderCursor = null;
    private Edge edgeUnderCursor = null;

    public PaintFrame() {
        createInitData();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        this.addMouseWheelListener(this);
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        if (graph != null) {
            graph.drawGraph(g2d);
        }
    }

    private Node findNode(int mx, int my) {
        Set<Node> allNodes = graph.getAllNodes();
        return allNodes.stream()
                .filter(node -> node.isMouseOver(mx, my))
                .findFirst()
                .orElse(null);
    }
    private void createInitData() {
        Node node1 = new Node("Warszawa", Color.LIGHT_GRAY, 100, 100, 30);
        Node node2 = new Node("Wrocław", Color.LIGHT_GRAY, 200, 100, 30);
        Node node3 = new Node("Berlin", Color.LIGHT_GRAY, 400, 500, 30);
        graph.add(node1);
        graph.add(node2);
        graph.add(node3);
        graph.addEdge(node1, node2);
        graph.addEdge(node1, node3);
        graph.addEdge(node2, node3);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1)  {
            mouseButtonLeft = true;
        }
        if (e.getButton() == 3) {
            mouseButtonRight = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == 1) {
            mouseButtonLeft = false;
        }
        if (e.getButton() == 3) {
            mouseButtonRight = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mouseButtonLeft) {
            if (nodeUnderCursor != null) {
                nodeUnderCursor.move(e.getX() - mouseX, e.getY() - mouseY);

            }
            if (edgeUnderCursor != null) {
                System.out.println("masz mnie kurwa");
            }
        }
        mouseX = e.getX();
        mouseY = e.getY();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        nodeUnderCursor = findNode(mouseX, mouseY);
        edgeUnderCursor = findCoursor(mouseX, mouseY);
        System.out.println(edgeUnderCursor);
    }

    private Edge findCoursor(int mouseX, int mouseY) {
        return graph.getAllDistinctEdges().stream()
                .filter(edge -> edge.isMouseOver(mouseX, mouseY))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (mouseButtonLeft && nodeUnderCursor != null) {
            if (e.getWheelRotation() == 1) {
                nodeUnderCursor.increaseSize(5);
            } else {
                nodeUnderCursor.increaseSize(-5);
            }
        }
        repaint();
    }
}
