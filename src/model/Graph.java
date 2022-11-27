package model;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Graph {
    private final List<Node> nodes = new ArrayList<>();
    private final HashSet<Edge> edges = new HashSet<>();

    public Node add(Node node) {
        nodes.add(node);
        return node;
    }
    public void remove(Node node) {
        nodes.remove(node);
        edges.removeIf(next -> next.getFirstNode().equals(node) || next.getSecondNode().equals(node));
    }

    public List<Node> getAllNodesConnectedTo(Node node) {
        if (!nodeExists(node)) {
            throw new GraphException("Węzeł nie znajduje się w grafie");
        }
        return edges.stream()
                .map(edge -> edge.getNotCalledNode(node))
                .collect(Collectors.toList());

    }

    public boolean nodeExists(Node node) {
        return nodes.contains(node);
    }

    public void addEdge(Node firstNode, Node secondNode) {
        if (!nodeExists(firstNode) || !nodeExists(secondNode)) {
            throw new GraphException("Dane węzły nie znajdują się w grafie");
        }
        edges.add(new Edge(firstNode, secondNode));
    }

    public Set<Edge> getAllEdges() {
        return edges;
    }

    public void drawGraph(Graphics2D g2d) {
        edges.stream()
                .forEach(edge -> edge.draw(g2d));
        nodes.stream()
                .forEach(node -> node.paint(g2d));
    }

    public List<Node> getAllNodes() {
        return nodes;
    }
}
