package model;

import java.awt.*;
import java.util.Objects;

public class Node {
    private String name;
    private Color color;
    private double x;
    private double y;

    public Node(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public Node(String name, Color color, double x, double y) {
        this.name = name;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public Node() {
        this("", Color.BLACK);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Double.compare(node.x, x) == 0 && Double.compare(node.y, y) == 0 && Objects.equals(name, node.name) && Objects.equals(color, node.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color, x, y);
    }
}
