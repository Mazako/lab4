package model;

import javax.swing.plaf.synth.SynthRootPaneUI;
import java.awt.*;
import java.util.Objects;

public class Edge {
    private Node firstNode;
    private Node secondNode;
    private MeansOfTransport transport;

    public Edge(Node firstNode, Node secondNode, MeansOfTransport transport) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.transport = transport;
    }

    public Edge(Node firstNode, Node secondNode) {
        this(firstNode, secondNode, MeansOfTransport.CAR);
    }

    public Node getNotCalledNode(Node node) {
        return node.equals(firstNode) ? secondNode : firstNode;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(transport.getColor());
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(firstNode.getX(), firstNode.getY(), secondNode.getX(), secondNode.getY());
    }

    public boolean isMouseOver(int x, int y) {
        int x1 = firstNode.getX();
        int y1 = firstNode.getY();
        int y2 = secondNode.getY();
        int x2 = secondNode.getX();
        double a = (double)(y1 - y2) / (x1-x2);
        double b = (double)((x1*y2) - (x2*y1))/(x1-x2);
        double mouseY = (a * x + b);
        return Math.abs(mouseY - y) <= 10;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return (Objects.equals(firstNode, edge.firstNode) && Objects.equals(secondNode, edge.secondNode)) || (Objects.equals(firstNode, edge.secondNode) && Objects.equals(secondNode, edge.secondNode)) && Objects.equals(transport, edge.transport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstNode, secondNode, transport);
    }
}
