package model;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Objects;

public class Node implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final int MIN_RADIUS = 35;
    public static final int MAX_RADIUS = 100;
    private String name;
    private Country country;
    private int x;
    private int y;
    private int r;

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


    public void paint(Graphics2D g2d) {
        g2d.setColor(country.getColor());
        g2d.fillOval(x - r, y - r, 2 * r, 2 * r);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x - r, y - r, 2 * r, 2 * r);
        g2d.drawString(name, x - (name.length()*4), y);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }


    public boolean isMouseOver(int mx, int my){
        return (x-mx)*(x-mx)+(y-my)*(y-my)<=r*r;
    }

    public void move(int x, int y) {
        setX(getX() + x);
        setY(getY() + y);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        support.firePropertyChange("changedName",oldName, name);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = Math.max(MIN_RADIUS, r);
        this.r = Math.min(MAX_RADIUS, this.r);
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        Country prevCountry = this.country;
        this.country = country;
        support.firePropertyChange("changeCountry", prevCountry, country);
    }

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

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getCountry().hashCode();
        result = 31 * result + getX();
        result = 31 * result + getY();
        result = 31 * result + getR();
        return result;
    }

    public void increaseSize(int dr) {
        this.setR(this.getR() + dr);
    }

    @Override
    public String toString() {
        return name;
    }
}
