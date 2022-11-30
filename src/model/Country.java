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
 *  Klasa reprezentująca państwo, do którego należy dane miasto w grafie
 *
 * @author Michał Maziarz
 * @version listopad 2022
 */
public class Country implements Serializable {

    /**
     * Pole oznaczające ID wersji klasy do serializacji obiektu
     */
    private static final long serialVersionUID = 1L;

    /**
     * Nazwa kraju
     */
    private String name;

    /**
     * Kolor kraju. Miasta które należą do tych państw będą miały wierzchołek tego koloru
     */
    private Color color;

    /**
     * Konstruktor klasy
     * @param name nazwa kraju
     * @param color kolor kraju
     */
    public Country(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Konstruktor niezdefiniowanego kraju <br>
     * <b>Nazwa</b> ustawiana jest jako - <br>
     * <b>Kolor</b> jest ustawiany jako niebieski <br>
     */
    public Country() {
        this.name = "-";
        this.color = Color.CYAN;
    }

    /**
     * Metoda zwraca nazwę kraju
     *
     * @return pole <i>name</i>
     */
    public String getName() {
        return name;
    }

    /**
     * Metoda ustawiająca nazwę kraju
     * @param name nazwa kraju
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Metoda zwracająca kolor kraju
     * @return pole <i>color</i>
     */
    public Color getColor() {
        return color;
    }

    /**
     * Metoda zwracająca kolor kraju w formie RGB
     * @return napis w postaci "[czerwony, zielony, niebieski]"
     */
    public String getRGBColor() {
        return String.format("[%d, %d, %d]",color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Metoda ustawiająca kolor państwa
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Metoda porównująca dwa państwa
     * @param o obiekt porównywany
     * @return true- obiekty są równe, false- obiekty są różne
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        if (getName() != null ? !getName().equals(country.getName()) : country.getName() != null) return false;
        return getColor() != null ? getColor().equals(country.getColor()) : country.getColor() == null;
    }

    /**
     * Funkcja skrótu państwa
     * @return hashCode państwa
     */
    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getColor() != null ? getColor().hashCode() : 0);
        return result;
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
