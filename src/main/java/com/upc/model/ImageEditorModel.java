package com.upc.model;

import java.awt.Color;

/**
 * Modèle de données pour l'éditeur d'image.
 * Gère l'état courant de l'outil sélectionné, la couleur et l'épaisseur du trait.
 */
public class ImageEditorModel {
    // Indice de l'outil sélectionné (ex : pinceau, gomme, etc.)
    private int selectedTool = 0;
    // Couleur actuellement sélectionnée pour le dessin
    private Color selectedColor = new Color(0, 0, 0);
    // Épaisseur du trait sélectionnée
    private int strokeWidth = 1; // Épaisseur par défaut

    /**
     * Définit l'outil sélectionné.
     * @param selectedTool indice de l'outil
     */
    public void setSelectedTool(int selectedTool) {
        this.selectedTool = selectedTool;
    }

    /**
     * Définit la couleur sélectionnée.
     * @param selectedColor couleur à utiliser
     */
    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    /**
     * Définit l'épaisseur du trait.
     * @param strokeWidth épaisseur du trait
     */
    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    /**
     * Retourne l'indice de l'outil sélectionné.
     * @return indice de l'outil
     */
    public int getSelectedTool() {
        return selectedTool;
    }

    /**
     * Retourne la couleur sélectionnée.
     * @return couleur courante
     */
    public Color getSelectedColor() {
        return selectedColor;
    }

    /**
     * Retourne l'épaisseur du trait sélectionnée.
     * @return épaisseur du trait
     */
    public int getStrokeWidth() {
        return strokeWidth;
    }
}