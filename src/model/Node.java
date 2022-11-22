package model;

import java.awt.*;
import java.util.Objects;

public class Node {
    private String name;
    private Color color;
    private int x;
    private int y;

    private int r;
    public Node(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public Node(String name, Color color, int x, int y, int r) {
        this.name = name;
        this.color = color;
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public Node() {
        this("", Color.BLACK);
    }

    public void paint(Graphics2D g2d) {
        g2d.fillOval(x - r, y + r, 2 * r, 2 * r);
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
