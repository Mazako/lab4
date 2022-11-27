package gui;

import model.Edge;
import model.Graph;
import model.MeansOfTransport;
import model.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PaintFrame extends JPanel implements MouseMotionListener, MouseListener, KeyListener, MouseWheelListener {
    protected Graph graph = new Graph();
    private int mouseX = 0;
    private int mouseY = 0;
    private boolean mouseButtonLeft = false;
    private boolean mouseButtonRight = false;

    private boolean insertingEdge = false;

    private Node insertingNode = null;
    private Node nodeUnderCursor = null;
    private Edge edgeUnderCursor = null;

    public PaintFrame() {
        createInitData();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        this.addMouseWheelListener(this);
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        if (graph != null) {
            graph.drawGraph(g2d);
        }
        if (insertingEdge) {
            g.drawLine(insertingNode.getX(), insertingNode.getY(), mouseX, mouseY);
        }
    }

    private Node findNode(int mx, int my) {
        List<Node> allNodes = graph.getAllNodes();
        return allNodes.stream()
                .filter(node -> node.isMouseOver(mx, my))
                .findFirst()
                .orElse(null);
    }
    private void createInitData() {
        Node node1 = new Node("Warszawa", Color.LIGHT_GRAY, 100, 100, 30);
        Node node2 = new Node("Wrocław", Color.LIGHT_GRAY, 200, 100, 30);
        Node node3 = new Node("Berlin", Color.LIGHT_GRAY, 400, 500, 30);
        graph.add(node1);
        graph.add(node2);
        graph.add(node3);
        graph.addEdge(node1, node2);
        graph.addEdge(node1, node3);
        graph.addEdge(node2, node3);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1)  {
            mouseButtonLeft = true;
            if (insertingEdge && nodeUnderCursor != null) {
                graph.addEdge(nodeUnderCursor, insertingNode);
                insertingEdge = false;
                insertingNode = null;
            }
        }
        if (e.getButton() == 3) {
            mouseButtonRight = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == 1) {
            mouseButtonLeft = false;
        }
        if (e.getButton() == 3) {
            mouseButtonRight = false;
            if (edgeUnderCursor == null && nodeUnderCursor == null) {
                createPopupMenu(e);
            } else if (nodeUnderCursor != null) {
                createPopupMenu(e, nodeUnderCursor);
            }
            else if (edgeUnderCursor != null) {
                createPopupMenu(e, edgeUnderCursor);
            }
        }
    }

    private void createPopupMenu(MouseEvent e, Edge edgeUnderCursor) {
            Edge currentEdge = edgeUnderCursor;
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem changeToCar = new JMenuItem("Transport samochodem");
            JMenuItem changeToTrain = new JMenuItem("Transport pociągiem");
            JMenuItem changeToPlane = new JMenuItem("Transport samolotem");
            JMenuItem changeToShip = new JMenuItem("Transport statkiem");
            changeToCar.addActionListener(action -> {
                currentEdge.setTransport(MeansOfTransport.CAR);
                repaint();
            });
            changeToTrain.addActionListener(action -> {
                currentEdge.setTransport(MeansOfTransport.TRAIN);
                repaint();
            });
            changeToPlane.addActionListener(action -> {
                currentEdge.setTransport(MeansOfTransport.PLANE);
                repaint();
            });
            changeToShip.addActionListener(action -> {
                currentEdge.setTransport(MeansOfTransport.SHIP);
                repaint();
            });
            popupMenu.add(changeToCar);
            popupMenu.add(changeToTrain);
            popupMenu.add(changeToPlane);
            popupMenu.add(changeToShip);
            popupMenu.show(this, e.getX(), e.getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mouseButtonLeft) {
            if (nodeUnderCursor != null) {
                nodeUnderCursor.move(e.getX() - mouseX, e.getY() - mouseY);
            }
            else if (edgeUnderCursor != null) {
                Node firstNode = edgeUnderCursor.getFirstNode();
                Node secondNode = edgeUnderCursor.getSecondNode();
                firstNode.move(e.getX() - mouseX, e.getY() - mouseY);
                secondNode.move(e.getX() - mouseX, e.getY() - mouseY);
            } else {
                this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                graph.getAllNodes().stream()
                        .forEach(node -> node.move(e.getX() - mouseX, e.getY() - mouseY));
            }
        }
        mouseX = e.getX();
        mouseY = e.getY();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        nodeUnderCursor = findNode(mouseX, mouseY);
        edgeUnderCursor = findEdgeOnCoursor(mouseX, mouseY);
        if (nodeUnderCursor != null) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else if (edgeUnderCursor != null) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        } else {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        repaint();
    }

    private Edge findEdgeOnCoursor(int mouseX, int mouseY) {
        return graph.getAllEdges().stream()
                .filter(edge -> edge.isMouseOver(mouseX, mouseY) && nodeUnderCursor == null)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (mouseButtonLeft && nodeUnderCursor != null) {
            if (e.getWheelRotation() == -1) {
                nodeUnderCursor.increaseSize(5);
            } else {
                nodeUnderCursor.increaseSize(-5);
            }
        }
        if (mouseButtonLeft && edgeUnderCursor != null) {
            if (e.getWheelRotation() == -1) {
                edgeUnderCursor.changeLength(-10);
            } else {
                edgeUnderCursor.changeLength(10);
            }
        }
        repaint();
    }

    private void createPopupMenu(MouseEvent e) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem jMenuItem = new JMenuItem("Dodaj nowy wierzchołek");
        jMenuItem.addActionListener(action -> {
            Node addedNode = graph.add(new Node(Color.LIGHT_GRAY, e.getX(), e.getY()));
            setNameOfNode(addedNode);
            repaint();
        });
        popupMenu.add(jMenuItem);
        popupMenu.show(this, e.getX(), e.getY());

    }

    private void createPopupMenu(MouseEvent e, Node node) {
        Node currentNode = nodeUnderCursor;
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem removeNodeMenuItem = new JMenuItem("Usuń wierzchołek");
        JMenuItem addEdgeMenuItem = new JMenuItem("Dodaj krawędź");
        JMenuItem changeNameMenuItem = new JMenuItem("Zmień nazwę");
        removeNodeMenuItem.addActionListener(action -> {
            graph.remove(node);
            repaint();
        });
        addEdgeMenuItem.addActionListener(action -> {
            insertingNode = currentNode;
            insertingEdge = true;
        });
        changeNameMenuItem.addActionListener(action -> {
            setNameOfNode(currentNode);
            repaint();
        });
        popupMenu.add(removeNodeMenuItem);
        popupMenu.add(addEdgeMenuItem);
        popupMenu.add(changeNameMenuItem);
        popupMenu.show(this, e.getX(), e.getY());
    }

    private void setNameOfNode(Node currentNode) {
        String name = JOptionPane.showInputDialog(this,
                "Wpisz nazwę miasta",
                "Wpisywanie nazwy miasta",
                JOptionPane.INFORMATION_MESSAGE
        );
        if (name != null) {
            currentNode.setName(name);
        }
    }
}