package model;

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
