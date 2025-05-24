package com.upc.view;

import com.upc.controller.DrawingController;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DrawingPanel extends JPanel {
    private Dimension dim;
    private BufferedImage bi;
    private Graphics2D gi;
    private DrawingController controller;
    private int previewX, previewY, previewWidth, previewHeight, previewStrokeWidth;
    private Color previewColor;
    private boolean isPreviewing = false;
    private String previewShape = ""; // "Cercle" ou "Carré"

    public DrawingPanel() {
        super();
        setBackground(Color.WHITE);
        dim = new Dimension(500, 500);
    }

    public void initializeCanvas() {
        if (bi == null) {
            bi = new BufferedImage((int) dim.getWidth(), (int) dim.getHeight(), BufferedImage.TYPE_INT_ARGB);
            gi = (Graphics2D) bi.getGraphics();
            gi.setColor(getBackground());
            gi.fillRect(0, 0, (int) dim.getWidth(), (int) dim.getHeight());
            gi.setStroke(new BasicStroke(1));
        }
    }

    public void drawLine(int ox, int oy, int x, int y, Color color, int strokeWidth) {
        gi.setStroke(new BasicStroke(strokeWidth));
        gi.setColor(color);
        gi.drawLine(ox, oy, x, y);
        repaint();
    }

    public void setController(DrawingController controller) {
        this.controller = controller;
        addMouseListener(controller);
        addMouseMotionListener(controller);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bi != null) {
            g.drawImage(bi, 0, 0, null);
        }

        // Dessiner la forme temporaire si en mode prévisualisation
        if (isPreviewing) {
            Graphics2D g2d = (Graphics2D) g; // Convertir en Graphics2D pour gérer l'épaisseur
            g2d.setColor(previewColor); // Couleur de la prévisualisation
            g2d.setStroke(new BasicStroke(previewStrokeWidth)); // Appliquer l'épaisseur

            switch (previewShape) {
                case "Cercle":
                    g2d.drawOval(previewX, previewY, previewWidth, previewHeight);
                    break;
                case "Carré":
                    g2d.drawRect(previewX, previewY, previewWidth, previewHeight);
                    break;
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return dim;
    }

    public void drawOval(int x, int y, int width, int height, Color color, int strokeWidth) {
        gi.setStroke(new BasicStroke(strokeWidth));
        gi.setColor(color);
        gi.drawOval(x, y, width, height);
        repaint();
    }

    public void drawImageIcon(ImageIcon imageIcon) {
        if (imageIcon != null) {
            Image image = imageIcon.getImage();
            int width = imageIcon.getIconWidth();
            int height = imageIcon.getIconHeight();
            gi.drawImage(image, 0, 0, width, height, null);
            repaint();
        }
    }

    public void drawRect(int x, int y, int width, int height, Color color, int strokeWidth) {
        gi.setStroke(new BasicStroke(strokeWidth));
        gi.setColor(color);
        gi.drawRect(x, y, width, height);
        repaint();
    }

    public void setPreviewShape(String shape, int x, int y, int width, int height, int strokeWidth, Color color) {
        this.previewShape = shape;
        this.previewX = x;
        this.previewY = y;
        this.previewWidth = width;
        this.previewHeight = height;
        this.previewStrokeWidth = strokeWidth;
        this.previewColor = color;
        this.isPreviewing = true;
        repaint();
    }

    public void clearPreview() {
        this.isPreviewing = false;
        repaint();
    }
}