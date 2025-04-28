package com.upc.view;

import javax.swing.*;

import com.upc.controller.MouseController;
import com.upc.controller.TransferController;

import java.awt.*;
import java.util.ArrayList;

public class ViewPanel extends JPanel {

  private TransferController transferController;
  private MouseController mouseController;

  public ViewPanel(String path, TransferController transferController, MouseController mouseController) {
    super();
    setLayout(new GridLayout(0, 4, 10, 10)); // Grid with 4 columns and spacing
    setPreferredSize(new Dimension(400, 400)); // Set preferred size for the panel
    displayImages(null);
    this.transferController = transferController;
    this.mouseController = mouseController;
  }

  public void displayImages(ArrayList<ImageIcon> imageIcons) {
    // Clear existing components
    removeAll();
    if (imageIcons != null && !imageIcons.isEmpty()) {
      for (ImageIcon imageIcon : imageIcons) {
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setTransferHandler(transferController.new TransferViewPanel()); // Enable drag functionality
        imageLabel.addMouseListener(mouseController.new ViewPanelMouseController());
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
