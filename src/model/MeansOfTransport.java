package model;

import java.awt.*;

public enum MeansOfTransport {
    CAR("Samochód", Color.BLACK),
    TRAIN("Pociąg", Color.GREEN),
    SHIP("Statek", Color.BLUE),
    PLANE("Samolot", Color.CYAN);

    private final String description;
    private final Color color;
    MeansOfTransport(String description, Color color) {
        this.description = description;
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public Color getColor() {
        return color;
    }

}
