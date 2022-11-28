package model;

import javax.swing.plaf.synth.SynthRootPaneUI;
import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

public class Edge implements Serializable {

    private static final long serialVersionUID = 1L;
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
        if (node.equals(firstNode)) {
            return secondNode;
        } else if (node.equals(secondNode)) {
            return firstNode;
        } else {
            return null;
        }
    }

    public boolean edgeContainsNode(Node node) {
        return node.equals(firstNode) || node.equals(secondNode);
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(transport.getColor());
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(firstNode.getX(), firstNode.getY(), secondNode.getX(), secondNode.getY());
    }

    public boolean isMouseOver(int x, int y) {
        double[] line = getLine();
        double a = line[0];
        double b = line[1];
        double mouseY = (a * x + b);
        return (Math.abs(mouseY - y) <= 10);
    }
    private double[] getLine() {
        int x1 = firstNode.getX();
        int y1 = firstNode.getY();
        int y2 = secondNode.getY();
        int x2 = secondNode.getX();
        double a = (double)(y1 - y2) / (x1-x2);
        double b = (double)((x1*y2) - (x2*y1))/(x1-x2);
        return new double[]{a, b};
    }

    public void changeLength(double size) {
        double[] line = getLine();
        double a = line[0];
        double b = line[1];
        int Sx = (firstNode.getX() + secondNode.getX()) / 2;
        int Sy = (firstNode.getY() + secondNode.getY()) / 2;
        changePoint(firstNode, size, Sx, Sy, a, b);
        changePoint(secondNode, size, Sx, Sy, a, b);
    }

    private void changePoint(Node node, double size, int sx, int sy, double a, double b) {
        double distanceToCenter = distance(node.getX(), node.getY(), sx, sy);
        if (size > 0 && distanceToCenter <= 50) {
            return;
        }
        size /= 2;
        double newDistance = distanceToCenter - size;
        double delta = Math.sqrt(a*a*newDistance*newDistance - a*a*sx*sx - 2*a*b*sx + 2*a*sx*sy - b*b + 2*b*sy + newDistance*newDistance - sy*sy);
        double B = -a*b + a*sy + sx;
        double newX1 = (+delta + B)/(a*a + 1);
        double newY1 = a * newX1 + b;
        double newX2 = (-delta + B)/(a*a+1);
        double newY2 = a*newX2 + b;
        if (Math.abs(newX1 - node.getX()) < Math.abs(newX2 - node.getX())) {
            node.setX((int) Math.ceil(newX1));
            node.setY((int) Math.ceil(newY1));
        } else {
            node.setX((int) Math.ceil(newX2));
            node.setY((int) Math.ceil(newY2));
        }

    }

    private double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public Node getFirstNode() {
        return firstNode;
    }

    public void setFirstNode(Node firstNode) {
        this.firstNode = firstNode;
    }

    public Node getSecondNode() {
        return secondNode;
    }

    public void setSecondNode(Node secondNode) {
        this.secondNode = secondNode;
    }

    public MeansOfTransport getTransport() {
        return transport;
    }

    public void setTransport(MeansOfTransport transport) {
        this.transport = transport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (!getFirstNode().equals(edge.getFirstNode()) && !getFirstNode().equals(edge.secondNode)) return false;
        if (!getSecondNode().equals(edge.getSecondNode()) && !getSecondNode().equals(edge.getFirstNode())) return false;
        return getTransport() == edge.getTransport();
    }

    @Override
    public int hashCode() {
        return firstNode.hashCode() + secondNode.hashCode() + transport.hashCode();
    }
}
