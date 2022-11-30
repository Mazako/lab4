package model;

import java.awt.*;

/**
 * Enum reprezentujący środek transportu krawędzi grafu
 * @author Michał Maziarz
 * @version Listopad 2022
 */
public enum MeansOfTransport {
    /**
     * Pole reprezentujące samochód
     */
    CAR("Samochód", Color.BLACK),

    /**
     * Pole reprezentujące pociąg
     */
    TRAIN("Pociąg", Color.GREEN),

    /**
     * Pole reprezentujące statek
     */
    SHIP("Statek", Color.BLUE),

    /**
     * Pole reprezentujące samolot
     */
    PLANE("Samolot", Color.CYAN);

    /**
     * Pole reprezentujące opis środka transportu
     */
    private final String description;

    /**
     * Pole reprezentujące kolor środka transportu reprezentowany na grafie
     */
    private final Color color;
    MeansOfTransport(String description, Color color) {
        this.description = description;
        this.color = color;
    }

    /**
     * Metoda zwracająca opis środka transportu
     * @return opis środka transportu
     */
    public String getDescription() {
        return description;
    }

    /**
     * Metoda zwracająca kolor transportu
     * @return kolor środka transportu
     */
    public Color getColor() {
        return color;
    }

}
