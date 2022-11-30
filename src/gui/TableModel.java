/*
 *  Laboratorium 4
 *
 *   Autor: Michal Maziarz, 263913
 *    Data: listopad 2022 r.
 */
package gui;

import model.Country;
import model.Graph;
import model.Node;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashSet;

public class TableModel extends DefaultTableModel {

    public static final String[] CITIES_HEADERS = new String[]{
            "Miasto",
            "Kraj",
            "Połączenia"
    };

    public static final String[] COUNTRIES_HEADERS = new String[]{
            "Kraj",
            "Kolor",
            "Miasta"
    };

    public TableModel(Graph graph) {
        super(CITIES_HEADERS, 0);
        for (Node node : graph.getAllNodes()) {
            addRow(new Object[]{node.getName(), node.getCountry(), graph.getAllNodesConnectedTo(node)});
        }
    }

    public TableModel(Graph graph, ArrayList<Country> countries) {
        super(COUNTRIES_HEADERS, 0);
        for (Country country : countries) {
            addRow(new Object[] {country.getName(), country.getRGBColor(), graph.getAllNodesInCountry(country)});
        }
    }


    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}

