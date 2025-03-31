package com.upc.model;

import java.awt.Color;

public class ImageEditorModel {
    int selectedTool = 0;
    Color selectedColor = new Color(0, 0, 0);

    public void setSelectedTool(int selectedTool) {
        this.selectedTool = selectedTool;
    }
    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }
    public int getSelectedTool() {
        return selectedTool;
    }
    public Color getSelectedColor() {
        return selectedColor;
    }

}
