package com.upc.model;

import java.awt.Color;

public class ImageEditorModel {
    private int selectedTool = 0;
    private Color selectedColor = new Color(0, 0, 0);
    private int strokeWidth = 1; // Épaisseur par défaut

    public void setSelectedTool(int selectedTool) {
        this.selectedTool = selectedTool;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getSelectedTool() {
        return selectedTool;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }
}