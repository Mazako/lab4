package gui;

import model.Country;
import model.Graph;
import model.GraphException;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class Table extends JScrollPane {

    private JTable jTable;
    private TableModel tableModel;

    public Table(Graph graph, int width, int height) {
        tableModel = new TableModel(graph);
        jTable = new JTable(tableModel);
        this.setViewportView(jTable);
        jTable.setFillsViewportHeight(true);
        jTable.setRowSelectionAllowed(true);
        setCorrectWidth(70, 70, 500);
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.setMaximumSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));

    }

    public Table(Graph graph, ArrayList<Country> countries, int width, int height) {
        tableModel = new TableModel(graph, countries);
        jTable = new JTable(tableModel);
        this.setViewportView(jTable);
        jTable.setFillsViewportHeight(true);
        jTable.setRowSelectionAllowed(true);
        setCorrectWidth(70, 90, 300);
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.setMaximumSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));


    }

    public void refreshView(Graph graph) {
        tableModel = new TableModel(graph);
        jTable.setModel(tableModel);
        setCorrectWidth(70, 70, 500);
    }

    public void refreshView(Graph graph, ArrayList<Country> countries) {
        tableModel = new TableModel(graph, countries);
        jTable.setModel(tableModel);
        setCorrectWidth(70, 90, 300);
    }

    private void setCorrectWidth(int zero, int one, int two) {
        jTable.getColumnModel().getColumn(0).setPreferredWidth(zero);
        jTable.getColumnModel().getColumn(1).setPreferredWidth(one);
        jTable.getColumnModel().getColumn(2).setPreferredWidth(two);
    }

    public int getSelectedRow() {
        int selectedRow = jTable.getSelectedRow();
        if (selectedRow < 0) {
            throw new GraphException("Nie wybrano żadnego rzędu");
        }
        return selectedRow;
    }


}
