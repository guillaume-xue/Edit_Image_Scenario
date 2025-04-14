package com.upc.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ViewPanel extends JPanel {

  public ViewPanel(String path) {
    super();
    setLayout(new GridLayout(0, 4, 10, 10)); // Grid with 4 columns and spacing
    setPreferredSize(new Dimension(400, 400)); // Set preferred size for the panel
    displayImages(null);
  }

  public void displayImages(ArrayList<ImageIcon> imageIcons) {
    // Clear existing components
    removeAll();
    if (imageIcons != null && !imageIcons.isEmpty()) {
      for (ImageIcon imageIcon : imageIcons) {
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setTransferHandler(new TransferHandler("icon")); // Enable drag-and-drop for the icon
        add(imageLabel);
      }
    } else {
      JLabel noImagesLabel = new JLabel("No images found in /resources/image");
      add(noImagesLabel);
    }
    revalidate(); // Refresh layout
    repaint(); // Repaint the panel

  }

  public ArrayList<JLabel> getImageLabels() {
    ArrayList<JLabel> labels = new ArrayList<>();
    for (Component component : getComponents()) {
      if (component instanceof JLabel) {
        labels.add((JLabel) component);
      }
    }
    return labels;
  }
}
