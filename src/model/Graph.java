package model;

import java.util.HashMap;
import java.util.HashSet;
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
}
