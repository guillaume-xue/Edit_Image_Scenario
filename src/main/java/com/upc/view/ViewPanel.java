package com.upc.view;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ViewPanel extends JPanel {

  public ViewPanel(String path) {
    setLayout(new GridLayout(0, 4, 10, 10)); // Grid with 4 columns and spacing
    displayImages(path);
  }

  public void displayImages(String path) {
    File imageDir = new File(path);
    if (imageDir.exists() && imageDir.isDirectory()) {
      File[] imageFiles = imageDir.listFiles((dir, name) -> {
        String lowerName = name.toLowerCase();
        return lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg") ||
            lowerName.endsWith(".png") || lowerName.endsWith(".gif") ||
            lowerName.endsWith(".bmp");
      });

      if (imageFiles != null) {
        for (File imageFile : imageFiles) {
          ImageIcon originalIcon = new ImageIcon(imageFile.getAbsolutePath());
          Image resizedImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
          ImageIcon resizedIcon = new ImageIcon(resizedImage);
          JLabel imageLabel = new JLabel(resizedIcon);
          add(imageLabel);
        }
      }
    } else {
      JLabel noImagesLabel = new JLabel("No images found in /resources/image");
      add(noImagesLabel);
    }
    revalidate(); // Refresh layout
    repaint(); // Repaint the panel
  }
}
