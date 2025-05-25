package com.upc.view;

import com.upc.controller.DrawingController;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DrawingPanel extends JPanel {
    private BufferedImage bi;
    private Graphics2D gi;
    private DrawingController controller;
    private int previewX, previewY, previewWidth, previewHeight, previewStrokeWidth;
    private Color previewColor;
    private boolean isPreviewing = false;
    private String previewShape = ""; // "Cercle" ou "Carré"
    private Dimension dim;

    public DrawingPanel() {
        super();
        setBackground(Color.WHITE);
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
        initializeCanvas(); // S'assurer que le canvas est initialisé
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
        return getSize();
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

    public boolean isEmpty() {
        if (bi == null) return true;
        int w = bi.getWidth();
        int h = bi.getHeight();
        int[] pixels = new int[w * h];
        bi.getRGB(0, 0, w, h, pixels, 0, w);
        for (int rgb : pixels) {
            if ((rgb & 0x00FFFFFF) != 0x00FFFFFF) { // Différent de blanc
                return false;
            }
        }
        return true;
    }

    public void clearAll() {
        if (bi == null) return;
        gi.setColor(Color.WHITE);
        gi.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        repaint();
    }

    public BufferedImage getBufferedImage() {
        return bi;
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        resizeCanvas(width, height);
    }

    private void resizeCanvas(int newWidth, int newHeight) {
        if (newWidth <= 0 || newHeight <= 0) return;

        if (bi == null) {
            // Première initialisation
            dim = new Dimension(newWidth, newHeight);
            initializeCanvas();
            return;
        }

        if (bi.getWidth() == newWidth && bi.getHeight() == newHeight) return;

        BufferedImage newBi = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newBi.createGraphics();
        // Remplir en blanc
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, newWidth, newHeight);
        // Dessiner l'ancienne image (rognée si besoin)
        int w = Math.min(bi.getWidth(), newWidth);
        int h = Math.min(bi.getHeight(), newHeight);
        g2.drawImage(bi, 0, 0, w, h, 0, 0, w, h, null);
        g2.dispose();

        bi = newBi;
        gi = (Graphics2D) bi.getGraphics();
        dim = new Dimension(newWidth, newHeight);
        repaint();
    }

    public void initializeCanvas() {
        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) {
            // Si le panel n'est pas encore affiché, utilise une taille par défaut minimale
            w = 1;
            h = 1;
        }
        if (bi == null || bi.getWidth() != w || bi.getHeight() != h) {
            bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            gi = (Graphics2D) bi.getGraphics();
            gi.setColor(getBackground());
            gi.fillRect(0, 0, w, h);
            gi.setStroke(new BasicStroke(1));
            dim = new Dimension(w, h);
        }
    }
}