package com.upc.view;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Panneau de dessin personnalisé permettant de dessiner des formes, des images et de gérer la prévisualisation.
 * Utilisé dans l'éditeur d'image pour offrir des fonctionnalités de dessin interactives.
 */
public class DrawingPanel extends JPanel {
    // Image tampon pour le dessin
    private BufferedImage bi;
    // Contexte graphique associé à l'image tampon
    private Graphics2D gi;
    // Coordonnées et propriétés de la forme en prévisualisation
    private int previewX, previewY, previewWidth, previewHeight, previewStrokeWidth;
    private Color previewColor;
    private boolean isPreviewing = false;
    private String previewShape = ""; // "Cercle" ou "Carré"

    /**
     * Constructeur du panneau de dessin.
     * Initialise le fond en blanc.
     */
    public DrawingPanel() {
        super();
        setBackground(Color.WHITE);
    }

    /**
     * Dessine une ligne sur le canvas.
     * @param ox abscisse du point de départ
     * @param oy ordonnée du point de départ
     * @param x abscisse du point d'arrivée
     * @param y ordonnée du point d'arrivée
     * @param color couleur de la ligne
     * @param strokeWidth épaisseur du trait
     */
    public void drawLine(int ox, int oy, int x, int y, Color color, int strokeWidth) {
        gi.setStroke(new BasicStroke(strokeWidth));
        gi.setColor(color);
        gi.drawLine(ox, oy, x, y);
        repaint();
    }

    /**
     * Redéfinition de la méthode de dessin du composant.
     * Affiche le contenu du canvas et la forme temporaire si en mode prévisualisation.
     */
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

    /**
     * Retourne la taille préférée du panneau.
     * @return dimension courante du panneau
     */
    @Override
    public Dimension getPreferredSize() {
        return getSize();
    }

    /**
     * Dessine un ovale sur le canvas.
     */
    public void drawOval(int x, int y, int width, int height, Color color, int strokeWidth) {
        gi.setStroke(new BasicStroke(strokeWidth));
        gi.setColor(color);
        gi.drawOval(x, y, width, height);
        repaint();
    }

    /**
     * Dessine une image sur le canvas.
     * @param imageIcon image à dessiner
     */
    public void drawImageIcon(ImageIcon imageIcon) {
        if (imageIcon != null) {
            Image image = imageIcon.getImage();
            int width = imageIcon.getIconWidth();
            int height = imageIcon.getIconHeight();
            gi.drawImage(image, 0, 0, width, height, null);
            repaint();
        }
    }

    /**
     * Dessine un rectangle sur le canvas.
     */
    public void drawRect(int x, int y, int width, int height, Color color, int strokeWidth) {
        gi.setStroke(new BasicStroke(strokeWidth));
        gi.setColor(color);
        gi.drawRect(x, y, width, height);
        repaint();
    }

    /**
     * Définit la forme à prévisualiser (cercle ou carré) et ses propriétés.
     */
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

    /**
     * Efface la prévisualisation de la forme temporaire.
     */
    public void clearPreview() {
        this.isPreviewing = false;
        repaint();
    }

    /**
     * Vérifie si le canvas est vide (tout blanc).
     * @return true si vide, false sinon
     */
    public boolean isEmpty() {
        if (bi == null)
            return true;
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

    /**
     * Efface tout le contenu du canvas (remplit en blanc).
     */
    public void clearAll() {
        if (bi == null)
            return;
        gi.setColor(Color.WHITE);
        gi.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        repaint();
    }

    /**
     * Retourne l'image tampon du panneau.
     * @return BufferedImage courante
     */
    public BufferedImage getBufferedImage() {
        return bi;
    }

    /**
     * Redimensionne le panneau et adapte le canvas à la nouvelle taille.
     */
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        resizeCanvas(width, height);
    }

    /**
     * Redimensionne le canvas interne à la nouvelle taille du panneau.
     * Conserve le contenu existant si possible.
     */
    private void resizeCanvas(int newWidth, int newHeight) {
        if (newWidth <= 0 || newHeight <= 0)
            return;

        if (bi == null) {
            // Première initialisation
            initializeCanvas();
            return;
        }

        if (bi.getWidth() == newWidth && bi.getHeight() == newHeight)
            return;

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
        repaint();
    }

    /**
     * Initialise le canvas interne si nécessaire (appelé lors du rendu).
     */
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
        }
    }

    /**
     * Recherche et retourne la vue parente de type ImageEditorView.
     * @return l'ImageEditorView parent ou null si non trouvée
     */
    public ImageEditorView getImageEditorView() {
        java.awt.Container parent = getParent();
        while (parent != null) {
            if (parent instanceof ImageEditorView) {
                return (ImageEditorView) parent;
            }
            parent = parent.getParent();
        }
        return null; // Si aucun parent n'est trouvé
    }
}