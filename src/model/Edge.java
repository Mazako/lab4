/*
 *  Laboratorium 4
 *
 *   Autor: Michal Maziarz, 263913
 *    Data: listopad 2022 r.
 */
package model;

import java.awt.*;
import java.io.Serializable;

/**
 * Klasa reprezentująca krawędź grafu
 *
 * @author Michał Maziarz
 * @version listopad 2022
 */
public class Edge implements Serializable {

    /**
     * Pole oznaczające ID wersji klasy do serializacji obiektu
     */
    private static final long serialVersionUID = 1L;

    /**
     * Pierwszy wierzchołek krawędzi
     */
    private Node firstNode;

    /**
     * Drugi wierzchołek krawędzi
     */
    private Node secondNode;

    /**
     * Typ środka transportu, pole to ma odzwierciedlenie w graficznej reprezentacji krawędzi
     */
    private MeansOfTransport transport;

    /**
     * Konstruktor krawędzi grafu
     * @param firstNode pierwszy wierzchołek
     * @param secondNode drugi wierzchołek
     * @param transport środek transportu
     */
    public Edge(Node firstNode, Node secondNode, MeansOfTransport transport) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.transport = transport;
    }

    /**
     * Metoda zwracająca wierzchołek krawędzi, który nie jest podany jako parametr funkcji
     * @param node wierzchołek pierwszy
     * @return wierzcholek drugi, lub null, jeśli krawędź nie składa się z wierzchołka
     */
    public Node getNotCalledNode(Node node) {
        if (node.equals(firstNode)) {
            return secondNode;
        } else if (node.equals(secondNode)) {
            return firstNode;
        } else {
            return null;
        }
    }

    /**
     * Metoda sprawdzająca, czy krawędź zawiera wierzchołek
     * @param node wierzchołek
     * @return true, jeśli krawędź zawiera wierzchołek, lub false- jeśli nie
     */
    public boolean edgeContainsNode(Node node) {
        return node.equals(firstNode) || node.equals(secondNode);
    }

    /**
     * Metoda rysująca krawędź
     * @param g2d Dwuwymiarowy kontekst graficzny
     */
    public void draw(Graphics2D g2d) {
        g2d.setColor(transport.getColor());
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(firstNode.getX(), firstNode.getY(), secondNode.getX(), secondNode.getY());
    }

    /**
     * Metoda sprawdzająca, czy mysz wskazuje na krawędź w graficznej reprezentacji.<br><br>
     * Metoda tworzy prostą na podstawie koordynatów wiechołków, i sprawdza, czy koordynaty myszy<br>
     * znajdują sie z małą niepewnością na prostej
     * @param x punkt x myszy
     * @param y punkt y myszy
     * @return true- mysz wskazuje na krawędź, lub false
     */
    public boolean isMouseOver(int x, int y) {
        double[] line = getLine();
        double a = line[0];
        double b = line[1];
        double mouseY = (a * x + b);
        return (Math.abs(mouseY - y) <= 10);
    }

    /**
     * Metoda zwracająca współczynnik a, i b prostej, która zawiera się w punktach wierzchołków składowych krawędzi<br>
     * Sposób liczenia współczynników- metoda Cramera
     * @return
     */
    private double[] getLine() {
        int x1 = firstNode.getX();
        int y1 = firstNode.getY();
        int y2 = secondNode.getY();
        int x2 = secondNode.getX();
        double a = (double)(y1 - y2) / (x1-x2);
        double b = (double)((x1*y2) - (x2*y1))/(x1-x2);
        return new double[]{a, b};
    }

    /**
     * Metoda zmieniająca rozmiar krawędzi (licząc od środka krawędzi)
     * @param size rozmiar o ile ma sie zmiejszyć(size &gt; 0) lub zwiększyć(size &lt; 0) krawędź
     */
    public void changeLength(double size) {
        double[] line = getLine();
        double a = line[0];
        double b = line[1];
        int Sx = (firstNode.getX() + secondNode.getX()) / 2;
        int Sy = (firstNode.getY() + secondNode.getY()) / 2;
        changePoint(firstNode, size, Sx, Sy, a, b);
        changePoint(secondNode, size, Sx, Sy, a, b);
    }

    /**
     * Metoda zmieniająca koordynaty wierzchołka grafu tak, aby zgodnie z wolą metody <i>changeLength</i> rozmiar połowy krawędzi został zmieniony
     * <br>
     * <br>
     * Metoda liczenia: rozwiązanie równania stara długość - o ile zmienić rozmiar = długość odcinka od środka krawędzi do wierzchołka
     * <br>
     * <br>
     * Metoda nic nie robi, jesli chcemy mniejszyć rozmiar od środka krawędzi o mniej niż 50
     * @param node wierzchołek będący w danej połowie krawędzi
     * @param size przyrost długośći krawedzi
     * @param sx X środka odcinka
     * @param sy Y środka odcinka
     * @param a współczynnik a prostej krawędzi
     * @param b współczynnik b prostej krawędzi
     */
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

    /**
     * Metoda licząca długość odcinka miedzy dwoma punktami na płaszczyźnie dwuwymiarowej<br>
     * <center>distance = sqrt((x1-x2)^2 + (y1-y2)^2)</center>
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return długość odcinka
     */
    private double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    /**
     * Metoda zwracająca pierwszy wierzchołek składowy krawędzi
     * @return pierwszy wierzchołek krawędzi
     */
    public Node getFirstNode() {
        return firstNode;
    }

    /**
     * Metoda ustawiająca pierwszy wierzchołek krawędzi
     * @param firstNode wierzcholek
     */
    public void setFirstNode(Node firstNode) {
        this.firstNode = firstNode;
    }

    /**
     * Metoda zwracająca drugi wierzchołek składowy krawędzi
     * @return drugi wierzchołek krawędzi
     */
    public Node getSecondNode() {
        return secondNode;
    }

    /**
     * Metoda ustawiająca pierwszy wierzchołek krawędzi
     * @param secondNode wierzcholek
     */
    public void setSecondNode(Node secondNode) {
        this.secondNode = secondNode;
    }

    /**
     * Metoda zwracająca środek transportu krawędzi
     * @return środek transportu
     */
    public MeansOfTransport getTransport() {
        return transport;
    }

    /**
     * Metoda ustawiająca środek transportu krawędzi
     * @param transport
     */
    public void setTransport(MeansOfTransport transport) {
        this.transport = transport;
    }

    /**
     * Metoda porównująca dwie krawędzie, czy są równe
     * <br>
     * <br>
     * <b>Uwaga!</b> kolejnośc wierzchołków składowych krawędzi jest nierozróżnialna, więc krawędź(A,B) == krawędź(B,A)
     * <br>
     * Dodatkowo, z racji tego, ze w grafie może istnieć tylko jedno połaczenie miedzy dwoma miastami, to pole środka transportu<br>
     * nie jest porównywane
     * @param o obiekt porównywany
     * @return true- krawędzie są równe false- nie są równe
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (!getFirstNode().equals(edge.getFirstNode()) && !getFirstNode().equals(edge.secondNode)) return false;
        return getSecondNode().equals(edge.getSecondNode()) || getSecondNode().equals(edge.getFirstNode());
    }

    /**
     * Funkcja skrótu krawędzi
     * @return hashCode krawędzi
     */
    @Override
    public int hashCode() {
        return firstNode.hashCode() + secondNode.hashCode();
    }
}
