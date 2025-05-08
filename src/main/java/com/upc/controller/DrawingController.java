package com.upc.controller;

import com.upc.model.ImageEditorModel;
import com.upc.view.DrawingPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class DrawingController implements MouseListener, MouseMotionListener {
    private int ox, oy;
    private DrawingPanel view;
    private ImageEditorModel model;

    public DrawingController(DrawingPanel view, ImageEditorModel model) {
        this.view = view;
        this.model = model;
        this.view.initializeCanvas();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        ox = e.getX();
        oy = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int selectedTool = model.getSelectedTool();

        switch (selectedTool) {
            case 0: // Stylo
                view.drawLine(ox, oy, x, y, model.getSelectedColor(), model.getStrokeWidth());
                ox = x;
                oy = y;
                break;
            case 1: // Gomme
                view.drawLine(ox, oy, x, y, view.getBackground(), model.getStrokeWidth()); // Utiliser la couleur de
                                                                                           // fond pour effacer
                ox = x;
                oy = y;
                break;
            case 2: // Cercle
                int width = Math.abs(x - ox);
                int height = Math.abs(y - oy);
                int startX = Math.min(ox, x);
                int startY = Math.min(oy, y);
                view.setPreviewShape("Cercle", startX, startY, width, height, model.getStrokeWidth(),
                        model.getSelectedColor());
                break;
            case 3: // Carré
                int side = Math.max(Math.abs(x - ox), Math.abs(y - oy));
                int squareX = Math.min(ox, x);
                int squareY = Math.min(oy, y);
                view.setPreviewShape("Carré", squareX, squareY, side, side, model.getStrokeWidth(),
                        model.getSelectedColor());
                break;
            default:
                // Les outils Cercle et Carré seront gérés dans mouseReleased
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int selectedTool = model.getSelectedTool();

        switch (selectedTool) {
            case 2: // Cercle
                int width = Math.abs(x - ox);
                int height = Math.abs(y - oy);
                int startX = Math.min(ox, x);
                int startY = Math.min(oy, y);
                view.drawOval(startX, startY, width, height, model.getSelectedColor(), model.getStrokeWidth());
                break;
            case 3: // Carré
                int side = Math.max(Math.abs(x - ox), Math.abs(y - oy));
                int squareX = Math.min(ox, x);
                int squareY = Math.min(oy, y);
                view.drawRect(squareX, squareY, side, side, model.getSelectedColor(), model.getStrokeWidth());
                break;
        }

        // Effacer la prévisualisation
        view.clearPreview();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // No specific action needed on move
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // No specific action needed on click
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // No specific action needed on enter
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // No specific action needed on exit
    }
}