/*
 *  Laboratorium 4
 *
 *   Autor: Michal Maziarz, 263913
 *    Data: listopad 2022 r.
 */
package model;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa reprezentująca graf
 *
 * @author Michał Maziarz
 * @version listopad 2022
 */
public class Graph implements PropertyChangeListener, Serializable {

    /**
     * Pole oznaczające ID wersji klasy do serializacji obiektu
     */
    private static final long serialVersionUID = 1L;

    /**
     * słuchacz dla wzorca obserwator. pozwala nasłuchiwanie zmian w obiekcie przez obserwatorów
     */
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Lista wszystkich wierzchołków grafu
     */
    private final List<Node> nodes = new ArrayList<>();

    /**
     * Zbiór wszystkich krawędzi grafu
     */
    private HashSet<Edge> edges = new HashSet<>();

    /**
     * Metoda pozwalająca zserializować graf i zapisać go do pliku binarnego
     * @param file obiekt pliku, do którego ma zostać zapisany graf
     * @param graph graf do zapisania
     * @exception GraphException wyjątek zgłaszany kiedy zapis do pliku się nie uda
     *
     */
    public static void saveGraphToFile(File file, Graph graph) {
        try (var oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(graph);
        } catch (FileNotFoundException e) {
            throw new GraphException("Nie znaleziono pliku: " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new GraphException("Błąd w zapisie do pliku");
        }
    }

    /**
     * Metoda pozwalająca wczytać plik binarny i zdeserializować graf
     * @param file plik zawierający plik binarny z grafem
     * @return zdeserializowany graf
     * @exception GraphException wyjątek rzucany, kiedy proces deserializacji grafu się nie powiedzie
     */
    public static Graph readGraphFromFile(File file) {
        try (var ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Graph) ois.readObject();
        } catch (FileNotFoundException e) {
            throw new GraphException("Nie znaleziono pliku: " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new GraphException("Błąd w odczytywaniu z pliku");
        } catch (ClassNotFoundException e) {
            throw new GraphException("Niekompatybilny typ klas");
        }
    }

    /**
     * Metoda zwracająca wszystkie kraje miast, do ktorych należy graf
     * @return lista wszystkich krajów miast
     */
    public ArrayList<Country> getAllCountries() {
        return nodes.stream()
                .map(Node::getCountry)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));

    }

    /**
     * Metoda dodawająca wierzcholek do grafu
     * @param node wierzchołek
     * @return dodany wierzchołek
     */
    public Node add(Node node) {
        nodes.add(node);
        node.addPropertyChangeListener(this);
        support.firePropertyChange("nodeSize", nodes.size() - 1, nodes.size());
        return node;
    }

    /**
     * Metoda usuwająca wierzchołek z grafu
     * @param node wierzchołek do usunięcia
     */
    public void remove(Node node) {
        nodes.remove(node);
        node.removePropertyChangeListener(this);
        support.firePropertyChange("nodeSize", nodes.size() + 1, nodes.size());
        edges.removeIf(next -> next.getFirstNode().equals(node) || next.getSecondNode().equals(node));
    }

    /**
     * Metoda usuwająca krawędź z grafu
     * @param edge krawędź to usunięcia
     */
    public void removeEdge(Edge edge) {
        edges.remove(edge);
        support.firePropertyChange("edgeSize", edges.size() - 1, edges.size());
    }

    /**
     * Metoda zwracająca wszystkie wierzchołki, które są połączone z podanym wierzchołkiem
     * @param node wierzchołek, którego połączone krawędzie mają być zwrócone
     * @return lista wierzchołków połączonych z danym wierzchołkiem
     */
    public List<Node> getAllNodesConnectedTo(Node node) {
        if (!nodeExists(node)) {
            throw new GraphException("Węzeł nie znajduje się w grafie");
        }
        return edges.stream()
                .filter(edge -> edge.edgeContainsNode(node))
                .map(edge -> edge.getNotCalledNode(node))
                .collect(Collectors.toList());
    }

    /**
     * Metoda zwracająca wszystkie wierzchołki(kraje), które znajdują się w danym państwie
     * @param country państwo
     * @return lista wierzchołków należących do danego państwa
     */
    public List<Node> getAllNodesInCountry(Country country) {
        return nodes.stream()
                .filter(node -> node.getCountry().equals(country))
                .collect(Collectors.toList());
    }

    /**
     * Metoda, która sprawdza, czy graf zawiera dany wierzchołek
     * @param node wierzchołek do sprawdzenia
     * @return true/false - zawiera/nie zawiera
     */
    public boolean nodeExists(Node node) {
        return nodes.contains(node);
    }

    /**
     * Metoda która dodaje krawędź  grafu ze środkiem transportu samochód, a co za tym idzie- tworzy krawędź zawierająca dwa wierzchołki
     * @param firstNode 1. wierzchołek
     * @param secondNode 2.wierzchołek
     */
    public void addEdge(Node firstNode, Node secondNode) {
        addEdge(firstNode, secondNode, MeansOfTransport.CAR);
    }

    /**
     * Metoda która dodaje krawędź  grafu , a co za tym idzie- tworzy krawędź zawierająca dwa wierzchołki
     * @param firstNode 1. wierzchołek
     * @param secondNode 2.wierzchołek
     * @param transport środek transportu
     * @exception GraphException wyjątek, kiedy graf nie zawiera jakiegoś wierzchołka
     */
    public void addEdge(Node firstNode, Node secondNode, MeansOfTransport transport) {
        if (!nodeExists(firstNode) || !nodeExists(secondNode)) {
            throw new GraphException("Dane węzły nie znajdują się w grafie");
        }
        int oldSize = edges.size();
        edges.add(new Edge(firstNode, secondNode, transport));
        edges = new HashSet<>(edges);
        support.firePropertyChange("edgesSize", oldSize ,edges.size());
    }

    /**
     * Metoda zwracająca wszystkie krawędzie składowe grafu
     * @return zbiór krawędzi
     */
    public Set<Edge> getAllEdges() {
        return edges;
    }

    /**
     * Metoda rysująca graf, a dokładniej wszystkie krawędzie i wierzchołki składowe grafu
     * @param g2d Dwuwymiarowy kontekst graficzny
     */
    public void drawGraph(Graphics2D g2d) {
        edges.stream()
                .forEach(edge -> edge.draw(g2d));
        nodes.stream()
                .forEach(node -> node.paint(g2d));
    }

    /**
     * Metoda umożliwiająca obserwatorowi rozpoczęcie nasłuchiwania grafu
     * @param pcl obserwator
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    /**
     * Metoda umożliwiająca obserwatorowi przestanie nasłuchiwania grafu
     * @param pcl obserwator
     */
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    /**
     * Metoda zwracająca wszystkie wierzchołki grafu
     * @return pole nodes
     */
    public List<Node> getAllNodes() {
        return nodes;
    }

    /**
     * Metoda wykonująca sie, kiedy obiekt jako obserwator zauwazy zmianę obserwowanego obiektu
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object oldValue = evt.getOldValue();
        Object newValue = evt.getNewValue();
        support.firePropertyChange("newName", oldValue, newValue);
    }

    /**
     * Metoda wykonująca algorytm przeszukiwania wszerz, i zwracająca graf, który zawiera najkrótszą droge z punktu A do punktu B
     * <br><br>
     * <b>Uwaga!</b> graf nie rozróżnia typów środka transportu, czy dystansu między sąsiednimi miastami, więc wyszukiwanie należy rozumieć jako najkrótsza liczba miast potrzebnych do osiągnięcia celu
     * <br>
     * <br>
     * Inspiracja na realizacje algorytmu zaczerpnięta z książki <i>Algorytmy. Ilustrowany przewodnik, A. Bhargava</i>
     * @param startNode wierzchołek początkowy
     * @param searchedNode wierzchołek szukany
     * @return graf zawierający najkrótszą drogę
     * @exception GraphException wyjąkek rzucany, gdy któryś z węzłow nie znajduje sie w grafie, lub najkrótsza droga nie istnieje
     */
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
