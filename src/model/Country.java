package model;

import java.awt.*;
import java.io.Serializable;

public class Country implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private Color color;

    public Country(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public Country() {
        this.name = "-";
        this.color = Color.CYAN;
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

    public String getRGBColor() {
        return String.format("[%d, %d, %d]",color.getRed(), color.getGreen(), color.getBlue());
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        if (getName() != null ? !getName().equals(country.getName()) : country.getName() != null) return false;
        return getColor() != null ? getColor().equals(country.getColor()) : country.getColor() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getColor() != null ? getColor().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name;
    }
}
