/*
 *  Laboratorium 4
 *
 *   Autor: Michal Maziarz, 263913
 *    Data: listopad 2022 r.
 */
package gui;

import model.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class BFSView extends JPanel implements MouseListener, MouseMotionListener {

    private Graph graph;

    private boolean leftButton = false;

    int mx;
    int my;

    BFSView(Graph graph) {
        this.graph = graph;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        if (graph != null) {
            graph.drawGraph(g2d);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1) {
            leftButton = true;
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == 1) {
            leftButton = false;
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (leftButton) {
            graph.getAllNodes()
                    .stream()
                    .forEach(node -> node.move(e.getX() - mx, e.getY() - my));
        }
        mx = e.getX();
        my = e.getY();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mx = e.getX();
        my = e.getY();
    }
}
