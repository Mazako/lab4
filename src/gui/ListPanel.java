package gui;

import model.Country;
import model.Graph;
import model.GraphException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class ListPanel extends JPanel implements PropertyChangeListener, ActionListener {
    private Graph graph;
    private ArrayList<Country> countries;
    private final JButton addCountryButton = new JButton("Dodaj kraj");
    private final JButton removeCountryButton = new JButton("Usuń kraj");
    private final JButton changeColorButton = new JButton("Zmień kolor");
    PropertyChangeSupport support = new PropertyChangeSupport(this);
    Table citiesTable;
    Table countriesTable;
    public ListPanel(Graph graph, ArrayList<Country> countrySet) {
        this.graph = graph;
        this.countries = countrySet;
        graph.addPropertyChangeListener(this);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        citiesTable = new Table(graph, 380, 300);
        countriesTable = new Table(graph, countrySet, 380, 200);
        initButtons();
        this.add(citiesTable);
        this.add(countriesTable);
        this.add(addCountryButton);
        this.add(removeCountryButton);
        this.add(changeColorButton);

    }

    private void initButtons() {
        addCountryButton.addActionListener(this);
        addCountryButton.setPreferredSize(new Dimension(380, 40));
        removeCountryButton.addActionListener(this);
        removeCountryButton.setPreferredSize(new Dimension(380, 40));

        changeColorButton.addActionListener(this);
        changeColorButton.setPreferredSize(new Dimension(380, 40));
    }

    public void setGraph(Graph g) {
        this.graph.removePropertyChangeListener(this);
        this.graph = g;
        this.graph.addPropertyChangeListener(this);
        citiesTable.refreshView(graph);
    }

    public void setCountries(ArrayList<Country> countries) {
        this.countries = countries;
        countriesTable.refreshView(graph, countries);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
       citiesTable.refreshView(graph);
       countriesTable.refreshView(graph, countries);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        try {
            if (source == addCountryButton) {
                String name = JOptionPane.showInputDialog(
                        this,
                        "Wpisz nazwę kraju",
                        "Dodawanie kraju",
                        JOptionPane.INFORMATION_MESSAGE
                );
                if (name == null || name.isEmpty()) {
                    return;
                }
                Color color = JColorChooser.showDialog(this, "Wybierz color", null);
                if (color == null) {
                    return;
                }
                Country country = new Country(name, color);
                countries.add(country);

            } else if (source == removeCountryButton) {
                Country undefinedCountry = countries.stream()
                        .filter(country -> country.getName().equals("-"))
                        .findFirst()
                        .get();
                int selectedRow = countriesTable.getSelectedRow();
                Country removedCountry = countries.get(selectedRow);
                if (removedCountry.equals(undefinedCountry)) {
                    throw new GraphException("Nie można usunąć niezdefiniowanego koloru");
                }
                countries.remove(selectedRow);
                graph.getAllNodesInCountry(removedCountry).stream()
                        .forEach(node -> node.setCountry(undefinedCountry));
                support.firePropertyChange("COLOR", 0, 1);
            } else if (source == changeColorButton) {
                int selectedRow = countriesTable.getSelectedRow();
                Country country = countries.get(selectedRow);
                Color color = JColorChooser.showDialog(this, "Wybierz color", null);
                if (color == null) {
                    return;
                }
                country.setColor(color);
                support.firePropertyChange("COLOR", 0, 1);
            }
        } catch (GraphException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "BŁĄD", JOptionPane.ERROR_MESSAGE);
        }
        countriesTable.refreshView(graph, countries);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
}
