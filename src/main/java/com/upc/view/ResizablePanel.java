package com.upc.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ResizablePanel extends JPanel {

  private JLabel durationLabel;
  private JLabel iconLabel;

  public ResizablePanel() {
    setBackground(Color.WHITE);
    setPreferredSize(new Dimension(100, 100));
    setMinimumSize(new Dimension(100, 100));
    durationLabel = new JLabel();
  }

  public ResizablePanel(ImageIcon icon, int duration) {
    super();
    setPreferredSize(new Dimension(duration, 100));
    iconLabel = new JLabel(icon);
    durationLabel = new JLabel(duration + "");
    add(durationLabel);
    add(iconLabel);
  }

  public void setDuration(String duration) {
    this.durationLabel.setText(duration);
  }

  public int getDuration() {
    return Integer.parseInt(durationLabel.getText());
  }

  public ImageIcon getIcon() {
    return (ImageIcon) iconLabel.getIcon();
  }

}
