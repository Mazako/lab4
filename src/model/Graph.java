package model;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Graph implements PropertyChangeListener, Serializable {

    private static final long serialVersionUID = 1L;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final List<Node> nodes = new ArrayList<>();
    private HashSet<Edge> edges = new HashSet<>();

    public static void saveGraphToFile(File file, Graph graph) {
        try (var oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(graph);
        } catch (FileNotFoundException e) {
            throw new GraphException("Nie znaleziono pliku: " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new GraphException("Błąd w zapisie do pliku");
        }
    }

    public static Graph readGraphFromFile(File file) {
        try (var ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Graph) ois.readObject();
        } catch (FileNotFoundException e) {
            throw new GraphException("Nie znaleziono pliku: " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new GraphException("Błąd w zapisie do pliku");
        } catch (ClassNotFoundException e) {
            throw new GraphException("Niekompatybilny typ klas");
        }
    }

    public ArrayList<Country> getAllCountries() {
        return nodes.stream()
                .map(Node::getCountry)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));

    }

    public Node add(Node node) {
        nodes.add(node);
        node.addPropertyChangeListener(this);
        support.firePropertyChange("nodeSize", nodes.size() - 1, nodes.size());
        return node;
    }
    public void remove(Node node) {
        nodes.remove(node);
        node.removePropertyChangeListener(this);
        support.firePropertyChange("nodeSize", nodes.size() + 1, nodes.size());
        edges.removeIf(next -> next.getFirstNode().equals(node) || next.getSecondNode().equals(node));
    }

    public List<Node> getAllNodesConnectedTo(Node node) {
        if (!nodeExists(node)) {
            throw new GraphException("Węzeł nie znajduje się w grafie");
        }
        return edges.stream()
                .filter(edge -> edge.edgeContainsNode(node))
                .map(edge -> edge.getNotCalledNode(node))
                .collect(Collectors.toList());
    }

    public List<Node> getAllNodesInCountry(Country country) {
        return nodes.stream()
                .filter(node -> node.getCountry().equals(country))
                .collect(Collectors.toList());
    }

    public boolean nodeExists(Node node) {
        return nodes.contains(node);
    }

    public void addEdge(Node firstNode, Node secondNode) {
        if (!nodeExists(firstNode) || !nodeExists(secondNode)) {
            throw new GraphException("Dane węzły nie znajdują się w grafie");
        }
        int oldSize = edges.size();
        edges.add(new Edge(firstNode, secondNode));
        edges = new HashSet<>(edges);
        support.firePropertyChange("edgesSize", oldSize ,edges.size());
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

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public List<Node> getAllNodes() {
        return nodes;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object oldValue = evt.getOldValue();
        Object newValue = evt.getNewValue();
        support.firePropertyChange("newName", oldValue, newValue);
    }

    public Graph BFS(Node startNode, Node searchedNode) {
        if (!nodeExists(startNode) || !nodeExists(searchedNode)) {
            throw new GraphException("Węzeł nie znajduje się w grafie");
        }
        Queue<Node> queue = new LinkedList<>();
        List<Node> searched = new ArrayList<>();
        queue.addAll(getAllNodesConnectedTo(startNode));
        searched.add(startNode);;
        HashMap<Node, Node> dependencyMap = new HashMap<>();
        nodes.forEach(node -> dependencyMap.put(node, null));
        getAllNodesConnectedTo(startNode).stream()
                .forEach(n -> dependencyMap.put(n, startNode));
        Node node = null;
        while (!queue.isEmpty()) {
            node = queue.poll();
            if (!searched.contains(node)) {
                searched.add(node);
                List<Node> collect = getAllNodesConnectedTo(node).stream()
                        .filter(n -> !searched.contains(n))
                        .collect(Collectors.toList());
                queue.addAll(collect);
                for (Node n : collect) {
                    if (dependencyMap.get(n) == null) {
                        dependencyMap.put(n, node);
                    }
                }
            }
        }
        if (dependencyMap.get(searchedNode) == null) {
            throw new GraphException("Nie udało się wyszukać najkrótszej drogi");
        }
        Graph graph = new Graph();
        node = searchedNode;
        Node previous = null;
        while (node != null) {
            Node newNode = new Node(node);
            graph.add(newNode);
            if (previous != null) {
                graph.addEdge(newNode, previous);
            }
            previous = newNode;
            node = dependencyMap.get(node);
        }

        return graph;
    }
}
