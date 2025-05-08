package com.upc.view;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

public class ViewPanel extends JPanel {

  private JPanel mainPanel;
  private JButton addButton;

  public ViewPanel(String path) {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Grid with 4 columns and spacing
    setPreferredSize(new Dimension(400, 400)); // Set preferred size for the panel

    this.mainPanel = new JPanel(); // Initialize mainPanel
    mainPanel.setLayout(new GridLayout(0, 7, 10, 10)); // Grid with 4 columns and spacing
    mainPanel.setPreferredSize(new Dimension(400, 400)); // Set preferred size for the panel
    mainPanel.setBackground(Color.WHITE); // Set background color
    mainPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT); // Center alignment
    mainPanel.setAlignmentY(JPanel.CENTER_ALIGNMENT); // Center alignment

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.setBackground(Color.WHITE);
    buttonPanel.setPreferredSize(new Dimension(1200, 40)); // Set preferred size for the panel
    buttonPanel.setMaximumSize(new Dimension(1200, 40)); // Set maximum size for the panel

    this.addButton = new JButton("Import");
    addButton.setPreferredSize(new Dimension(100, 30));
    addButton.setMaximumSize(new Dimension(100, 30));
    addButton.setMinimumSize(new Dimension(100, 30));

    buttonPanel.add(addButton);

    add(buttonPanel);
    add(mainPanel);

  }

  public ArrayList<JLabel> getImageLabels() {
    ArrayList<JLabel> labels = new ArrayList<>();
    for (Component component : mainPanel.getComponents()) {
      if (component instanceof JLabel) {
        labels.add((JLabel) component);
      }
    }
    return labels;
  }

  public JButton getAddButton() {
    return addButton;
  }

  public JPanel getMainPanel() {
    return mainPanel;
  }

}
