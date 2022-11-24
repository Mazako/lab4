package model;

import java.awt.*;
import java.util.Objects;

public class Node {

    public static final int MIN_RADIUS = 35;
    public static final int MAX_RADIUS = 100;
    private String name;
    private Color color;
    private int x;
    private int y;

    private int r;

    public Node(String name, Color color, int x, int y, int r) {
        this.name = name;
        this.color = color;
        this.x = x;
        this.y = y;
        this.r =  Math.max(MIN_RADIUS, r);
        this.r = Math.min(MAX_RADIUS, this.r);
    }


    public void paint(Graphics2D g2d) {
        g2d.setColor(color);
        g2d.fillOval(x - r, y - r, 2 * r, 2 * r);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x - r, y - r, 2 * r, 2 * r);
        g2d.drawString(name, x - (name.length()*4), y);
    }

    public boolean isMouseOver(int mx, int my){
        return (x-mx)*(x-mx)+(y-my)*(y-my)<=r*r;
    }

    public void move(int x, int y) {
        this.x += x;
        this.y += y;
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

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = Math.max(MIN_RADIUS, r);
        this.r = Math.min(MAX_RADIUS, this.r);
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

    public void increaseSize(int dr) {
        this.setR(this.getR() + dr);
    }
}
