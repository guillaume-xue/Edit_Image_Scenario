package com.upc.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ResizablePanel extends JPanel {

  private JLabel durationLabel;
  private ImageIcon icon;

  public ResizablePanel() {
    setBackground(Color.WHITE);
    setPreferredSize(new Dimension(100, 100));
    setMinimumSize(new Dimension(100, 100));
    durationLabel = new JLabel("");
    add(durationLabel);
  }

  public ResizablePanel(ImageIcon icon, int duration) {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setPreferredSize(new Dimension(duration, 100));

    this.icon = icon;

    JPanel durationPanel = new JPanel();
    durationPanel.setLayout(new BoxLayout(durationPanel, BoxLayout.X_AXIS));
    durationPanel.setPreferredSize(new Dimension(100, 20));
    durationPanel.setMinimumSize(new Dimension(100, 20));
    durationPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
    durationLabel = new JLabel(duration + "");
    durationPanel.add(durationLabel);
    durationPanel.add(Box.createHorizontalStrut(10));

    add(durationPanel);
    add(Box.createHorizontalStrut(40));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (icon != null) {
      int iconWidth = 38; // largeur désirée
      int iconHeight = 38; // hauteur désirée
      int y = 30; // position verticale à ajuster selon le layout
      int count = 1 + (getWidth() / iconWidth);

      for (int i = 0; i < count; i++) {
        int x = i * iconWidth;
        g.drawImage(icon.getImage(), x, y, iconWidth, iconHeight, this);
      }
    }
  }

  public void setDuration(String duration) {
    this.durationLabel.setText(duration);
  }

  public int getDuration() {
    if (durationLabel.getText().isEmpty()) {
      return 0;
    }
    return Integer.parseInt(durationLabel.getText());
  }

  public ImageIcon getIcon() {
    return icon;
  }
}