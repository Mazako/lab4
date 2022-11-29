package gui;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class MainFrame extends JFrame implements ActionListener {

    public static final String ABOUT = "Laboratorium 4\n" +
                                        "Program do edycji grafów w postaci miast i połączeń między nimi\n" +
                                        "Autor: Michał Maziarz, 263 913\n" +
                                        "Data: Listopad 2022\n" +
                                        "Wersja JDK: 11";
    //Proszę mi wierzyć, że \t nie działo
    public static final String INSTRUCTION = "Instrukcja obsługi:\n" +
            "1. Poruszanie, zmiana rozmiarów\n" +
            "       -LPM na planszy- poruszanie planszą\n" +
            "       -LPM na grafie- poruszanie grafem\n" +
            "       -LPM na grafie + ruch kółkiem myszy- zmiana rozmiaru grafu\n" +
            "       -LPM na krawędzi- poruszanie krawedzi\n" +
            "       -LPM na krawędzi + ruch kółkiem myszy- zmiana rozmiaru krawędzi\n" +
            "       -Strzałki- ruch całym grafem\n" +
            "       -Del (jeśli kursor wskazuje na graf/krawędź)- usuwanie\n" +
            "2. Edycja:\n" +
            "       -PPM na grafie- opcje edycji grafu\n" +
            "       -PPM na krawędź- opcje edycji krawędzi\n" +
            "       -PPM na nic: opcja dodawania wierzchołka\n" +
            "3. BFS\n" +
            "       -Program umożliwia wyszukiwanie najkrótszej drogi miedzy miastami za pomocą algorytmu przeszukiwania grafu wszerz.\n" +
            "       Algorytm nie uwgzględnia realnych odległości, oraz typu transportu. Zakładamy że szuka\n" +
            "       najkrótszej drogi w pojęciu najmniejszej Ilości miast do odwiedzenia.";

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
        paintPanel.requestFocus();
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
        Country sweeden = new Country("Szwecja", Color.MAGENTA);
        Country gB = new Country("Wielka Brytania", Color.yellow);
        Country brazil = new Country("Brazylia", Color.GREEN);
        Country chechia = new Country("Czechy", Color.PINK);
        Country undefined = new Country();
        countries.add(poland);
        countries.add(germany);
        countries.add(sweeden);
        countries.add(gB);
        countries.add(brazil);
        countries.add(chechia);
        countries.add(undefined);
        Node node1 = new Node("Warszawa", poland, 634, 311, 30);
        Node node2 = new Node("Wrocław", poland, 465, 417, 30);
        Node node3 = new Node("Berlin", germany, 283, 306, 30);
        Node monachium = new Node("Monachium", germany, 67, 321,30);
        Node london = new Node("Londyn", gB, 58, 67,50);
        Node saoPaolo = new Node("Sao Paolo", brazil, 87, 623,45);
        Node prague = new Node("Praga", chechia, 545, 545,35);
        Node bydgoszcz = new Node("Bydgoszcz", poland, 403, 206,30);
        Node choroszcz = new Node("Choroszcz", undefined, 571, 95, 43);
        Node stockholm = new Node("Sztokholm", sweeden, 359, 54, 31);
        graph.add(node1);
        graph.add(node2);
        graph.add(node3);
        graph.add(monachium);
        graph.add(london);
        graph.add(saoPaolo);
        graph.add(prague);
        graph.add(bydgoszcz);
        graph.add(choroszcz);
        graph.add(stockholm);
        graph.addEdge(node1, node2);
        graph.addEdge(node1, node3);
        graph.addEdge(node2, node3);
        graph.addEdge(stockholm, choroszcz, MeansOfTransport.PLANE);
        graph.addEdge(stockholm, london, MeansOfTransport.SHIP);
        graph.addEdge(london, monachium, MeansOfTransport.SHIP);
        graph.addEdge(london, node3, MeansOfTransport.PLANE);
        graph.addEdge(node3, stockholm, MeansOfTransport.SHIP);
        graph.addEdge(stockholm, node1, MeansOfTransport.SHIP);
        graph.addEdge(monachium, node3, MeansOfTransport.CAR);
        graph.addEdge(monachium, saoPaolo, MeansOfTransport.PLANE);
        graph.addEdge(monachium, prague, MeansOfTransport.CAR);
        graph.addEdge(node2, prague, MeansOfTransport.TRAIN);
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
            } else if (source == helpMenuItem) {
                JOptionPane.showMessageDialog(this,
                        INSTRUCTION,
                        "Instrukcja obsługi",
                        JOptionPane.INFORMATION_MESSAGE
                );
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

