package com.upc.view;

import javax.swing.*;

import java.awt.*;

public class ViewPanel extends JPanel {

  private JPanel mainPanel;
  private JButton importButton;

  public ViewPanel() {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Grid with 4 columns and spacing
    this.setPreferredSize(new Dimension(400, 400)); // Set preferred size for the panel
    this.setMinimumSize(new Dimension(400, 400)); // Set minimum size for the panel
    this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400)); // Set maximum size for the panel
    setPreferredSize(new Dimension(400, 400)); // Set preferred size for the panel

    this.mainPanel = new JPanel(); // Initialize mainPanel
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS)); // Grid with 4 columns and spacing
    mainPanel.setPreferredSize(new Dimension(400, 400)); // Set preferred size for the panel
    mainPanel.setMinimumSize(new Dimension(400, 400)); // Set minimum size for the panel
    mainPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400)); // Set maximum size for the panel
    mainPanel.setBackground(Color.WHITE); // Set background color

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.setBackground(Color.WHITE);
    buttonPanel.setPreferredSize(new Dimension(1200, 40)); // Set preferred size for the panel
    buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Set maximum size for the panel

    this.importButton = new JButton("Import");
    importButton.setPreferredSize(new Dimension(100, 30));
    importButton.setMaximumSize(new Dimension(100, 30));
    importButton.setMinimumSize(new Dimension(100, 30));

    buttonPanel.add(importButton);
    buttonPanel.add(Box.createHorizontalGlue()); // Add glue to push the button to the left

    add(buttonPanel);
    add(mainPanel);
  }

  public JButton getImportButton() {
    return importButton;
  }

  public JPanel getMainPanel() {
    return mainPanel;
  }

}
