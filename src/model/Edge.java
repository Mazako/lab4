package model;

import java.awt.*;
import java.util.Objects;

public class Edge {
    private Node firstNode;
    private Node secondNode;
    private Color color;

    public Edge(Node firstNode, Node secondNode, Color color) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.color = color;
    }

    public Edge(Node firstNode, Node secondNode) {
        this(firstNode, secondNode, Color.BLACK);
    }

    public Node getNotCalledNode(Node node) {
        return node.equals(firstNode) ? secondNode : firstNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(firstNode, edge.firstNode) && Objects.equals(secondNode, edge.secondNode) && Objects.equals(color, edge.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstNode, secondNode, color);
    }
}
