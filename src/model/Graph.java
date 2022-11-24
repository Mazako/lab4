package model;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Graph {
    private final HashMap<Node, HashSet<Edge>> graph = new HashMap<>();

    public void add(Node node) {
        graph.put(node, new HashSet<>());
    }

    public HashSet<Node> getAllNodesConnectedTo(Node node) {
        if (!nodeExists(node)) {
            throw new GraphException("Węzeł nie znajduje się w grafie");
        }
        return graph.get(node)
                .stream()
                .map(edge -> edge.getNotCalledNode(node))
                .collect(Collectors.toCollection(HashSet::new));
    }

    public boolean nodeExists(Node node) {
        return graph.containsKey(node);
    }

    public void addEdge(Node firstNode, Node secondNode) {
        if (!nodeExists(firstNode) || !nodeExists(secondNode)) {
            throw new GraphException("Dane węzły nie znajdują się w grafie");
        }
        Edge edge = new Edge(firstNode, secondNode);
        graph.get(firstNode).add(edge);
        graph.get(secondNode).add(edge);
    }

    public Set<Edge> getAllDistinctEdges() {
        return graph.values()
                .stream()
                .flatMap(set -> set.stream().distinct())
                .collect(Collectors.toSet());
    }

    public void drawGraph(Graphics2D g2d) {
        Set<Node> nodes = graph.keySet();
        for (Edge edge : getAllDistinctEdges()) {
            edge.draw(g2d);
        }
        for (Node node : nodes) {
            node.paint(g2d);
        }
    }

    public Set<Node> getAllNodes() {
        return graph.keySet();
    }
}
