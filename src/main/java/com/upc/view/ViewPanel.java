package com.upc.view;

import javax.swing.*;

import java.awt.*;

public class ViewPanel extends JPanel {

  private JPanel mainPanel;
  private JButton importButton;
  JScrollPane scrollPane;

  public ViewPanel() {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setPreferredSize(new Dimension(400, 400));
    this.setMinimumSize(new Dimension(400, 400));
    this.setBackground(Color.WHITE);

    this.mainPanel = new JPanel();
    mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
    mainPanel.setMinimumSize(new Dimension(200, 200));
    mainPanel.setBackground(Color.WHITE);

    scrollPane = new JScrollPane(mainPanel);
    scrollPane.setMinimumSize(new Dimension(200, 200));
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setBorder(null);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.setBackground(Color.WHITE);
    buttonPanel.setPreferredSize(new Dimension(1200, 40));
    buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

    this.importButton = new JButton("Import");
    importButton.setPreferredSize(new Dimension(100, 30));
    importButton.setMaximumSize(new Dimension(100, 30));
    importButton.setMinimumSize(new Dimension(100, 30));

    buttonPanel.add(importButton);
    buttonPanel.add(Box.createHorizontalGlue());

    add(buttonPanel);
    add(scrollPane);
    add(Box.createVerticalStrut(20));
  }

  public JButton getImportButton() {
    return importButton;
  }

  public JPanel getMainPanel() {
    return mainPanel;
  }

  public JScrollPane getScrollPane() {
    return scrollPane;
  }

}
