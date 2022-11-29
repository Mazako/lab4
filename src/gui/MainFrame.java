package gui;

import model.Country;
import model.Graph;
import model.GraphException;
import model.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class MainFrame extends JFrame implements ActionListener {

    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("Plik");
        JMenuItem newMenuItem = new JMenuItem("Nowy");
        JMenuItem openGraphMenuItem = new JMenuItem("Wczytaj graf");
        JMenuItem saveGraphMenuItem = new JMenuItem("Zapisz graf");
        JMenuItem exampleGraphMenuItem = new JMenuItem("Przykładowy graf");
        JMenuItem exitMenuItem = new JMenuItem("Wyjdź");
    JMenu help = new JMenu("Pomoc");
        JMenuItem aboutMenuItem = new JMenuItem("O autorze");
        JMenuItem helpMenuItem = new JMenuItem("Pomoc");
    JMenu graphMenu = new JMenu("Graf");
        JMenuItem BFSMenuItem = new JMenuItem("Wyszukaj najkrótszą drogę");

    private Graph graph = new Graph();

    private ArrayList<Country> countries = new ArrayList<>();

    public static final int WINDOW_WIDTH = 1100;
    public static final int WINDOW_HEIGHT = 750;
    private final PaintFrame paintPanel;
    private ListPanel listPanel;

    public static final String ABOUT = "Laboratorium 4\n" +
                                        "Program do edycji grafów w postaci miast i połączeń między nimi\n" +
                                        "Autor: Michał Maziarz, 263 913\n" +
                                        "Data: Listopad 2022";


    public MainFrame() {
        createInitData();
        paintPanel = new PaintFrame(graph, countries);
        listPanel = new ListPanel(graph, countries);
        listPanel.addPropertyChangeListener(paintPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Mapa miast i połączeń");
        this.setResizable(false);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        paintPanel.setBounds(400,0, 700, 750);
        listPanel.setBounds(0, 0, 400, 750);
        initMenuBar();
        this.setJMenuBar(menuBar);
        this.add(paintPanel);
        this.add(listPanel);
        this.setVisible(true);
    }

    private void initMenuBar() {
        for (JMenuItem item : new JMenuItem[]{
                newMenuItem,
                openGraphMenuItem,
                saveGraphMenuItem,
                exampleGraphMenuItem,
                exitMenuItem
        }) {
            item.addActionListener(this);
            fileMenu.add(item);
        }
        menuBar.add(fileMenu);

        help.add(aboutMenuItem);
        aboutMenuItem.addActionListener(this);
        help.add(helpMenuItem);
        helpMenuItem.addActionListener(this);

        graphMenu.add(BFSMenuItem);
        BFSMenuItem.addActionListener(this);
        menuBar.add(graphMenu);
        menuBar.add(help);
    }

    private void createInitData() {
        countries = new ArrayList<>();
        graph = new Graph();
        Country poland = new Country("Polska", Color.RED);
        Country germany = new Country("Niemcy", Color.LIGHT_GRAY);
        Country undefined = new Country();
        countries.add(poland);
        countries.add(germany);
        countries.add(undefined);
        Node node1 = new Node("Warszawa", poland, 100, 100, 30);
        Node node2 = new Node("Wrocław", poland, 200, 100, 30);
        Node node3 = new Node("Berlin", germany, 400, 500, 30);
        graph.add(node1);
        graph.add(node2);
        graph.add(node3);
        graph.addEdge(node1, node2);
        graph.addEdge(node1, node3);
        graph.addEdge(node2, node3);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        try {
            if (source == newMenuItem) {
                graph = new Graph();
                countries = new ArrayList<>();
                countries.add(new Country());
                paintPanel.setCountries(countries);
                paintPanel.setGraph(graph);
                listPanel.setCountries(countries);
                listPanel.setGraph(graph);
            } else if (source == exampleGraphMenuItem) {
                createInitData();
                paintPanel.setGraph(graph);
                paintPanel.setCountries(countries);
                listPanel.setGraph(graph);
                listPanel.setCountries(countries);
            } else if (source == saveGraphMenuItem) {
                saveGraph();
            } else if (source == openGraphMenuItem) {
                openGraph();
            } else if (source == exitMenuItem) {
                System.exit(0);
            } else if (source == aboutMenuItem) {
                JOptionPane.showMessageDialog(this,
                        ABOUT,
                        "O autorze",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else if (source == BFSMenuItem) {
                BFS();
            }
        } catch (GraphException ex) {
            JOptionPane.showMessageDialog(this,ex.getMessage(), "BŁĄD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void BFS() {
        Node node = searchNode(graph, "Wybierz miasto początkowe");
        Node searchNode = searchNode(graph, "Wybierz miasto docelowe");
        if (node == null || searchNode == null) {
            return;
        }
        Graph bfs = graph.BFS(node, searchNode);
        JDialog dialog = new JDialog();
        BFSView bfsView = new BFSView(bfs);
        dialog.add(bfsView);
        dialog.setSize(700, 750);
        dialog.setTitle("BFS");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    private Node searchNode(Graph graph, String title) {
        return (Node) JOptionPane.showInputDialog(this,
                title,
                "",
                JOptionPane.INFORMATION_MESSAGE,
                null,
                graph.getAllNodes().toArray(),
                graph.getAllNodes().get(0)
        );
    }

    private void openGraph() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(new File("./"));
        jFileChooser.showSaveDialog(this);
        File selectedFile = jFileChooser.getSelectedFile();
        if (selectedFile == null) {
            return;
        }
        graph = Graph.readGraphFromFile(selectedFile);
        countries = graph.getAllCountries();
        if(!countries.contains(new Country())) {
            countries.add(new Country());
        }
        paintPanel.setGraph(graph);
        paintPanel.setCountries(countries);
        listPanel.setGraph(graph);
        listPanel.setCountries(countries);
        JOptionPane.showMessageDialog(this,
                "Pomyślnie wczytano graf z pliku: " + selectedFile,
                "Sukces",
                JOptionPane.INFORMATION_MESSAGE
        );

    }

    private void saveGraph() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(new File("./"));
        jFileChooser.showSaveDialog(this);
        File selectedFile = jFileChooser.getSelectedFile();
        if (selectedFile == null) {
            return;
        }
        Graph.saveGraphToFile(selectedFile, graph);
        JOptionPane.showMessageDialog(this,
                "Pomyslnie zapisano graf",
                "Sukces",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}

