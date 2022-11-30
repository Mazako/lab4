/*
 *  Laboratorium 4
 *
 *   Autor: Michal Maziarz, 263913
 *    Data: listopad 2022 r.
 */
package model;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Objects;

/**
 * Klasa reprezentująca wierzchołek grafu, czyli miasto
 * @author Michał Maziarz
 * @version Listopad 2022
 */
public class Node implements Serializable {

    /**
     * Pole oznaczające ID wersji klasy do serializacji obiektu
     */
    private static final long serialVersionUID = 1L;

    /**
     * Pole reprezentujące minimalny mozliwy promień wierzchołka
     */
    public static final int MIN_RADIUS = 35;

    /**
     * Pole reprezentujące maksymalny promień wierzchołka
     */
    public static final int MAX_RADIUS = 100;

    /**
     * Pole reprezentujące nazwę wierzchołka
     */
    private String name;

    /**
     * pole reprezentujące kraj, do którego należy miasto
     */
    private Country country;

    /**
     * Pozycja X wierzchołka
     */
    private int x;

    /**
     * Pozycja Y wierzchołka
     */
    private int y;

    /**
     * Promień wierzchołka
     */
    private int r;

    /**
     * słuchacz dla wzorca obserwator. pozwala nasłuchiwanie zmian w obiekcie przez obserwatorów
     */
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public Node(String name, Country country, int x, int y, int r) {
        this.name = name;
        this.country = country;
        this.x = x;
        this.y = y;
        this.r =  Math.max(MIN_RADIUS, r);
        this.r = Math.min(MAX_RADIUS, this.r);
    }

    public Node(Node node) {
        this(node.name, node.country, node.x, node.y, node.r);
    }

    public Node (int x, int y) {
        this("", new Country(), x, y, MIN_RADIUS);
    }


    /**
     * Metoda rysująca wierzchołek na planszy
     * @param g2d Dwuwymiarowy kontekst graficzny
     */
    public void paint(Graphics2D g2d) {
        g2d.setColor(country.getColor());
        g2d.fillOval(x - r, y - r, 2 * r, 2 * r);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x - r, y - r, 2 * r, 2 * r);
        g2d.drawString(name, x - (name.length()*4), y);
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
     * Metoda sprawdzająca, czy mysz wskazuje na wierzchołek grafu. Wykorzystano geometrię analityczną i równanie kola na układzie współrzędnych
     * @param mx x punktu, na który wskazuje mysz
     * @param my y punktu, na który wskazuje mysz
     * @return true/false - mysz wskazuje/ nie wskazuje na wierzchołek
     */
    public boolean isMouseOver(int mx, int my){
        return (x-mx)*(x-mx)+(y-my)*(y-my)<=r*r;
    }

    /**
     * Metoda zmieniająca położenia wierzchołka
     * @param x przyrost x
     * @param y przyrost y
     */
    public void move(int x, int y) {
        setX(getX() + x);
        setY(getY() + y);
    }

    /**
     * Metoda zwracająca nazwę kraju
     * @return nazwa kraju
     */
    public String getName() {
        return name;
    }

    /**
     * Metoda ustawiająca nazwę kraju
     * @param name nazwa kraju do ustawienia
     */
    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        support.firePropertyChange("changedName",oldName, name);
    }

    /**
     *
     * @return współrzędna X wierzchołka
     */
    public int getX() {
        return x;
    }

    /**
     * Metoda ustawiająca współrzędną X wierzchołka
     * @param x nowa współrzędna x wierzchołka
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     *
     * @return współrzędna Y wierzchołka
     */
    public int getY() {
        return y;
    }

    /**
     * Metoda ustawiająca współrzędną y wierzchołka
     * @param y nowa współrzędna y wierzchołka
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     *
     * @return promień wierzchołka
     */
    public int getR() {
        return r;
    }

    /**
     * Metoda ustawiająca promień wierzchołka
     * @param r nowy promień wierzchołka
     */
    public void setR(int r) {
        this.r = Math.max(MIN_RADIUS, r);
        this.r = Math.min(MAX_RADIUS, this.r);
    }

    /**
     *
     * @return kraj, do którego należy wierzchołek
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Metoda ustawiająca kraj wierzchołka
     * @param country nowy kraj
     */
    public void setCountry(Country country) {
        Country prevCountry = this.country;
        this.country = country;
        support.firePropertyChange("changeCountry", prevCountry, country);
    }

    /**
     * Metoda porównująca dwa wierzchołki
     * @param o obiekt porównywany
     * @return true- obiekty są równe, false- obiekty są różne
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (getX() != node.getX()) return false;
        if (getY() != node.getY()) return false;
        if (getR() != node.getR()) return false;
        if (!getName().equals(node.getName())) return false;
        return getCountry().equals(node.getCountry());
    }

    /**
     * Funkcja skrótu wierzchołka
     * @return hashCode wierzchołka
     */
    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getCountry().hashCode();
        result = 31 * result + getX();
        result = 31 * result + getY();
        result = 31 * result + getR();
        return result;
    }

    /**
     * Metoda zwiększająca promień wierzchołka
     * @param dr przyrost długości promienia
     */
    public void increaseSize(int dr) {
        this.setR(this.getR() + dr);
    }

    /**
     * Metoda zwracająca reprezentacje obiektu jako String
     * @return reprezentacja String obiektu (tylko nazwa)
     */
    @Override
    public String toString() {
        return name;
    }
}
